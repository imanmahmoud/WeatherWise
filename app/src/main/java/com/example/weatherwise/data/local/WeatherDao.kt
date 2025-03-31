package com.example.weatherwise.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherwise.data.model.FavouriteLocation
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Query("SELECT * FROM FAVOURITE_LOCATIONS")
            /*suspend*/ fun getAllFavouriteLocations(): Flow<List<FavouriteLocation>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavouriteLocation(favouriteLocation: FavouriteLocation): Long

    @Delete
    suspend fun deleteFavouriteLocation(favouriteLocation: FavouriteLocation): Int


}


/*
@Dao
interface WeatherDao {
   */
/* @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProduct(product: Product): Long
    @Delete
    suspend fun deleteProduct(product: Product): Int
   @Query("SELECT * FROM products")
    *//*
*/
/*suspend*//*
*/
/* fun getAllProducts(): Flow<List<Product>>*//*



}*/
