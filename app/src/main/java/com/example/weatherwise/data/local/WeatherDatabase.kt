package com.example.weatherwise.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase


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
