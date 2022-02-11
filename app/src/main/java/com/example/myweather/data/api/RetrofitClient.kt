package com.example.myweather.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val WEATHER_BASE_URL = "https://api.openweathermap.org"
    private const val FAVORITE_BASE_URL = "http://api.vworld.kr"

    val weatherService: WeatherService by lazy{ weatherRetrofit().create(WeatherService::class.java)}
    val favoriteService : FavoriteService by lazy { favoriteRetrofit().create(FavoriteService::class.java) }

    private fun weatherRetrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl(WEATHER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private fun favoriteRetrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl(FAVORITE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}