package com.example.weatherapp.data.db.entity


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName

const val TODAY_WEATHER_ID=0;
@Entity(tableName = "current_weather")
data class CurrentWeather(
    val feelslike: Double,
    @SerializedName("is_day")
    val isDay: String,
    @SerializedName("observation_time")
    val observationTime: String,
    val precip: Double,
    val temperature: Double,
    @SerializedName("uv_index")
    val uvIndex: Double,
    val visibility: Double,
    @SerializedName("weather_code")
    val weatherCode: Int,
    @SerializedName("weather_descriptions")
    val weatherDescriptions: List<String>,
    @SerializedName("weather_icons")
    val weatherIcons: List<String>,
    @SerializedName("wind_degree")
    val windDegree: Double,
    @SerializedName("wind_dir")
    val windDir: String,
    @SerializedName("wind_speed")
    val windSpeed: Double,
    @PrimaryKey(autoGenerate = false)
    val id:Int =TODAY_WEATHER_ID
)