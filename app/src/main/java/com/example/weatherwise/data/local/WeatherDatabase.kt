package com.example.weatherwise.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherwise.data.model.Converters
import com.example.weatherwise.data.model.FavouriteLocation


@Database(entities = [ FavouriteLocation::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class WeatherDatabase: RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
    companion object {
        @Volatile
        private var INSTANCE: WeatherDatabase?= null
        fun getInstance(context: Context):WeatherDatabase{
            return INSTANCE?: synchronized(this){
                val instance: WeatherDatabase= Room.databaseBuilder(context, WeatherDatabase::class.java, "weather_db").build()
                INSTANCE=instance
                instance
            }
        }
    }

    }

/*//@Database(entities = [Product::class], version = 1)
abstract class WeatherDatabase: RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
    companion object {
        @Volatile
        private var INSTANCE: WeatherDatabase?= null
        fun getInstance(context: Context):WeatherDatabase{
            return INSTANCE?: synchronized(this){
                val instance: WeatherDatabase= Room.databaseBuilder(context, WeatherDatabase::class.java, "weather_db").build()
                INSTANCE=instance
                instance
            }
        }
    }

    }*/
