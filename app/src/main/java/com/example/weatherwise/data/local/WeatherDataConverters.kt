package com.example.weatherwise.data.local

import androidx.room.TypeConverter
import com.example.weatherwise.data.model.currentWeather.CurrentWeatherResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class WeatherDataConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromCurrentWeatherResponse(currentWeatherResponse: CurrentWeatherResponse): String {
        return gson.toJson(currentWeatherResponse)
    }

    @TypeConverter
    fun toCurrentWeatherResponse(currentWeatherString: String): CurrentWeatherResponse {
        return gson.fromJson(currentWeatherString, CurrentWeatherResponse::class.java)
    }

    @TypeConverter
    fun fromForecastList(forecast: List<CurrentWeatherResponse>): String {
        return gson.toJson(forecast)
    }

    @TypeConverter
    fun toForecastList(forecastString: String): List<CurrentWeatherResponse> {
        val type = object : TypeToken<List<CurrentWeatherResponse>>() {}.type
        return gson.fromJson(forecastString, type)
    }
}

/*class WeatherDataConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromCurrentWeatherResponse(currentWeatherResponse: CurrentWeatherResponse): String {
        return gson.toJson(currentWeatherResponse)
    }

    @TypeConverter
    fun toCurrentWeatherResponse(currentWeatherString: String): CurrentWeatherResponse {
        return gson.fromJson(currentWeatherString, CurrentWeatherResponse::class.java)
    }

    @TypeConverter
    fun fromHourlyForecast(hourlyForecast: List<CurrentWeatherResponse>): String {
        return gson.toJson(hourlyForecast)
    }

    @TypeConverter
    fun toHourlyForecast(hourlyForecastString: String): List<CurrentWeatherResponse> {
        val type = object : TypeToken<List<CurrentWeatherResponse>>() {}.type
        return gson.fromJson(hourlyForecastString, type)
    }

    @TypeConverter
    fun fromDailyForecast(dailyForecast: List<CurrentWeatherResponse>): String {
        return gson.toJson(dailyForecast)
    }

    @TypeConverter
    fun toDailyForecast(dailyForecastString: String): List<CurrentWeatherResponse> {
        val type = object : TypeToken<List<CurrentWeatherResponse>>() {}.type
        return gson.fromJson(dailyForecastString, type)
    }
}*/
