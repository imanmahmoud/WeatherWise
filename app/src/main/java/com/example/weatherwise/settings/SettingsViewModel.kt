package com.example.weatherwise.settings

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherwise.data.sharedPreference.PreferenceHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel(private val preferenceHelper: PreferenceHelper) : ViewModel() {

    private val _tempUnit = MutableStateFlow(preferenceHelper.getTempUnit())
    val tempUnit = _tempUnit.asStateFlow()

    private val _windSpeedUnit = MutableStateFlow(preferenceHelper.getWindSpeedUnit())
    val windSpeedUnit = _windSpeedUnit.asStateFlow()

    private val _language = MutableStateFlow(preferenceHelper.getLanguage())
    val language = _language.asStateFlow()

    private val _location = MutableStateFlow(preferenceHelper.getLocation())
    val location = _location.asStateFlow()

    fun updateTempUnit(tempUnit: String) {
        preferenceHelper.saveTempUnit(tempUnit)
        _tempUnit.value = tempUnit
        _windSpeedUnit.value = if (tempUnit == "imperial") "miles/hr" else "meter/sec"
    }


    fun updateLanguage(language: String, context: Context) {
        val languageToSave = if (language == "default") "" else language
        preferenceHelper.saveLanguage(languageToSave)
        _language.value = languageToSave

        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        Runtime.getRuntime().exit(0)
    }

    fun updateLocation(location: String) {
        preferenceHelper.saveLocation(location)
        _location.value = location
    }


    class SettingsFactory(private val preferenceHelper: PreferenceHelper) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SettingsViewModel(preferenceHelper) as T
        }
    }
}
