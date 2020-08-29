package com.example.weatherapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.data.db.entity.CurrentWeather
import com.example.weatherapp.data.db.entity.TODAY_WEATHER_ID

@Dao
interface CurrentWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) // replace because we have same ID
    fun upsert(currentWeather: CurrentWeather)
    @Query("select * from current_weather where id=$TODAY_WEATHER_ID")
    fun getWeatherValues(): LiveData<CurrentWeather>
}