package com.example.weatherwise.home


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherwise.data.model.currentWeather.CurrentWeatherResponse
import com.example.weatherwise.data.repo.Response
import com.example.weatherwise.data.repo.WeatherRepository
import com.example.weatherwise.utils.DateTimeUtils
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(private val repo: WeatherRepository) : ViewModel() {

    private val _weatherState = MutableStateFlow<Response<CurrentWeatherResponse>>(Response.Loading())
    val weatherState = _weatherState.asStateFlow()

    private val _message = MutableSharedFlow<String>()
    val message = _message.asSharedFlow()



    fun fetchWeather(lat: Double, lon: Double, apiKey: String) {
        viewModelScope.launch {
            Log.i("TAG", "fetchWeather: before tryyyyyy")
                try {
                    Log.i("TAG", "fetchWeather: after calling repo")
                    repo.getCurrentWeather(lat, lon, apiKey)

                    .catch { e -> // Handle errors here
                        Log.i("TAG", "fetchWeather: errorrrrrrrr")
                        _weatherState.value = Response.Error(e.message ?: "Unknown error")
                        _message.emit(e.message ?: "Unknown error")
                    }
                    .collect { weatherData ->
                            Log.i("TAG", "fetchWeather: $weatherData")
                            val formattedDate =
                                DateTimeUtils.formatUnixTimestampToDate(weatherData!!.dt.toLong())
                          val currentWeather =  weatherData.copy(formattedDt = formattedDate)
                            _weatherState.value = Response.Success(currentWeather)
                    }
                }catch (e:Exception){
                    Log.i("TAG", "fetchWeather: catch errroooor")
                    _weatherState.value = Response.Error(e.message ?: "Unknown error")
                    _message.emit(e.message ?: "Unknown error")
                }
        }
    }

    class HomeFactory(private val repo: WeatherRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(repo) as T
        }
    }
}
