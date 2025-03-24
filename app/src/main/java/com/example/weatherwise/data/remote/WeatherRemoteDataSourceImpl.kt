package com.example.weatherwise.data.remote

import android.util.Log
import com.example.weatherwise.data.model.currentWeather.CurrentWeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherRemoteDataSourceImpl(private val weatherApiService: WeatherApiService) : RemoteDataSource  {
    override fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
        apiKey: String
    ): Flow<CurrentWeatherResponse?> = flow {
        Log.i("TAG", "getCurrentWeather: before ifffffffff")
        val response = weatherApiService.getCurrentWeather(latitude, longitude, apiKey)
        if (response.isSuccessful) {
            Log.i("TAG", "getCurrentWeather: suscceesssssssss")
            response.body()?.let { weatherData ->
                emit(weatherData)
            } ?: throw Exception("Empty response body")
        } else {
            Log.i("TAG", "getCurrentWeather: elseeeee")
            throw Exception("Error ${response.code()}: ${response.message()}")
        }
    }

}