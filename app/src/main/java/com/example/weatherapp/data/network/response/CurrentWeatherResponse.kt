package com.example.weatherapp.data.network.response

import com.example.weatherapp.data.db.entity.CurrentWeather
import com.example.weatherapp.data.db.entity.WeatherLocation
import com.google.gson.annotations.SerializedName

data class CurrentWeatherResponse(
    @SerializedName("current")
    val currentWeather: CurrentWeather,
    val location: WeatherLocation
)