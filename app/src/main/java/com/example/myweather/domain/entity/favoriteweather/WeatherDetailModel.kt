package com.example.myweather.domain.entity.favoriteweather

import com.example.myweather.domain.entity.weather.WeatherGrade
import com.google.gson.annotations.SerializedName

data class WeatherDetailModel(
    @SerializedName("main")
    val main : WeatherGrade
)
