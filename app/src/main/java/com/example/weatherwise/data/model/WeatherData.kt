package com.example.weatherwise.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.weatherwise.data.local.WeatherDataConverters
import com.example.weatherwise.data.model.currentWeather.CurrentWeatherResponse

@Entity(tableName = "weather_data")
@TypeConverters(WeatherDataConverters::class) // Register converters
data class WeatherData(
    @PrimaryKey(autoGenerate = false) val id: Int = 1, // Fixed ID for replacing old data
    var currentWeatherResponse: CurrentWeatherResponse,
    var hourlyForecast: List<CurrentWeatherResponse>,
    var dailyForecast: List<CurrentWeatherResponse>
)



