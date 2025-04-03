package com.example.weatherwise.utils


import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.weatherwise.R
import java.util.Locale

fun convertToArabicNumbers(number: String): String {
    val arabicDigits = arrayOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')
    return number.map { if (it.isDigit()) arabicDigits[it.digitToInt()] else it }.joinToString("")
}

fun formatNumberBasedOnLanguage(number: String): String {
    val language = Locale.getDefault().language
    return if (language == "ar") convertToArabicNumbers(number) else number

}


@Composable
fun getTempUnitDisplay(unit: String): String {
    val context = LocalContext.current
    return when (unit) {
        "metric" -> context.getString(R.string.celsius)
        "imperial" -> context.getString(R.string.fahrenheit)
        "standard" -> context.getString(R.string.kelvin)
        else -> context.getString(R.string.celsius)
    }
}

@Composable
fun getWindSpeedUnitDisplay(unit: String): String {
    val context = LocalContext.current
    return when (unit) {
        //"meter/sec" -> context.getString(R.string.meter_sec)
        "imperial" -> context.getString(R.string.miles_hour)
        else -> context.getString(R.string.meter_sec)
    }
}

fun mapDisplayToTempUnit(displayUnit: String): String {
    return when (displayUnit) {
        "°س","°C" -> "metric"
        "°ف", "°F" -> "imperial"
        "°ك","°K" -> "standard"
        else -> "metric"
    }
}

fun mapDisplayToLanguage(displayLanguage: String): String {
    return when (displayLanguage) {
        "العربية","Arabic" -> "ar"
        "الإنجليزية", "English" -> "en"
        "الافتراضية","Default" -> ""
        else -> ""
    }
}

fun mapDisplayToLocation(displayLocation: String): String {
    return when (displayLocation) {
        "نظام تحديد المواقع","GPS" -> "GPS"
        "الخريطة", "Map" -> "Map"
        else -> "GPS"
    }
}


@Composable
fun LottieWithControls() {
    val composition = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.empty))
    val progress by animateLottieCompositionAsState(
        composition = composition.value,
        iterations = LottieConstants.IterateForever,
        isPlaying = true // Change this to control play/pause
    )

    LottieAnimation(
        composition = composition.value,
        progress = progress,
        Modifier.padding(vertical = 150.dp).size(400.dp)

    )
}


/*
fun formatTemperatureUnitBasedOnLanguage(unit: String): String {
    val language = Locale.getDefault().language
    if (language == "ar") {
        return when (unit) {
            "°C" -> "°س"
            "°F" -> "°ف"
            "°K" -> "°ك"
            else -> "°س"
        }
    }
    return unit
}

fun getCountryNameFromCode(code: String): String? {
    return try {
        Locale("", code).displayCountry.takeIf { it.isNotBlank() }
    } catch (e: Exception) {
        null
    }
}*/
