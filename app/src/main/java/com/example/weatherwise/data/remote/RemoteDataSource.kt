package com.example.weatherwise.data.remote

import com.example.weatherwise.data.model.currentWeather.CurrentWeatherResponse
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    /*suspend*/ fun getCurrentWeather(latitude: Double, longitude: Double, apiKey: String): Flow<CurrentWeatherResponse?>

}