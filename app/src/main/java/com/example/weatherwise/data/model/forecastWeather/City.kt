package com.example.weatherwise.data.model.forecastWeather

import com.example.weatherwise.data.model.currentWeather.Coord

data class City(
    val id: Int,
    val name: String,
    val coord: Coord,
    val country: String,
    val population: Int,
    val timezone: Int,
    val sunrise: Int,
    val sunset: Int
)