package com.example.weatherwise.data.local


import com.example.weatherwise.data.model.FavouriteLocation
import com.example.weatherwise.data.model.WeatherData
import com.example.weatherwizard.alert.model.AlertModel
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun getAllFavouriteLocations(): Flow<List<FavouriteLocation>>
    suspend fun insertFavouriteLocation(favouriteLocation: FavouriteLocation): Long
    suspend fun deleteFavouriteLocation(favouriteLocation: FavouriteLocation): Int


    suspend fun getWeatherData(lat: Double, lon: Double): Flow<WeatherData>
    suspend fun insertWeatherData(weatherData: WeatherData):Long

    suspend fun insertAlert(alert: AlertModel)
    suspend fun deleteAlert(alert: AlertModel)
    suspend fun getAllAlerts(): Flow<List<AlertModel>>



}