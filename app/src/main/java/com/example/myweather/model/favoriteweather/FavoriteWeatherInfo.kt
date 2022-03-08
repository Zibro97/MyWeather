package com.example.myweather.model.favoriteweather

import com.google.gson.annotations.SerializedName

data class FavoriteWeatherInfo(
    @SerializedName("weather")
    val weather: List<WeatherDetailModel>,
    @SerializedName("main")
    val main : MainWeatherModel
)