package com.example.weatherwise.home.viewModel


import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherwise.data.model.currentWeather.CurrentWeatherResponse
import com.example.weatherwise.data.repo.ResultState
import com.example.weatherwise.data.repo.WeatherRepository
import com.example.weatherwise.utils.ApiConstants
import com.example.weatherwise.utils.DateTimeUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.UnknownHostException

const val apiKey = "5a46ee8289123314e05b723b9e36d002"

class HomeViewModel(private val repo: WeatherRepository) : ViewModel() {

    private val _currentWeatherState = MutableStateFlow<ResultState<CurrentWeatherResponse>>(ResultState.Loading())
    val currentWeatherState = _currentWeatherState.asStateFlow()

    private val _hourlyForecastState = MutableStateFlow<ResultState<List<CurrentWeatherResponse>>>(ResultState.Loading())
    val hourlyForecastState = _hourlyForecastState.asStateFlow()

    private val _dailyForecastState = MutableStateFlow<ResultState<List<CurrentWeatherResponse>>>(ResultState.Loading())
    val dailyForecastState = _dailyForecastState.asStateFlow()

    /*private val _message = MutableSharedFlow<String>()
    val message = _message.asSharedFlow()*/

    private var lastFetchedLocation: Location? = null

    fun fetchWeatherIfLocationChanged(newLocation: Location) {
        if (lastFetchedLocation == null ||
            newLocation.latitude != lastFetchedLocation?.latitude ||
            newLocation.longitude != lastFetchedLocation?.longitude
        ) {
            lastFetchedLocation = newLocation
            fetchCurrentWeather(newLocation.latitude, newLocation.longitude, ApiConstants.WEATHER_API_KEY)
            fetchWeatherForecast(newLocation.latitude, newLocation.longitude, ApiConstants.WEATHER_API_KEY)
        }
    }



    fun fetchCurrentWeather(lat: Double, lon: Double, apiKey: String) {
        viewModelScope.launch {
            Log.i("TAG", "fetchWeather: before tryyyyyy")
                try {
                   // Log.i("TAG", "fetchWeather: after calling repo")
                    repo.getCurrentWeather(lat, lon, apiKey)

                    .catch { e ->

                        val errorMessage = when (e) {
                            is UnknownHostException -> "No internet connection. Please check your network."
                            is HttpException -> "Server error: ${e.code()}. Please try again later."
                            else -> "Unexpected error: ${e.localizedMessage}"
                        }
                        Log.i("TAG", "fetchWeather: errorrrrrrrr: $errorMessage")
                        _currentWeatherState.value = ResultState.Failure(errorMessage)
                       /*_message.emit(errorMessage)*/
                    }
                    .collect { weatherData ->
                            Log.i("TAG", "fetchWeather: $weatherData")

                      /*  weatherData?.let {
                            val formattedDate = DateTimeUtils.formatUnixTimestampToDate(it.dt.toLong())
                            val currentWeather = it.copy(formattedDt = formattedDate)
                            _currentWeatherState.value = Response.Success(currentWeather)
                        } ?: run {
                            _currentWeatherState.value = Response.Failure("Received null weather data")
                        }*/


                            val formattedDate =
                                DateTimeUtils.formatUnixTimestampToDate(weatherData/*!!*/.dt.toLong())
                          val currentWeather =  weatherData.copy(formattedDt = formattedDate)
                            _currentWeatherState.value = ResultState.Success(currentWeather)
                    }
                }catch (e:Exception){
                    Log.i("TAG", "fetchWeather: catch errroooor")
                    _currentWeatherState.value = ResultState.Failure("Unexpected error: ${e.localizedMessage}")
                  /*  _message.emit("Unexpected error: ${e.localizedMessage}")*/
                }
        }
    }

    fun fetchWeatherForecast(lat: Double, lon: Double, apiKey: String) {
        viewModelScope.launch {
            Log.i("TAG", "fetchWeatherForecast: before try")
            repo.getForecastWeather(lat, lon, apiKey)
                .catch { e ->
                    Log.e("TAG", "Error fetching forecast: ${e.localizedMessage}")

                    val errorMessage = when (e) {
                        is UnknownHostException -> "No internet connection. Please check your network."
                        is HttpException -> "Server error: ${e.code()}. Please try again later."
                        else -> "Unexpected error: ${e.localizedMessage}"
                    }
                    _dailyForecastState.value = ResultState.Failure(errorMessage)
                    _hourlyForecastState.value = ResultState.Failure(errorMessage)
                }
                .collect { forecastData ->
                    val hourlyData = forecastData.list
                        .take(8)
                        .map { item ->
                            val formattedHour = DateTimeUtils.formatUnixTimestampToHour(item.dt.toLong())

                        val hourlyDataItem = item.copy(formattedDt = formattedHour,name = forecastData.city.name, coord = forecastData.city.coord)
                            hourlyDataItem
                        }

                    _hourlyForecastState.value = ResultState.Success(hourlyData)
                    Log.i("TAG", "fetchWeather: $hourlyData")

                    val dailyData = forecastData.list
                        .groupBy { DateTimeUtils.formatUnixTimestampToDay(it.dt.toLong()) }
                        .map { (day, items) ->
                            val firstItem = items.first()
                             firstItem.copy(formattedDt = day,name = forecastData.city.name, coord = forecastData.city.coord)
                        }

                    _dailyForecastState.value = ResultState.Success(dailyData)
                    Log.i("TAG", "fetchWeather: $dailyData")
                }
        }
    }



    class HomeFactory(private val repo: WeatherRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(repo) as T
        }
    }
}
