package com.example.myweather.presentation.ui.favorite

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myweather.data.api.RetrofitClient
import com.example.myweather.data.db.DatabaseProvider
import com.example.myweather.domain.entity.favorite.FavoriteEntity
import com.example.myweather.domain.entity.vworld.VworldLocation
import com.example.myweather.presentation.base.BaseViewModel
import kotlinx.coroutines.launch

class FavoriteViewModel : BaseViewModel() {
    private val service = RetrofitClient.LOCATION_API
    val searchLocateLiveData: MutableLiveData<VworldLocation> = MutableLiveData()

    fun locationInfo(keyword: String) {
        viewModelScope.launch {
            val jsonArray = service.locationInfo(keyword)
            searchLocateLiveData.value = jsonArray
        }
    }

    fun removeFavorite(context: Context, favoriteEntity: FavoriteEntity) {
        viewModelScope.launch {
            DatabaseProvider.getAppDatabase(context).favoriteDao().removeFavorite(favoriteEntity)
            getAllLocation(context)
        }
    }
}