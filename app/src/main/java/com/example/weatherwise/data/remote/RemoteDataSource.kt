package com.example.weatherwise.data.remote

import com.example.weatherwise.data.model.currentWeather.CurrentWeatherResponse
import com.example.weatherwise.data.model.forecastWeather.ForecastWeatherResponse
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    /*suspend*/ fun getCurrentWeather(latitude: Double, longitude: Double, apiKey: String, units: String = "metric", language: String = "en"): Flow<CurrentWeatherResponse/*?*/>

    fun getForecastWeather(latitude: Double, longitude: Double, apiKey: String, units: String = "metric", language: String = "en"): Flow<ForecastWeatherResponse>
}