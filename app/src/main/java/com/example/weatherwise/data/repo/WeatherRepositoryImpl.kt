
package com.example.weatherwise.data.repo
import com.example.weatherwise.data.model.currentWeather.CurrentWeatherResponse
import com.example.weatherwise.data.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow

class WeatherRepositoryImpl private constructor(private val remoteDataSource: RemoteDataSource/*, private val localDataSource: LocalDataSource*/): WeatherRepository {

    companion object {
        private var Instance: WeatherRepositoryImpl? = null
        fun getInstance(remoteDataSource: RemoteDataSource/*, localDataSource: LocalDataSource*/): WeatherRepository {
            return Instance ?: synchronized(this) {
                val instance = WeatherRepositoryImpl(remoteDataSource/*, localDataSource*/)
                Instance = instance
                instance
            }
        }

    }

    override suspend fun getCurrentWeather(latitude: Double, longitude: Double, apiKey: String): Flow<CurrentWeatherResponse?> {
        return remoteDataSource.getCurrentWeather(latitude, longitude, apiKey)
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