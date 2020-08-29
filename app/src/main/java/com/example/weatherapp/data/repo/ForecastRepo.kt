package com.example.weatherapp.data.repo

import androidx.lifecycle.LiveData
import com.example.weatherapp.data.db.entity.CurrentWeather
import com.example.weatherapp.data.db.entity.WeatherLocation

interface ForecastRepo {
    suspend fun getCurrentWeather() :LiveData<CurrentWeather>// suspend is coroutine
    suspend fun getWeatherLocation() :LiveData<WeatherLocation>// suspend is coroutine
}