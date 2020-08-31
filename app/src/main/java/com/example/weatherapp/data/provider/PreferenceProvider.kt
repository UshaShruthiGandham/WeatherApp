package com.example.weatherapp.data.provider

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

const val PREF_FILE="PrefFile"
abstract class PreferenceProvider(context: Context) {
     val appContext= context.applicationContext

    protected val preferences: SharedPreferences
    get() = PreferenceManager.getDefaultSharedPreferences(appContext)

}