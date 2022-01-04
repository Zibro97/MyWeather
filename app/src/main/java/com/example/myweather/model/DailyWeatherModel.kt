package com.example.myweather.model

import com.google.gson.annotations.SerializedName

data class DailyWeatherModel(
    @SerializedName("dt")
    val dt:Int,
    @SerializedName("temp")
    val temp:TemperatureModel,
    @SerializedName("weather")
    val weather : List<WeatherModel>,
)
