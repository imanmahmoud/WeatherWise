package com.example.weatherwise.data.sharedPreference

import android.content.Context
import android.content.SharedPreferences
import java.util.Locale

class PreferenceHelper(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("settings_prefs", Context.MODE_PRIVATE)

    companion object {
        const val TEMP_UNIT_KEY = "temp_unit"
      //  const val WIND_SPEED_UNIT_KEY = "wind_speed_unit"
        const val LANGUAGE_KEY = "language"
        const val LOCATION_KEY = "location"
        const val MAP_LATITUDE_KEY = "mapLatitude"
        const val MAP_LONGITUDE_KEY = "mapLongitude"
    }

    fun saveMapGPSLocation(latitude: Double, longitude: Double) {
        sharedPreferences.edit().putFloat(MAP_LATITUDE_KEY, latitude.toFloat()).apply()
        sharedPreferences.edit().putFloat(MAP_LONGITUDE_KEY, longitude.toFloat()).apply()
        sharedPreferences.edit().commit()
    }
    fun getMapGPSLocation(): Pair<Float, Float> {
        val latitude = sharedPreferences.getFloat(MAP_LATITUDE_KEY, 0f)
        val longitude = sharedPreferences.getFloat(MAP_LONGITUDE_KEY, 0f)
        return Pair(latitude, longitude)
    }
    fun saveTempUnit(tempUnit: String) {
        sharedPreferences.edit().putString(TEMP_UNIT_KEY, tempUnit).apply()
        sharedPreferences.edit().commit()
    }

    fun getTempUnit(): String {
        return sharedPreferences.getString(TEMP_UNIT_KEY, "metric") ?: "metric"
    }

    fun getWindSpeedUnit(): String {
        val tempUnit = getTempUnit()
        return if (tempUnit == "imperial") "miles/hr" else "meter/sec"
    }

    fun saveLanguage(language: String) {
        sharedPreferences.edit().putString(LANGUAGE_KEY, language).apply()
        sharedPreferences.edit().commit()
    }

   /* fun getLanguage(): String {
        return sharedPreferences.getString(LANGUAGE_KEY, "en") ?: "en"
    }*/

    fun getLanguage(): String {
        val storedLanguage = sharedPreferences.getString(LANGUAGE_KEY, "en") ?: "en"
        return if (storedLanguage.isEmpty()) Locale.getDefault().language else storedLanguage
    }

    fun saveLocation(location: String) {
        sharedPreferences.edit().putString(LOCATION_KEY, location).apply()
        sharedPreferences.edit().commit()
    }

    fun getLocation(): String {
        return sharedPreferences.getString(LOCATION_KEY, "GPS") ?: "GPS"
    }
}
