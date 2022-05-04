package com.example.myweather.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val WEATHER_BASE_URL = "https://api.openweathermap.org"
    private const val FAVORITE_BASE_URL = "http://api.vworld.kr"

    val WEATHER_API: WeatherApi by lazy{ weatherRetrofit().create(WeatherApi::class.java)}

    private fun weatherRetrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl(WEATHER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}