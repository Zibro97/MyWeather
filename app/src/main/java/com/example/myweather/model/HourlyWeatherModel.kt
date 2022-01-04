package com.example.myweather.model

import com.google.gson.annotations.SerializedName

data class HourlyWeatherModel(
    //예측 데이터의 시간
    @SerializedName("dt")
    val dt : Int,
    //예측 온도
    @SerializedName("temp")
    val temp:Double,
    //예측 날씨
    @SerializedName("weather")
    val weather : List<WeatherModel>
)
