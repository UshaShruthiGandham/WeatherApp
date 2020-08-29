package com.example.weatherapp.data.provider

import com.example.weatherapp.data.db.entity.WeatherLocation

interface LocationProvider {
    suspend fun hasLocationChanged(lastSavedLocation:WeatherLocation):Boolean
    suspend fun getPreferredLocation():String
}