
package com.example.weatherwise.data.repo

import com.example.weatherwise.data.model.currentWeather.CurrentWeatherResponse
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    suspend fun getCurrentWeather(latitude: Double, longitude: Double, apiKey: String): Flow<CurrentWeatherResponse?>
   /* suspend fun getAllProducts(isOnline: Boolean): Flow<List<Product>?>
    suspend fun insertProduct(product: Product): Long
    suspend fun deleteProduct(product: Product): Int*/


}