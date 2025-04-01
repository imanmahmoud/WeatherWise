package com.example.weatherwise.home.viewModel


import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherwise.data.model.WeatherData
import com.example.weatherwise.data.model.currentWeather.CurrentWeatherResponse
import com.example.weatherwise.data.repo.ResultState
import com.example.weatherwise.data.repo.WeatherRepository
import com.example.weatherwise.utils.ApiConstants
import com.example.weatherwise.utils.DateTimeHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.UnknownHostException


class HomeViewModel(private val repo: WeatherRepository) : ViewModel() {

    private val _currentWeatherState =
        MutableStateFlow<ResultState<CurrentWeatherResponse>>(ResultState.Loading())
    val currentWeatherState = _currentWeatherState.asStateFlow()

    private val _hourlyForecastState =
        MutableStateFlow<ResultState<List<CurrentWeatherResponse>>>(ResultState.Loading())
    val hourlyForecastState = _hourlyForecastState.asStateFlow()

    private val _dailyForecastState =
        MutableStateFlow<ResultState<List<CurrentWeatherResponse>>>(ResultState.Loading())
    val dailyForecastState = _dailyForecastState.asStateFlow()

    /*private val _message = MutableSharedFlow<String>()
    val message = _message.asSharedFlow()*/

    private var lastFetchedLocation: Location? = null
    private var lastFetchedWeatherData: WeatherData? = null
    private var lastUnit: String? = null
    private var lastLanguage: String? = null
    //  private lateinit var lastFetchedWeatherData: WeatherData


    fun fetchWeatherIfLocationChanged(
        newLocation: Location,
        isFromFavourite: Boolean,
        unit: String,
        language: String
    ) {
        if (lastFetchedLocation == null ||
            newLocation.latitude != lastFetchedLocation?.latitude ||
            newLocation.longitude != lastFetchedLocation?.longitude  ||
            lastUnit != unit ||
            lastLanguage != language
        ) {
            lastFetchedLocation = newLocation
            fetchCurrentWeather(
               lat =  newLocation.latitude,
                lon = newLocation.longitude,
                apiKey = ApiConstants.WEATHER_API_KEY,
                isFromFavourite = isFromFavourite,
               unit = unit,
                language = language
            )
            fetchWeatherForecast(
               lat = newLocation.latitude,
                lon = newLocation.longitude,
                apiKey = ApiConstants.WEATHER_API_KEY,
                isFromFavourite = isFromFavourite,
                unit = unit,
                language = language
            )
           /* if (!isFromFavourite && currentWeatherState.value is ResultState.Success && hourlyForecastState.value is ResultState.Success && dailyForecastState.value is ResultState.Success) {
                lastFetchedWeatherData = WeatherData(
                    1,
                    (currentWeatherState.value as ResultState.Success).data,
                    (hourlyForecastState.value as ResultState.Success).data,
                    (dailyForecastState.value as ResultState.Success).data
                )

                insertWeatherData()
            }*/
        }
    }


    fun fetchCurrentWeather(
        lat: Double,
        lon: Double,
        apiKey: String,
        isFromFavourite: Boolean,
        unit: String,
        language: String
    ) {
        viewModelScope.launch {
            Log.i("TAG", "fetchWeather: before tryyyyyy")
            try {
                // Log.i("TAG", "fetchWeather: after calling repo")
                repo.getCurrentWeather(lat, lon, apiKey, units = unit, language = language)
                    .catch { e ->
                        val errorMessage = when (e) {
                            is UnknownHostException -> "No internet connection. Please check your network."
                            is HttpException -> "Server error: ${e.code()}. Please try again later."
                            else -> "Unexpected error: ${e.localizedMessage}"
                        }
                        Log.i("TAG", "fetchWeather: errorrrrrrrr: $errorMessage")
                        if (isFromFavourite) {
                            _currentWeatherState.value = ResultState.Failure(errorMessage)
                        } else {
                            getCachedWeatherData()
                        }
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
                            DateTimeHelper.formatUnixTimestampToDate(weatherData/*!!*/.dt.toLong())
                        val currentWeather = weatherData.copy(formattedDt = formattedDate)
                        _currentWeatherState.value = ResultState.Success(currentWeather)
                         if (!isFromFavourite) {
                             Log.i("TAG", "fetchCurrentWeather: not from fav")
                             lastFetchedWeatherData = WeatherData(
                                 currentWeatherResponse = currentWeather,
                                 hourlyForecast = emptyList(),
                                 dailyForecast = emptyList()
                             )
                             //lastFetchedWeatherData.currentWeatherResponse = currentWeather
                         }
                    }
            } catch (e: Exception) {
                Log.i("TAG", "fetchWeather: catch errroooor")
                _currentWeatherState.value =
                    ResultState.Failure("Unexpected error: ${e.localizedMessage}")
            }
        }
    }

    fun fetchWeatherForecast(
        lat: Double,
        lon: Double,
        apiKey: String,
        isFromFavourite: Boolean,
        unit: String,
        language: String
    ) {
        viewModelScope.launch {
            Log.i("TAG", "fetchWeatherForecast: before try")
            Log.i("TAG", "Unitsssssss:$unit ")
            repo.getForecastWeather(lat, lon, apiKey, units = unit, language = language)

                .catch { e ->
                    Log.e("TAG", "Error fetching forecast: ${e.localizedMessage}")

                    val errorMessage = when (e) {
                        is UnknownHostException -> "No internet connection. Please check your network."
                        is HttpException -> "Server error: ${e.code()}. Please try again later."
                        else -> "Unexpected error: ${e.localizedMessage}"
                    }
                    if (isFromFavourite) {
                        _dailyForecastState.value = ResultState.Failure(errorMessage)
                        _hourlyForecastState.value = ResultState.Failure(errorMessage)
                    }
                }
                .collect { forecastData ->
                    Log.i("TAG", "fetchForecastData: $forecastData")

                    val hourlyData = forecastData.list
                        .take(8)
                        .map { item ->
                            val formattedHour =
                                DateTimeHelper.formatUnixTimestampToHour(item.dt.toLong())

                            val hourlyDataItem = item.copy(
                                formattedDt = formattedHour,
                                name = forecastData.city.name,
                                coord = forecastData.city.coord
                            )
                            hourlyDataItem
                        }

                    _hourlyForecastState.value = ResultState.Success(hourlyData)


                    Log.i("TAG", "fetchHourlyWeather: $hourlyData")

                    val dailyData = forecastData.list
                        .groupBy { DateTimeHelper.formatUnixTimestampToDay(it.dt.toLong()) }
                        .map { (day, items) ->
                            val firstItem = items.first()
                            firstItem.copy(
                                formattedDt = day,
                                name = forecastData.city.name,
                                coord = forecastData.city.coord
                            )
                        }

                    _dailyForecastState.value = ResultState.Success(dailyData)
                    Log.i("TAG", "fetchDailyWeather: $dailyData")

                     if (!isFromFavourite) {
                         Log.i("TAG", "fetchWeatherForecast: not from fav2")
                         lastFetchedWeatherData?.hourlyForecast = hourlyData
                         lastFetchedWeatherData?.dailyForecast = hourlyData
                         insertWeatherData()
                     }
                }
        }
    }

    fun insertWeatherData() {
        viewModelScope.launch {
            try {
                Log.i("TAG", "insertWeatherData: before insert")
                val result = lastFetchedWeatherData?.let {
                    Log.i("TAG", "insertWeatherData: not null")
                    repo.insertWeatherData(it)
                }
                if (result != null) {
                    if (result > 0) {
                        Log.i("TAG", "insertWeatherData: insert weather data success")
                    } else {
                        Log.i("TAG", "insertWeatherData: insert weather data failed")
                    }
                }
            } catch (ex: Exception) {
                Log.i("TAG", "insertWeatherData: catch error")
            }
        }
    }

    fun getCachedWeatherData() {
        viewModelScope.launch {
            repo.getWeatherData()
                .catch { e ->
                    Log.e("TAG", "Error fetching cached weather data: ${e.localizedMessage}")
                }
                .collect { weatherData ->
                    _currentWeatherState.value =
                        ResultState.Success(weatherData.currentWeatherResponse)
                    _hourlyForecastState.value = ResultState.Success(weatherData.hourlyForecast)
                    _dailyForecastState.value = ResultState.Success(weatherData.dailyForecast)

                    Log.i("TAG", "fetchCachedWeather: $weatherData")
                }
        }
    }

    class HomeFactory(private val repo: WeatherRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(repo) as T
        }
    }
}
