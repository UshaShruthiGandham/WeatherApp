package com.example.weatherapp.ui.settings

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.example.weatherapp.MainActivity
import com.example.weatherapp.R


class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)

        //val editPref = findPreference<EditTextPreference>(getString(R.string.Custom_Location_key))
        //editPref?.setDefaultValue(R.string.Custom_Location_key_Default)
     /*  editPref?.setOnPreferenceChangeListener { preference, newValue ->
           editPref.text=newValue.toString()
           *//*val preferences= context?.applicationContext?.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE)
           var editor= preferences?.edit()
           editor?.putString(getString(R.string.Custom_Location_key),editPref.text)
           editor?.apply()
           editor?.commit()*//*
           return@setOnPreferenceChangeListener false
       }*/

        val editDevicePref = findPreference<SwitchPreference>(getString(R.string.Device_Location_key))
        editDevicePref?.setOnPreferenceChangeListener { preference, newValue ->
            if(newValue.toString().toBoolean() && !(activity as MainActivity).hasLocationPermissions()){
                ActivityCompat.requestPermissions((activity as MainActivity), arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),1)
            } else{
                editDevicePref?.isChecked=newValue.toString().toBoolean()
            }
            return@setOnPreferenceChangeListener false
        }


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.title="Settings"
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle=null

    }
}