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
   // val currentWeatherResponse: CurrentWeatherResponse,
  //  val hourlyForecast: List<CurrentWeatherResponse>,
   // val dailyForecast: List<CurrentWeatherResponse>

)

/*class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromCurrentWeatherResponse(value: CurrentWeatherResponse): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toCurrentWeatherResponse(value: String): CurrentWeatherResponse {
        return gson.fromJson(value, CurrentWeatherResponse::class.java)
    }

    @TypeConverter
    fun fromCurrentWeatherResponseList(value: List<CurrentWeatherResponse>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toCurrentWeatherResponseList(value: String): List<CurrentWeatherResponse> {
        val type = object : TypeToken<List<CurrentWeatherResponse>>() {}.type
        return gson.fromJson(value, type)
    }
}*/
