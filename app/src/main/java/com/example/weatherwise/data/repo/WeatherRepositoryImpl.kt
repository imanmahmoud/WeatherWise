package com.example.weatherwise.data.repo

import com.example.weatherwise.data.local.LocalDataSource
import com.example.weatherwise.data.model.FavouriteLocation
import com.example.weatherwise.data.model.WeatherData
import com.example.weatherwise.data.model.currentWeather.CurrentWeatherResponse
import com.example.weatherwise.data.model.forecastWeather.ForecastWeatherResponse
import com.example.weatherwise.data.remote.RemoteDataSource
import com.example.weatherwizard.alert.model.AlertModel
import kotlinx.coroutines.flow.Flow

class WeatherRepositoryImpl private constructor(private val remoteDataSource: RemoteDataSource, private val localDataSource: LocalDataSource) :
    WeatherRepository {

    companion object {
        private var Instance: WeatherRepositoryImpl? = null
        fun getInstance(remoteDataSource: RemoteDataSource, localDataSource: LocalDataSource): WeatherRepository {
            return Instance ?: synchronized(this) {
                val instance = WeatherRepositoryImpl(remoteDataSource, localDataSource)
                Instance = instance
                instance
            }
        }

    }

    override suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
        apiKey: String,
        units: String,
        language: String
    ): Flow<CurrentWeatherResponse/*?*/> {
        return remoteDataSource.getCurrentWeather(latitude, longitude, apiKey, units, language)
    }

    override suspend fun getForecastWeather(
        latitude: Double,
        longitude: Double,
        apiKey: String,
        units: String,
        language: String
    ): Flow<ForecastWeatherResponse> {
        return remoteDataSource.getForecastWeather(latitude, longitude, apiKey, units, language)
    }

    override suspend fun getAllFavouriteLocations(): Flow<List<FavouriteLocation>> {
        return localDataSource.getAllFavouriteLocations()
    }

    override suspend fun insertFavouriteLocation(favouriteLocation: FavouriteLocation): Long {
      return localDataSource.insertFavouriteLocation(favouriteLocation)
    }

    override suspend fun deleteFavouriteLocation(favouriteLocation: FavouriteLocation): Int {
        return localDataSource.deleteFavouriteLocation(favouriteLocation)
    }

    override suspend fun getWeatherData(): Flow<WeatherData> {
        return localDataSource.getWeatherData()
    }

    override suspend fun insertWeatherData(weatherData: WeatherData):Long{
        return localDataSource.insertWeatherData(weatherData)
    }

    override suspend fun insertAlert(alert: AlertModel) {
        localDataSource.insertAlert(alert)
    }

    override suspend fun deleteAlert(alert: AlertModel) {
        localDataSource.deleteAlert(alert)
    }

    override suspend fun getAllAlerts(): Flow<List<AlertModel>> {
        return localDataSource.getAllAlerts()
    }


    /* override suspend fun getAllProducts(isOnline: Boolean) : Flow<List<Product>?> {
         if (isOnline) {
             return remoteDataSource.getAllProducts()
         }
         else {
             //return remoteDataSource.getAllProducts()
             return localDataSource.getAllProducts()
         }

     }

     override suspend fun insertProduct(product: Product): Long {
         return localDataSource.insertProduct(product)

     }
     override suspend fun deleteProduct(product: Product): Int {
         return localDataSource.deleteProduct(product)
     }*/


}