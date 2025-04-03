package com.example.weatherwise.favourite.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherwise.data.model.FavouriteLocation
import com.example.weatherwise.data.repo.ResultState
import com.example.weatherwise.data.repo.WeatherRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


class FavouriteViewModel(private val repo: WeatherRepository) : ViewModel() {
    private var _favouriteLocations =
        MutableStateFlow<ResultState<List<FavouriteLocation>>>(ResultState.Loading())
    val favouriteLocations = _favouriteLocations.asStateFlow()

    private val _message = MutableSharedFlow<String>()
    val message = _message.asSharedFlow()

    var recentlyDeletedLocation:FavouriteLocation? =null

    fun restoreFavouriteLocation() {
        recentlyDeletedLocation?.let { favouriteLocation ->
            viewModelScope.launch {
                val result = repo.insertFavouriteLocation(favouriteLocation)
                if (result > 0) {
                    Log.i("TAG", "restoreFavouriteLocation: added again")
                    _message.emit("Location restored successfully")
                } else {
                    Log.i("TAG", "restoreFavouriteLocation: failed")
                    _message.emit("Failed to restore location")
                }
            }
        }
    }

    fun deleteFavouriteLocation(favouriteLocation: FavouriteLocation) {
        viewModelScope.launch {

            try {
                recentlyDeletedLocation = favouriteLocation
                val result = repo.deleteFavouriteLocation(favouriteLocation)
                if (result > 0) {
                    _message.emit("Location deleted successfully")
                } else {
                    _message.emit("Failed to delete location")
                }
            }catch (ex: Exception){
                _message.emit("Couldn't delete location")
            }

        }
    }

    fun getAllFavouriteLocations() {
        Log.i("TAG", "getAllFavouriteLocations: intro")
        viewModelScope.launch {
            try {
                val result = repo.getAllFavouriteLocations()
                result
                    .catch { e ->
                        Log.e("FavouriteViewModel", "Error fetching favorite locations", e)
                        val errorMessage = when (e) {
                            is java.io.IOException -> "Network issue. Please check your connection."
                            is java.sql.SQLException -> "Database error. Please try again later."
                            else -> "An unexpected error occurred."
                        }
                        _favouriteLocations.value = ResultState.Failure(errorMessage)
                    }
                    .collect {
                            locations ->
                        val sortedLocations = locations.sortedBy { it.cityName }
                        Log.i("TAG", "getAllFavouriteLocations: Sorted Data")
                        _favouriteLocations.value = ResultState.Success(sortedLocations)
                    }
            } catch (e: Exception) {
                _favouriteLocations.value = ResultState.Failure("Unexpected error: ${e.localizedMessage} ")
            }
        }
    }

    class FavouriteProductsFactory(private val repo: WeatherRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FavouriteViewModel(repo) as T
        }
    }


}






