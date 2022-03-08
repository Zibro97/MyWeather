package com.example.myweather.model.favoriteweather

import com.example.myweather.model.weather.WeatherGrade
import com.google.gson.annotations.SerializedName

data class WeatherDetailModel(
    @SerializedName("main")
    val main : WeatherGrade
)
