package com.example.weatherapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.example.weatherapp.ui.LifeCycleBoundLocationManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

const val APP_PERMISSION_ACCESS_COARSE_LOCATION = 1

class MainActivity : AppCompatActivity(),KodeinAware {


    override val kodein by closestKodein()
    private val fusedLocationProviderClient:FusedLocationProviderClient by instance()
    private lateinit var navController: NavController
    private val locationCallback= object :LocationCallback(){
        override fun onLocationResult(p0: LocationResult?) {
            super.onLocationResult(p0)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        navController = Navigation.findNavController(this, R.id.nav_fragment)
        nav_bar.setupWithNavController(navController)
        NavigationUI.setupActionBarWithNavController(this, navController)

        requestLocationPermissions()

        if(hasLocationPermissions()){
            bindLocationManager()
        }
    }

    private fun bindLocationManager() {
      // lifecycle bound location manager
        LifeCycleBoundLocationManager(
            this,
            fusedLocationProviderClient,locationCallback
        )
    }

    private fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            APP_PERMISSION_ACCESS_COARSE_LOCATION
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }
    internal fun hasLocationPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == APP_PERMISSION_ACCESS_COARSE_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                PreferenceManager.getDefaultSharedPreferences(applicationContext).edit().putBoolean(getString(R.string.Device_Location_key),true).apply()
                bindLocationManager()
            }else {
                PreferenceManager.getDefaultSharedPreferences(applicationContext).edit().putBoolean(getString(R.string.Device_Location_key),false).apply()
                Toast.makeText(this, "Please, set location manually in settings", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
}