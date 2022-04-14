package com.example.myweather.presentation.base

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweather.data.api.RetrofitClient
import com.example.myweather.data.db.DatabaseProvider
import com.example.myweather.domain.entity.favorite.FavoriteEntity
import com.example.myweather.domain.entity.favoriteweather.FavoriteWeatherModel
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {

}