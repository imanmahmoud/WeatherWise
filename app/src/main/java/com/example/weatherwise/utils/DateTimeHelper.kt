package com.example.weatherwise.utils

import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import java.util.Date
import java.util.Locale

object DateTimeHelper {

    fun formatUnixTimestampToDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("EEEE, dd MMMM yyyy | hh:mm a", Locale.getDefault())
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(Date(timestamp * 1000))
    }

    fun formatUnixTimestampToHour(dt: Long): String {
        val date = Date(dt * 1000) // Convert seconds to milliseconds
        val format = SimpleDateFormat("h a", Locale.getDefault()) // "h" for hour, "a" for AM/PM
        return format.format(date).lowercase() // Convert "PM" -> "pm"
    }

    fun formatUnixTimestampToDay(timestamp: Long): String {
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault()) // "EEEE" for full day name
        val date = Date(timestamp * 1000) // Convert seconds to milliseconds
        return sdf.format(date)
    }




}