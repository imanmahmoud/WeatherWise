package com.example.weatherwise.utils

import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import java.util.Date
import java.util.Locale

object DateTimeUtils {

    fun formatUnixTimestampToDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("EEEE, dd MMMM yyyy | hh:mm a", Locale.ENGLISH)
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(Date(timestamp * 1000))
    }


}