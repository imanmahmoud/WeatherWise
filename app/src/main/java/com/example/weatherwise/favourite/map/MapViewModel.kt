package com.example.weatherwise.favourite.map

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherwise.data.model.FavouriteLocation
import com.example.weatherwise.data.repo.WeatherRepository

import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MapViewModel(private val placesClient: PlacesClient, private val repository: WeatherRepository): ViewModel() {


//Old ONE
    class MapScreenViewModelFactory(private val placesClient: PlacesClient,private val repository: WeatherRepository) : ViewModelProvider.Factory
    {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MapViewModel(placesClient,repository) as T
        }
    }


    /*class MapScreenViewModelFactory(
        private val placesClient: PlacesClient,
        private val repository: WeatherRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
               @Suppress("UNCHECKED_CAST")
                return MapViewModel(placesClient, repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }*/


    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _predictions = MutableStateFlow<List<AutocompletePrediction>>(emptyList())
    val predictions= _predictions.asStateFlow()

    private val _selectedLocation = MutableStateFlow<Location?>(null)
    val selectedLocation = _selectedLocation.asStateFlow()

    private val _message = MutableSharedFlow<String>()
    val message = _message.asSharedFlow()

    //private val locationBias: LocationBias = RectangularBounds.newInstance(LatLng(39.9, -105.5), // SW lat, lng LatLng(40.1, -105.0)  // NE lat, lng)

    // Update search query and fetch predictions
    fun onSearchQueryChanged(query: String) {
        _searchText.value = query
        fetchPredictions(query)
    }

    //Fetch place details when a prediction is selected
    fun onPlaceSelected(placeId: String) {
        fetchPlaceDetails(placeId)
        _searchText.value = "" // Clear search text after selection
        _predictions.value = emptyList() // Clear predictions
    }


    // Fetch autocomplete predictions
    //old
    private fun fetchPredictions(query: String) {
        if (query.isEmpty()) {
            _predictions.value = emptyList() // Clear predictions when input is empty
            return
        }

        viewModelScope.launch {
            val request = FindAutocompletePredictionsRequest.builder()
                .setQuery(query)
                .build()

            placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener { response ->
                    _predictions.value = response.autocompletePredictions
                }
                .addOnFailureListener { exception ->
                    Log.e("MapViewModel", "Error fetching predictions: ${exception.message}")
                }
        }
    }

    // Fetch place details
    fun fetchPlaceDetails(placeId: String) {
        val placeFields = listOf(Place.Field.LAT_LNG)
        val request = FetchPlaceRequest.builder(placeId, placeFields).build()
        placesClient.fetchPlace(request)
            .addOnSuccessListener { response ->
                response.place.latLng?.let { latLng ->
                    _selectedLocation.value = Location("").apply {
                        latitude = latLng.latitude
                        longitude = latLng.longitude
                    }

                }
            }
            .addOnFailureListener { exception ->
                Log.e("MapViewModel", "Error fetching place details: ${exception.message}")
            }
    }

    fun updateSelectedLocation(latLng: LatLng) {
        _selectedLocation.value = Location("").apply {
            latitude = latLng.latitude
            longitude = latLng.longitude
        }
    }

    fun insertFavouriteLocation(favouriteLocation: FavouriteLocation) {
        viewModelScope.launch{
            try {
                val result = repository.insertFavouriteLocation(favouriteLocation)
                if (result > 0) {
                    _message.emit("Location Added Successfully!")
                } else {
                    _message.emit("Location is already in Favourites")
                }
            } catch (ex: Exception){
                _message.emit("Couldn't Add Location To Favourites ${ex.message}")
            }
        }
    }

}