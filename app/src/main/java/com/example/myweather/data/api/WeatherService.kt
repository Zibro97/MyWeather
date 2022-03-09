package com.example.myweather.data.api

import com.example.myweather.BuildConfig
import com.example.myweather.model.favoriteweather.FavoriteWeatherModel
import com.example.myweather.model.favoriteweather.LocationIdModel
import com.example.myweather.model.weather.WeatherDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    //suspend fun : blocked 된 상태에 놓일 때, 그 작업을 suspend 하고 그 기간동안 thread에서 다른 작업 수행 가능.
    //oneCall API
    @GET("/data/2.5/onecall?exclude=minutely&units=metric&lang=kr&appid=${BuildConfig.OPEN_WEATHER_API_KEY}")
    suspend fun weatherInfo(
        @Query("lat") latitude:Double,
        @Query("lon") longitude:Double,
    ): WeatherDTO

    //weather api : 해당 지역 id값 알기 위한 api
    @GET("/data/2.5/weather?units=metric&appid=${BuildConfig.OPEN_WEATHER_API_KEY}")
    suspend fun getLocationId(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double
    ) : LocationIdModel

    //multiple locations weather api
    @GET("/data/2.5/group?&units=metric&appid=${BuildConfig.OPEN_WEATHER_API_KEY}")
    suspend fun locations(
        @Query("id",encoded = true) appId : String?
    ) : FavoriteWeatherModel
}