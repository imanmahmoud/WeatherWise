package com.example.weatherwise.data.model.currentWeather

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)