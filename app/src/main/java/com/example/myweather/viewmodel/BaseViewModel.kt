package com.example.myweather.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweather.data.api.RetrofitClient
import com.example.myweather.data.db.DatabaseProvider
import com.example.myweather.model.favorite.Favorite
import com.example.myweather.model.weather.WeatherDTO
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {
    private val service = RetrofitClient.weatherService
    val weatherLiveData:MutableLiveData<WeatherDTO> = MutableLiveData()
    val locationLiveData: MutableLiveData<List<Favorite>> = MutableLiveData()

    //동작 과정
    //1. view에서 getWeather함수를 호출
    //2. launch가 새 코루틴을 생성,스레드에서 독립적으로 네트워크 요청
    //3. 코루틴이 실행되는 동안 네트워크 요청이 완료되기 전에 getWeather 함수가 계속 실행되어 결과를 반환.
    //해당 지역 날씨정보 가져오는 함수
    fun getWeather(latitude:Double,longitude:Double){
        //viewModelScope : viewModel KTX확장 프로그램에 포함된 사전 정의된 CouroutineScope,모든 코루틴은 해당 범위 내에서 실행해야 함.
        //CouroutineScope는 하나 이상의 코루틴을 관리
        //launch : 코루틴을 만들고 함수 본문의 실행을 해당하는 디스패처에 전달하는 함수
        viewModelScope.launch {
            val jsonArray = service.weatherInfo(latitude = latitude,longitude = longitude)
            weatherLiveData.value = jsonArray
        }
    }
    //db에 저장된 모든 위치 정보 가져오는 함수
    fun getAllLocation(context: Context){
        viewModelScope.launch {
            val favorite = DatabaseProvider.getAppDatabase(context).favoriteDao().getAll()
            locationLiveData.value = favorite
        }
    }
    //관심지역 or 현재 위치 저장하기 위한 함수
    fun insertLocation(context:Context,location:String,latitude: Double,longitude: Double){
        viewModelScope.launch {
            val favorite = Favorite(id = null,location = location,latitude = latitude,longitude = longitude)
            DatabaseProvider.getAppDatabase(context).favoriteDao().insertFavorite(favorite)
            getAllLocation(context)
        }
    }
}