package com.example.myweather.model

import com.google.gson.annotations.SerializedName

data class TemperatureModel (
    @SerializedName("min")
    val minTemp : Double,
    @SerializedName("max")
    val maxTemp:Double,
)