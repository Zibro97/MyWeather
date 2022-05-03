package com.example.myweather.presentation.ui.weather

import androidx.lifecycle.*
import com.example.myweather.domain.entity.favorite.FavoriteEntity
import com.example.myweather.domain.entity.weather.WeatherDTO
import com.example.myweather.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeatherUseCase :GetWeatherUseCase,
    private val insertFavoriteUseCase : InsertFavoriteUseCase,
    private val updateCurrentFavoriteUseCase : UpdateCurrentFavoriteUseCase,
    private val getLocationIdUseCase: GetLocationIdUseCase,
    private val getAllFavoriteUseCase: GetAllFavoriteUseCase
): ViewModel() {
    //날씨 정보 LiveData
    private val _weatherLiveData:MutableLiveData<WeatherDTO> = MutableLiveData()
    val weatherLiveData : LiveData<WeatherDTO> = _weatherLiveData
    //관심지역 정보 LiveData
    private val _favoriteLiveData : LiveData<List<FavoriteEntity>> = getAllFavoriteUseCase.invoke().asLiveData()
    val favoriteLiveData : LiveData<List<FavoriteEntity>> = _favoriteLiveData

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
            _weatherLiveData.value = getWeatherUseCase.invoke(latitude,longitude)
        }
    }
    //관심지역 or 현재 위치 저장하기 위한 함수
    fun insertFavorite(location:String,latitude: Double,longitude: Double){
        viewModelScope.launch {
            val locationId = getLocationIdUseCase.invoke(latitude,longitude)
            val favorite = FavoriteEntity(id = null,locationId = locationId,location = location,latitude = latitude,longitude = longitude)
            insertFavoriteUseCase.invoke(favorite)
        }
    }
    //현재 위치 정보 수정하는 함수
    fun updateCurrentLocation(latitude: Double,longitude: Double){
        viewModelScope.launch {
            val locationId = getLocationIdUseCase.invoke(latitude,longitude)
            val favorite = FavoriteEntity(id = 1,locationId = locationId,location = "나의 위치",latitude = latitude,longitude=longitude)
            updateCurrentFavoriteUseCase.invoke(favorite)
        }
    }
}