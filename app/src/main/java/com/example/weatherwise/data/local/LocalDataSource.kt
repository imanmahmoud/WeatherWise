package com.example.weatherwise.data.local


import com.example.weatherwise.data.model.FavouriteLocation
import com.example.weatherwise.data.model.WeatherData
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun getAllFavouriteLocations(): Flow<List<FavouriteLocation>>
    suspend fun insertFavouriteLocation(favouriteLocation: FavouriteLocation): Long
    suspend fun deleteFavouriteLocation(favouriteLocation: FavouriteLocation): Int
    suspend fun getWeatherData(): Flow<WeatherData>
    suspend fun insertWeatherData(weatherData: WeatherData):Long


}