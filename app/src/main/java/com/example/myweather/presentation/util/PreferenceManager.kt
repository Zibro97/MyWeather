package com.example.myweather.presentation.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class PreferenceManager(context: Context) {
    private val prefs:SharedPreferences = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE)

    fun getCurrent():String? = prefs.getString(CURRENT_KEY,DEFAULT_VALUE)

    fun setCurrent(location:String) = prefs.edit { putString(CURRENT_KEY,location).apply() }

    companion object{
        const val PREFS_NAME = "current"
        const val CURRENT_KEY = "location"
        const val DEFAULT_VALUE = "no current"
    }
}