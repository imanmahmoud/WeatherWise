package com.example.weatherwise.data.model.forecastWeather

import com.example.weatherwise.data.model.currentWeather.CurrentWeatherResponse

data class ForecastWeatherResponse(
    val cod: String,
    val message: Int,
    val cnt: Int,
    val list: List<CurrentWeatherResponse>,
    val city: City
)