package com.example.myweather.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweather.model.Favorite
import com.example.myweather.util.RetrofitClient
import com.example.myweather.model.WeatherDTO
import com.example.myweather.util.DatabaseProvider.getAppDatabase
import kotlinx.coroutines.launch

class WeatherViewModel:ViewModel() {
    private val service = RetrofitClient.weatherService
    val weatherLiveData:MutableLiveData<WeatherDTO> = MutableLiveData()
    val favoriteLiveData:MutableLiveData<List<Favorite>> = MutableLiveData()

    //동작 과정
    //1. view에서 getWeather함수를 호출
    //2. launch가 새 코루틴을 생성,스레드에서 독립적으로 네트워크 요청
    //3. 코루틴이 실행되는 동안 네트워크 요청이 완료되기 전에 getWeather 함수가 계속 실행되어 결과를 반환.
    fun getWeather(latitude:Double,longitude:Double){
        //viewModelScope : viewModel KTX확장 프로그램에 포함된 사전 정의된 CouroutineScope,모든 코루틴은 해당 범위 내에서 실행해야 함.
        //CouroutineScope는 하나 이상의 코루틴을 관리
        //launch : 코루틴을 만들고 함수 본문의 실행을 해당하는 디스패처에 전달하는 함수
        viewModelScope.launch {
            val jsonArray = service.getWeather(latitude = latitude,longitude = longitude)
            weatherLiveData.value = jsonArray
        }
    }

    fun insertFavorite(context:Context,location:String,latitude: Double,longitude: Double){
        viewModelScope.launch {
            val favorite = Favorite(id = null,location = location,latitude = latitude,longitude = longitude)
            getAppDatabase(context).favoriteDao().insertFavorite(favorite)
        }
    }

    fun getAllLocation(context: Context){
        viewModelScope.launch {
            val favorite = getAppDatabase(context).favoriteDao().getAll()
            favoriteLiveData.value = favorite
        }
    }

    fun updateCurrentLocation(context: Context,latitude: Double,longitude: Double){
        viewModelScope.launch {
            val favorite = Favorite(id = null,location = "나의 위치",latitude = latitude,longitude=longitude)
            getAppDatabase(context).favoriteDao().updateCurrentLocation(favorite)
        }
    }
}