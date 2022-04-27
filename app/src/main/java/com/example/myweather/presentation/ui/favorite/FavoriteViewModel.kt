package com.example.myweather.presentation.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweather.domain.entity.favorite.FavoriteEntity
import com.example.myweather.domain.entity.favoriteweather.FavoriteWeatherModel
import com.example.myweather.domain.entity.vworld.VworldLocation
import com.example.myweather.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getLocationInfoUseCase: GetLocationInfoUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
    private val getAllFavoriteUseCase: GetAllFavoriteUseCase,
    private val insertFavoriteUseCase: InsertFavoriteUseCase,
    private val getLocationIdUseCase: GetLocationIdUseCase,
    private val getWeatherListUseCase: GetWeatherListUseCase
) : ViewModel() {
    //VWorld 위치 정보 검색 결과 LiveData
    private val _searchLocateLiveData: MutableLiveData<VworldLocation> = MutableLiveData()
    val searchLocateLiveData:LiveData<VworldLocation> = _searchLocateLiveData
    //Room에 저장된 모든 Favorite LiveData
    private val _locationLiveData: MutableLiveData<List<FavoriteEntity>> = MutableLiveData()
    val locationLiveData : LiveData<List<FavoriteEntity>> = _locationLiveData
    //관심지역들 날씨 정보 LiveData
    private val _weatherListLiveData : MutableLiveData<FavoriteWeatherModel> = MutableLiveData()
    val weatherListLiveData : LiveData<FavoriteWeatherModel> = _weatherListLiveData

    //VWorld 위치 정보 얻는 함수
    fun getLocationInfo(keyword: String) {
        viewModelScope.launch {
            _searchLocateLiveData.value = getLocationInfoUseCase.invoke(keyword)
        }
    }

    //Favorite Room 관심지역 삭제
    fun removeFavorite(favoriteEntity: FavoriteEntity) {
        viewModelScope.launch {
            removeFavoriteUseCase.invoke(favoriteEntity)
        }
    }

    //db에 저장된 모든 위치 정보 가져오는 함수
    fun getAllFavorites(){
        viewModelScope.launch {
            _locationLiveData.value = getAllFavoriteUseCase.invoke()
        }
    }

    //관심지역 or 현재 위치 저장하기 위한 함수
    fun insertLocation(location:String,latitude: Double,longitude: Double){
        viewModelScope.launch {
            val locationId = getLocationIdUseCase.invoke(latitude,longitude)
            val favorite = FavoriteEntity(id = null,locationId = locationId,location = location,latitude = latitude,longitude = longitude)
            insertFavoriteUseCase.invoke(favorite)
        }
    }

    //관심지역들 날씨 정보 가져오는 함수
    fun getLocationsWeather(id:String){
        viewModelScope.launch {
            _weatherListLiveData.value = getWeatherListUseCase.invoke(id)
        }
    }
}