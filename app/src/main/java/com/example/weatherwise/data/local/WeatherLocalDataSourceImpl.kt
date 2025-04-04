package com.example.weatherwise.data.local


import com.example.weatherwise.data.model.FavouriteLocation
import com.example.weatherwise.data.model.WeatherData
import com.example.weatherwizard.alert.model.AlertModel
import kotlinx.coroutines.flow.Flow

class WeatherLocalDataSourceImpl(private val weatherDao: WeatherDao) : LocalDataSource {

    override suspend fun getAllFavouriteLocations(): Flow<List<FavouriteLocation>> {
        return weatherDao.getAllFavouriteLocations()
    }

    override suspend fun insertFavouriteLocation(favouriteLocation: FavouriteLocation): Long {
        return weatherDao.insertFavouriteLocation(favouriteLocation)
    }

    override suspend fun deleteFavouriteLocation(favouriteLocation: FavouriteLocation): Int {
        return weatherDao.deleteFavouriteLocation(favouriteLocation)
    }

    override suspend fun getWeatherData(lat: Double, lon: Double): Flow<WeatherData> {
        return weatherDao.getWeatherData(lat, lon)
    }


    override suspend fun insertWeatherData(weatherData: WeatherData): Long {
        return weatherDao.insertWeatherData(weatherData)

    }

    override suspend fun insertAlert(alert: AlertModel) {
        weatherDao.insertAlert(alert)
    }

    override suspend fun deleteAlert(alert: AlertModel) {
        weatherDao.deleteAlert(alert)
    }

    override suspend fun getAllAlerts(): Flow<List<AlertModel>> {
        return weatherDao.getAllAlerts()
    }


}
