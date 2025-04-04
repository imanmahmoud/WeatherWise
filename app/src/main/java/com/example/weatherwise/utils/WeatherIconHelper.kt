package com.example.weatherwise.utils

import com.example.weatherwise.R

object WeatherIconHelper {
    fun getWeatherIcon(weatherMain: String): Int {
        return when (weatherMain) {

            "Clouds" -> R.drawable.mostly_cloud
            "Clear" -> R.drawable.clear_sunny
            "Rain" -> R.drawable.rain_weather
            "Snow" -> R.drawable.snow_weather
            "Thunderstorm" -> R.drawable.thunder_weather
            "Drizzle" -> R.drawable.rain_weather
            "Atmosphere" -> R.drawable.atmosphere_weather
            else -> R.drawable.cloudy_weather
        }
    }


}