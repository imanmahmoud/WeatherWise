package com.example.weatherwise.data.remote

import android.util.Log
import com.example.weatherwise.data.model.currentWeather.CurrentWeatherResponse
import com.example.weatherwise.data.model.forecastWeather.ForecastWeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherRemoteDataSourceImpl(private val weatherApiService: WeatherApiService) : RemoteDataSource  {
    override fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
        apiKey: String,
        units: String,
        language: String
    ): Flow<CurrentWeatherResponse/*?*/> = flow {
       Log.i("TAG", "getCurrentWeather: before ifffffffff repo")

      //  try {
            val response = weatherApiService.getCurrentWeather(latitude, longitude, apiKey, units, language)
            if (response.isSuccessful) {
                Log.i("TAG", "getCurrentWeather: suscceesssssssss repo")
                response.body()?.let { weatherData ->
                    emit(weatherData)
                } ?: {
                    Log.i("TAG", "getCurrentWeather: empty response body repo")
                    throw Exception("Empty response body")
                }
            } else {
                //network error
                Log.i("TAG", "getCurrentWeather: elseeeee repo")
                throw Exception("Error ${response.code()}: ${response.message()}")
            }
        /*}catch (e:Exception){
            Log.i("TAG", "getCurrentWeather: catch errorrrrr repo")
            throw e
        }*/
    }

    override fun getForecastWeather(
        latitude: Double,
        longitude: Double,
        apiKey: String,
        units: String,
        language: String
    ): Flow<ForecastWeatherResponse> = flow {
        val response = weatherApiService.getForecastWeather(latitude, longitude, apiKey, units, language)
        if (response.isSuccessful) {
            response.body()?.let { forecastData ->
                  emit(forecastData)
            } ?: throw Exception("Empty response body")
        }else{
            throw Exception("Error ${response.code()}: ${response.message()}")

        }
    }


}