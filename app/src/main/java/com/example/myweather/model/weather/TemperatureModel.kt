package com.example.myweather.model.weather

import com.google.gson.annotations.SerializedName

data class TemperatureModel (
    //최저 온도
    @SerializedName("min")
    val minTemp : Double,
    //최고 온도
    @SerializedName("max")
    val maxTemp:Double,
)