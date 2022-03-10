package com.example.myweather.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myweather.data.api.RetrofitClient
import com.example.myweather.data.db.DatabaseProvider
import com.example.myweather.model.favorite.Favorite
import com.example.myweather.model.vworld.VworldLocation
import kotlinx.coroutines.launch

class FavoriteViewModel : BaseViewModel() {
    private val service = RetrofitClient.favoriteService
    val searchLocateLiveData: MutableLiveData<VworldLocation> = MutableLiveData()

    fun locationInfo(keyword: String) {
        viewModelScope.launch {
            val jsonArray = service.locationInfo(keyword)
            searchLocateLiveData.value = jsonArray
        }
    }

    fun removeFavorite(context: Context, favorite: Favorite) {
        viewModelScope.launch {
            DatabaseProvider.getAppDatabase(context).favoriteDao().delete(favorite)
            getAllLocation(context)
        }
    }
}