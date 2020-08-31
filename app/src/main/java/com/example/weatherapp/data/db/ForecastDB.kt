package com.example.weatherapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherapp.data.db.entity.Converters
import com.example.weatherapp.data.db.entity.CurrentWeather
import com.example.weatherapp.data.db.entity.WeatherLocation

@Database(
    entities = [CurrentWeather::class,WeatherLocation::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ForecastDB :RoomDatabase(){

    abstract fun weatherLocationDao():WeatherLocationDao

    abstract fun currentWeatherDao():CurrentWeatherDao
    companion object{ // since db has to be singleton
       @Volatile private var instance:ForecastDB?=null
        private val LOCK= Any()  // for synchronous calls on threads
        operator fun invoke(context:Context)= instance?: synchronized(LOCK){
            instance?: buildDB(context).also { instance=it }
        }

        private fun buildDB(context:Context)=
            Room.databaseBuilder(context.applicationContext,
            ForecastDB::class.java,"forecast.db")
                .build()
    }
}