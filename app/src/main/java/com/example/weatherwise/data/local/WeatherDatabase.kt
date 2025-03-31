package com.example.weatherwise.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
//import androidx.room.TypeConverters
//import com.example.weatherwise.data.model.Converters
import com.example.weatherwise.data.model.FavouriteLocation
import com.example.weatherwise.data.model.WeatherData


@Database(entities = [ FavouriteLocation::class , WeatherData::class ], version = 2/*, exportSchema = false*/)
@TypeConverters(WeatherDataConverters::class)
abstract class WeatherDatabase: RoomDatabase() {




    abstract fun weatherDao(): WeatherDao
    companion object {
        @Volatile
        private var INSTANCE: WeatherDatabase?= null
        fun getInstance(context: Context):WeatherDatabase{
            return INSTANCE?: synchronized(this){
                val instance: WeatherDatabase= Room.databaseBuilder(context, WeatherDatabase::class.java, "weather_db") .fallbackToDestructiveMigration().build()
                INSTANCE=instance
                instance
            }
        }
    }

    }


val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Example: If you added a new column to a table
        database.execSQL("ALTER TABLE favorite_locations ADD COLUMN new_column_name TEXT DEFAULT '' NOT NULL")
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
