package com.example.myweather.domain.repository

import com.example.myweather.domain.entity.favoriteweather.FavoriteWeatherModel
import com.example.myweather.domain.entity.favoriteweather.LocationIdModel
import com.example.myweather.domain.entity.weather.WeatherDTO

/**
* 날씨 정보를 가져오는 Repository
* */
interface WeatherRepository {

    //해당 지역의 날씨 정보를 가져옴
    suspend fun getWeather(
        latitude:Double,
        longitude:Double
    ) : WeatherDTO

    //지역들의 날씨 정보를 가져옴
    suspend fun getWeatherList(
        idList : String
    ) : FavoriteWeatherModel

    //해당 지역의 id값을 가져옴
    suspend fun getLocationId(
        latitude:Double,
        longitude:Double
    ) : Int
}