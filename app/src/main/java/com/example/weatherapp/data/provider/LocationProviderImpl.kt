package com.example.weatherapp.data.provider

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.example.weatherapp.data.db.entity.WeatherLocation

const val CUSTOM_LOCATION= "CUSTOM _LOCATION"
class LocationProviderImpl(/*context:Context*/) : LocationProvider {
   // private val appContext= context.applicationContext

    //private val preferences:SharedPreferences
   // get() = PreferenceManager.getDefaultSharedPreferences(appContext)

    override suspend fun hasLocationChanged(lastSavedLocation: WeatherLocation): Boolean {
        return true
    }

    override suspend fun getPreferredLocation(): String {
       return "Chicago"
    }
}