package com.example.myweather.api

import com.example.myweather.BuildConfig
import com.example.myweather.model.WeatherDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    //해당 위치의 날씨 데이터 요청
    @GET("/data/2.5/onecall?exclude=minutely&units=metric&lang=kr&appid=${BuildConfig.OPEN_WEATHER_API_KEY}")
    fun getWeather(
        @Query("lat") latitude:Double,
        @Query("lon") longitude:Double,
    ):Call<WeatherDTO>
}