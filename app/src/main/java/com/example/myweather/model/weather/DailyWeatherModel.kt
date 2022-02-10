package com.example.myweather.model.weather

import com.google.gson.annotations.SerializedName

data class DailyWeatherModel(
    //예측 날짜
    @SerializedName("dt")
    val dt:Int,
    //예측 온도
    @SerializedName("temp")
    val temp: TemperatureModel,
    //예측 날씨
    @SerializedName("weather")
    val weather : List<WeatherModel>,
)
