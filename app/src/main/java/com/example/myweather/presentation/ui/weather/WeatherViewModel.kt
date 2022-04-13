package com.example.myweather.presentation.ui.weather

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myweather.data.db.DatabaseProvider.getAppDatabase
import com.example.myweather.presentation.base.BaseViewModel
import kotlinx.coroutines.launch

class WeatherViewModel: BaseViewModel() {
    val locationCntLiveData:MutableLiveData<Int> = MutableLiveData()

    //db에 저장된 위치정보 개수 반환하는 함수
    fun getLocationCnt(context:Context){
        viewModelScope.launch {
            val cnt = getAppDatabase(context).favoriteDao().favoriteCnt()
            locationCntLiveData.value = cnt
        }
    }
}