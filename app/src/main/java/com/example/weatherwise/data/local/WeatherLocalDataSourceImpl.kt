package com.example.weatherwise.data.local


import com.example.weatherwise.data.model.FavouriteLocation
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


}
