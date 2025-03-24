package com.example.weatherwise.utils

import com.example.weatherwise.R

object WeatherIconHelper {
    fun getWeatherIcon(weatherMain: String): Int {
        return when (weatherMain) {
            "Clouds" -> R.drawable.mostly_cloudy
            "Clear" -> R.drawable.sunny_weather
            "Rain" -> R.drawable.rain_weather
            "Snow" -> R.drawable.snow_weather
            "Thunderstorm" -> R.drawable.thunder_weather
            "Drizzle" -> R.drawable.rain_weather
            "Atmosphere" -> R.drawable.atmosphere_weather
            else -> R.drawable.cloudy_weather
        }
    }


}