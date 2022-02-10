package com.example.myweather.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myweather.data.api.RetrofitClient

class FavoriteViewModel:ViewModel() {
    private val service = RetrofitClient.weatherService

}