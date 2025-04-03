package com.example.weatherwise.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.weatherwise.data.model.currentWeather.CurrentWeatherResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "favourite_locations")
data class FavouriteLocation(
    @PrimaryKey
    val cityName: String,
    val latitude: Double,
    val longitude: Double,
)


