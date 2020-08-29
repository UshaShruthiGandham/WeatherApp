package com.example.weatherapp.data.network

import androidx.lifecycle.LiveData
import com.example.weatherapp.data.network.response.CurrentWeatherResponse

interface WeatherNetworkDataSource {
    val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>

    // only update downloaded data and will observe for the changes happens in repo class asynchronously
    suspend fun fetchCurrentWeather(location:String)

}