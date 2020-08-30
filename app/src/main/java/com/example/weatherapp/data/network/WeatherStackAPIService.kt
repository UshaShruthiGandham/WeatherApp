package com.example.weatherapp.data.network

import android.util.Log
import com.example.weatherapp.data.network.response.CurrentWeatherResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val API_KEY ="f1de759e60e1ce8dbf318898fbb1ff78"

//http://api.weatherstack.com/current?access_key=f1de759e60e1ce8dbf318898fbb1ff78&query=New%20York
interface WeatherStackAPIService {

    @GET("current")
    fun getTodaysWeather(
        @Query("query") location: String,
    ): Deferred<CurrentWeatherResponse>  // coroutines and await the response

    companion object{
        operator fun invoke(
            connectivityInterceptor:ConnectivityInterceptor
        ): WeatherStackAPIService {
            val reqInterceptor= Interceptor{chain ->   // Interceptor adds the api key to API
              val url= chain.request()
                  .url()
                  .newBuilder()
                  .addQueryParameter("access_key", API_KEY)
                  .build()
                val request=chain.request()
                    .newBuilder()
                    .url(url)
                    .build()
                Log.i("URL::::",url.toString())
                return@Interceptor chain.proceed(request)
            }

            val okhttpClient= OkHttpClient.Builder()
                .addInterceptor(reqInterceptor)
                .addInterceptor(connectivityInterceptor)
                .build()


            return Retrofit.Builder()
                .client(okhttpClient)
                .baseUrl("http://api.weatherstack.com/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherStackAPIService::class.java)
        }
    }
}

