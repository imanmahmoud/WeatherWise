package com.example.weatherwise.data.remote

import com.example.weatherwise.data.model.currentWeather.CurrentWeatherResponse
import com.example.weatherwise.data.model.forecastWeather.ForecastWeatherResponse
import com.example.weatherwise.utils.ApiConstants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String = ApiConstants.WEATHER_API_KEY,
        @Query("units") units: String = "metric",
        @Query("lang") language: String = "en"
    ): Response<CurrentWeatherResponse>

    @GET("data/2.5/forecast")
    suspend fun getForecastWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String = ApiConstants.WEATHER_API_KEY,
        @Query("units") units: String = "metric",
        @Query("lang") language: String = "en"
    ): Response<ForecastWeatherResponse>
}
