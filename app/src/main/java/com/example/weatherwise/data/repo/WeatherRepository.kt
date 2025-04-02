
package com.example.weatherwise.data.repo

import com.example.weatherwise.data.model.FavouriteLocation
import com.example.weatherwise.data.model.WeatherData
import com.example.weatherwise.data.model.currentWeather.CurrentWeatherResponse
import com.example.weatherwise.data.model.forecastWeather.ForecastWeatherResponse
import com.example.weatherwizard.alert.model.AlertModel
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    suspend fun getCurrentWeather(latitude: Double, longitude: Double, apiKey: String, units: String = "metric", language: String = "en"): Flow<CurrentWeatherResponse/*?*/>
    suspend fun getForecastWeather(latitude: Double, longitude: Double, apiKey: String, units: String = "metric", language: String = "en"): Flow<ForecastWeatherResponse>

    suspend fun getAllFavouriteLocations(): Flow<List<FavouriteLocation>>
    suspend fun insertFavouriteLocation(favouriteLocation: FavouriteLocation): Long
    suspend fun deleteFavouriteLocation(favouriteLocation: FavouriteLocation): Int

    suspend fun getWeatherData(): Flow<WeatherData>
    suspend fun insertWeatherData(weatherData: WeatherData):Long

    suspend fun insertAlert(alert: AlertModel)
    suspend fun deleteAlert(alert: AlertModel)
    suspend fun getAllAlerts(): Flow<List<AlertModel>>

   /* suspend fun getAllProducts(isOnline: Boolean): Flow<List<Product>?>
    suspend fun insertProduct(product: Product): Long
    suspend fun deleteProduct(product: Product): Int*/


}