package com.example.weatherapp.data.repo

import androidx.lifecycle.LiveData
import com.example.weatherapp.data.network.response.CurrentWeatherResponse
import com.example.weatherapp.data.db.CurrentWeatherDao
import com.example.weatherapp.data.db.WeatherLocationDao
import com.example.weatherapp.data.db.entity.CurrentWeather
import com.example.weatherapp.data.db.entity.WeatherLocation
import com.example.weatherapp.data.network.WeatherNetworkDataSource
import com.example.weatherapp.data.provider.LocationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime

class ForecastRepoImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val weatherLocationDao: WeatherLocationDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource,
    private val locationProvider: LocationProvider
) : ForecastRepo {
    init {
        weatherNetworkDataSource.downloadedCurrentWeather.observeForever { newCurrentWeather ->
            // persist
            persistFetchedCurrentWeather(newCurrentWeather)
        }
    }

    override suspend fun getCurrentWeather(): LiveData<CurrentWeather> {
        return withContext(Dispatchers.IO) {
            initWeatherData()
            return@withContext currentWeatherDao.getWeatherValues()
        } // withContext coroutine returns a value, launch returns a job
    }

    override suspend fun getWeatherLocation(): LiveData<WeatherLocation> {
        return withContext(Dispatchers.IO) {
            return@withContext weatherLocationDao.getLocation()
        }
    }

    //persist location and weather into DB
    private fun persistFetchedCurrentWeather(fetchedWeather: CurrentWeatherResponse) {
        //dispather can dispatch many threads at once and then destroy then
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDao.upsert(fetchedWeather.currentWeather)
            weatherLocationDao.upsert(fetchedWeather.location)
        } //coroutine global scope can be used when the underlying class has no lifecycle
    }

    private suspend fun initWeatherData() {
        val lastLocation = weatherLocationDao.getLocation().value
        if (lastLocation == null || locationProvider.hasLocationChanged(lastLocation)) {
            fetchCurrentWeather() // when the db doesnt have the value
            return
        }
        if (isFetchCurrentNeeded(lastLocation.zonedDateTime))
            fetchCurrentWeather()
    }

    private suspend fun fetchCurrentWeather() {
        weatherNetworkDataSource.fetchCurrentWeather(locationProvider.getPreferredLocation())
    }

    private fun isFetchCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val thirtyMinAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirtyMinAgo)
    }
}