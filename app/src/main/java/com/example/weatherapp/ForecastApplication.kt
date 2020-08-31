package com.example.weatherapp

import android.app.Application
import android.content.Context
import com.example.weatherapp.data.db.ForecastDB
import com.example.weatherapp.data.network.*
import com.example.weatherapp.data.provider.LocationProvider
import com.example.weatherapp.data.provider.LocationProviderImpl
import com.example.weatherapp.data.repo.ForecastRepo
import com.example.weatherapp.data.repo.ForecastRepoImpl
import com.example.weatherapp.ui.weather.current.CurrentViewModelFactory
import com.google.android.gms.location.LocationServices
import com.jakewharton.threetenabp.AndroidThreeTen
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class ForecastApplication: Application(),KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@ForecastApplication))
        bind() from singleton { ForecastDB(instance()) }
        bind() from singleton { instance<ForecastDB>().currentWeatherDao()}
        bind() from singleton { instance<ForecastDB>().weatherLocationDao()}
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance())}
        bind() from singleton { WeatherStackAPIService(instance()) }
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance())}
        bind() from  provider { LocationServices.getFusedLocationProviderClient(instance<Context>())}
        bind<LocationProvider>() with singleton{LocationProviderImpl(instance(),instance())}
        bind<ForecastRepo>() with singleton { ForecastRepoImpl(instance(),instance(),instance(),instance())}
        bind() from  provider { CurrentViewModelFactory(instance())}

    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}