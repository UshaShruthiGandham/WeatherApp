package com.example.weatherapp.ui.weather.current

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.R
import com.example.weatherapp.internal.glide.GlideApp
import com.example.weatherapp.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.current_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class CurrentFragment : ScopedFragment(),KodeinAware {

    override val kodein: Kodein by closestKodein() // inside forecast application class

    private val viewModelFactory:CurrentViewModelFactory by instance()

    private lateinit var viewModel: CurrentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this,viewModelFactory).get(CurrentViewModel::class.java)

        bindUI()
        /*val apiService = WeatherStackAPIService(ConnectivityInterceptorImpl(this.requireContext()))
        val weatherNtwkDataSource = WeatherNetworkDataSourceImpl(apiService)
        weatherNtwkDataSource.downloadedCurrentWeather.observe(viewLifecycleOwner, Observer {
            textView.text=it.toString()
        })
        GlobalScope.launch(Dispatchers.Main ){
            weatherNtwkDataSource.fetchCurrentWeather("Canada")
            //val todayWeatherResponse = apiService.getTodaysWeather("Canada").await()
        }*/
    }

    private fun bindUI()= launch{
        val currentWeather = viewModel.weather.await()
        val weatherLocation = viewModel.weatherLocation.await()

        weatherLocation.observe(viewLifecycleOwner, Observer { location->
            if(location==null) return@Observer
            updateLocation(location.name)
        })

        currentWeather.observe(viewLifecycleOwner, Observer {
            if (it==null) return@Observer

            loadingGroup.visibility=View.GONE
            //updateLocation("Chicago")
            updateTodaysDate()
            updateTemps(it.temperature,it.feelslike)
            updateCondition(it.weatherDescriptions[0])
            updateWind(it.windDegree,it.windDir)
            updatePrecipitation(it.precip)
            updateVisibility(it.visibility)

            GlideApp.with(this@CurrentFragment)
                .load(it.weatherIcons[0])
                .into(weatherIcon)
        })
    }

    private fun updateLocation(location:String){
        (activity as? AppCompatActivity)?.supportActionBar?.title=location
    }
    private fun updateTodaysDate(){
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle="Today"
    }
    private fun updateTemps(temp:Double,feelsLike:Double){
        temperatureTv.text="$temp°C"
        feelsTempTv.text="Feels like $feelsLike°C"

    }
    private fun updateCondition(condition:String){
        conditionTv.text="$condition"
    }
    private fun updateWind(windDegree:Double,windDir:String){
        windTV.text="Wind: $windDir, $windDegree MPH"
    }

    private fun updatePrecipitation(precipitation:Double){
        precipitationTv.text="Precipitation: $precipitation mm"
    }
    private fun updateVisibility(visibility:Double){
        visibilityTv.text="Visibility: $visibility km"
    }
}