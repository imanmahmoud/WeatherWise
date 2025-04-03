package com.example.weatherwise.data.model

import androidx.room.Entity
import androidx.room.TypeConverters
import com.example.weatherwise.data.local.WeatherDataConverters
import com.example.weatherwise.data.model.currentWeather.CurrentWeatherResponse

@Entity(tableName = "weather_data",primaryKeys = ["latitude","longitude"])
@TypeConverters(WeatherDataConverters::class) // Register converters
data class WeatherData(
   // @PrimaryKey(autoGenerate = false) val id: Int = 1, // Fixed ID for replacing old data
    val latitude: Double,
    val longitude: Double,
    var currentWeatherResponse: CurrentWeatherResponse,
    var hourlyForecast: List<CurrentWeatherResponse>,
    var dailyForecast: List<CurrentWeatherResponse>
)



