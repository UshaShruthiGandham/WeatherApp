package com.example.weatherapp.ui.weather.current

import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.repo.ForecastRepo
import com.example.weatherapp.internal.lazyDeferred

class CurrentViewModel (
    private val forecastRepo: ForecastRepo
): ViewModel() {  // VM doesn't know about view it just exposes data , view model preserves the state of the phone.

    val weather by lazyDeferred {
        forecastRepo.getCurrentWeather()
    }

    val weatherLocation by lazyDeferred {
        forecastRepo.getWeatherLocation()
    }

}