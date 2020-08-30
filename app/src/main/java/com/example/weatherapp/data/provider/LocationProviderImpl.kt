package com.example.weatherapp.data.provider

import android.content.Context
import android.location.Location
import androidx.core.content.ContextCompat
import com.example.weatherapp.data.db.entity.WeatherLocation
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Deferred
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import com.example.weatherapp.internal.NoLocationAccessPermission
import com.example.weatherapp.internal.asDeferred

const val USE_DEVICE_LOCATION = "USE_DEVICE_LOCATION"
const val CUSTOM_LOCATION = "CUSTOM _LOCATION"

class LocationProviderImpl(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    context: Context
) : PreferenceProvider(context), LocationProvider {
    private val appcontext = context.applicationContext

    override suspend fun hasLocationChanged(lastSavedLocation: WeatherLocation): Boolean {

        val deviceLocationChanged = try{
            hasDeviceLocationChanged(lastSavedLocation)
        } catch (e:NoLocationAccessPermission){
            false
        }

        return deviceLocationChanged || hasCustomLocationChanged(lastSavedLocation)
    }

    override suspend fun getPreferredLocation(): String {
        return if(isUsingDeviceLocation()){
            try{
                val deviceLocation:Location?= getLastDeviceLocation().await()
                if(deviceLocation!==null)
                    "${getCustomLocationName()}"
                else
                    "${deviceLocation?.latitude},${deviceLocation?.longitude}"
            } catch (e:NoLocationAccessPermission) {
                "${getCustomLocationName()}"
            }
        } else
            "${getCustomLocationName()}"
    }

    private suspend fun hasDeviceLocationChanged(lastSavedLocation: WeatherLocation): Boolean {

        if (!isUsingDeviceLocation()) // no grant for location
            return false

        val deviceLocation: Location = getLastDeviceLocation().await() ?: return false

        //comparing doubles can't be done by ==
        val comparisionThreshold = 0.03
        // math absolute value functions

        return Math.abs(deviceLocation.latitude - lastSavedLocation.lat) > comparisionThreshold &&
                Math.abs(deviceLocation.longitude - lastSavedLocation.lon) > comparisionThreshold
    }
    private fun hasCustomLocationChanged(lastSavedLocation: WeatherLocation): Boolean {
        if (!isUsingDeviceLocation()) {
            val customLocationName = getCustomLocationName()
            return customLocationName != lastSavedLocation.name
        }
        return false
    }

    private fun getCustomLocationName(): String? {
        return preferences.getString(CUSTOM_LOCATION, null)
    }
    private fun isUsingDeviceLocation(): Boolean {
        return preferences.getBoolean(USE_DEVICE_LOCATION, true)
    }

    @SuppressLint("MissingPermission")
    private fun getLastDeviceLocation(): Deferred<Location> {
        return if (hasLocationPermissions())
            fusedLocationProviderClient.lastLocation.asDeferred()
        else
            throw NoLocationAccessPermission()
    }

    private fun hasLocationPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            appContext,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}