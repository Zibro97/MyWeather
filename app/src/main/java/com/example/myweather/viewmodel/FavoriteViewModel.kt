package com.example.myweather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweather.data.api.RetrofitClient
import com.example.myweather.model.vworld.Location
import com.example.myweather.model.vworld.VworldLocation
import kotlinx.coroutines.launch

class FavoriteViewModel:BaseViewModel() {
    private val service = RetrofitClient.favoriteService
    val searchLocateLiveData : MutableLiveData<VworldLocation> = MutableLiveData()

    fun locationInfo(keyword:String){
        viewModelScope.launch {
            val jsonArray = service.locationInfo(keyword)
            searchLocateLiveData.value = jsonArray
        }
    }

}