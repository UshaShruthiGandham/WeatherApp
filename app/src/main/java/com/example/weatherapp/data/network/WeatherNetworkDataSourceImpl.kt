package com.example.weatherapp.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.data.network.response.CurrentWeatherResponse
import com.example.weatherapp.internal.NoConnectivityExceptions

class WeatherNetworkDataSourceImpl (
    private val  apiService: WeatherStackAPIService
): WeatherNetworkDataSource {
    private val downloadedCurrentDataMutable= MutableLiveData<CurrentWeatherResponse>()
    override val downloadedCurrentWeather: LiveData<CurrentWeatherResponse> // live data cannot be changed to change we need muttablelivedata
        get() = downloadedCurrentDataMutable // casts mutable live data to be livedata so the client code requesting downloaded data cannot change the value

    override suspend fun fetchCurrentWeather(location: String) {

        try {
            val fetchCurrent= apiService
                .getTodaysWeatherAsync(location)
                .await()
            downloadedCurrentDataMutable.postValue(fetchCurrent)

        } catch (e:NoConnectivityExceptions){
            Log.e("connectivity","No Internet Connection.",e)
        }
    }
}