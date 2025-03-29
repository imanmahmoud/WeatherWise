package com.example.weatherwise.favourite.map

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherwise.BuildConfig
import com.example.weatherwise.R
import com.example.weatherwise.WeatherWiseApplication
import com.example.weatherwise.data.local.WeatherDatabase
import com.example.weatherwise.data.local.WeatherLocalDataSourceImpl

import com.example.weatherwise.data.remote.RetrofitHelper
import com.example.weatherwise.data.remote.WeatherRemoteDataSourceImpl
import com.example.weatherwise.data.repo.WeatherRepositoryImpl
import com.example.weatherwise.ui.theme.LightPurple
import com.example.weatherwise.ui.theme.PurplePink
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.compose.autocomplete.components.PlacesAutocompleteTextField
import com.google.android.libraries.places.compose.autocomplete.models.AutocompletePlace
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun MapScreen(/*showFAB: MutableState<Boolean>*/) {

   // showFAB.value = false
    val context = LocalContext.current

    val application = context.applicationContext as WeatherWiseApplication
    val placesClient = application.placesClient

    val db = WeatherDatabase.getInstance(context = context)

    val mapScreenFactory = MapViewModel.MapScreenViewModelFactory(
        placesClient = placesClient,
        repository = WeatherRepositoryImpl.getInstance(
            WeatherRemoteDataSourceImpl(RetrofitHelper.service),
            WeatherLocalDataSourceImpl(db.weatherDao())
        )
    )
    val viewModel: MapViewModel = viewModel(factory = mapScreenFactory)



//OLD ONE
    // Initialize Places API (outside ViewModel)
   /* Places.initializeWithNewPlacesApiEnabled(context, BuildConfig.MAPS_API_KEY )
    val placesClient: PlacesClient = Places.createClient(context)

    // Create ViewModel with custom factory
    val mapScreenFactory = MapViewModel.MapScreenViewModelFactory(
        placesClient = placesClient,
        repository = WeatherRepositoryImpl.getInstance(
            WeatherRemoteDataSourceImpl(RetrofitHelper.service),
           WeatherLocalDataSourceImpl(db.weatherDao())
        )
    )
    val viewModel: MapViewModel = viewModel(factory = mapScreenFactory)*/

    val searchText by viewModel.searchText.collectAsStateWithLifecycle()
    val predictions by viewModel.predictions.collectAsStateWithLifecycle()
    val selectedLocation by viewModel.selectedLocation.collectAsStateWithLifecycle()

    val markerState = rememberMarkerState(
        position = LatLng(
            selectedLocation?.latitude ?: 31.2001,
            selectedLocation?.longitude ?: 29.9187,
        )
    )
    val cameraPositionState = rememberCameraPositionState{position = CameraPosition.fromLatLngZoom(markerState.position, 10f)}

    LaunchedEffect(Unit) {
        viewModel.message.collect {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(selectedLocation) {
        selectedLocation?.let {
            val newLatLng = LatLng(it.latitude, it.longitude)

            // Update marker position
            markerState.position = newLatLng

            // Move camera smoothly to new location
            cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(newLatLng, 12f))
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(mapType = MapType.NORMAL),
            onMapClick = { latLng ->
                markerState.position = latLng
                viewModel.updateSelectedLocation(latLng)
            }
        ) {

           Marker(
                state = markerState,
                title = "Selected Location",
                snippet = "Marker at selected location"
            )
        }

        PlacesAutocompleteTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            searchText = searchText,
            textFieldMaxLines = 1,
            predictions = predictions.map {
                AutocompletePlace(
                    placeId = it.placeId,
                    primaryText = it.getPrimaryText(null),
                    secondaryText = it.getSecondaryText(null)
                )
            },
            onQueryChanged = { viewModel.onSearchQueryChanged(it) },
            onSelected = { autocompletePlace ->
                viewModel.onPlaceSelected(autocompletePlace.placeId)
            },
            selectedPlace = null
        )


        selectedLocation?.let { location ->

           /* val favouriteLocation = FavouriteLocation(location.latitude,location.longitude)
            val address = getAddressFromLocation(favouriteLocation)*/
            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(colorResource(R.color.white))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "address", style = MaterialTheme.typography.titleMedium, color = colorResource(R.color.black), fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Latitude: ${location.latitude}")
                    Text(text = "Longitude: ${location.longitude}")
                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { viewModel.insertFavouriteLocation(selectedLocation!!.latitude, selectedLocation!!.longitude) },
                        colors = ButtonDefaults.buttonColors(PurplePink/*colorResource(R.color.teal_700)*/),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Add to Favourites",
                            style = MaterialTheme.typography.titleMedium,
                            color = colorResource(R.color.white),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

//class FavouriteLocation(latitude: Double, longitude: Double)
