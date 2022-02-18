package com.example.myweather.model.weather

import com.google.gson.annotations.SerializedName

data class RainSnowHour(
    //1시간동안의 강우량,적설량
    @SerializedName("1h")
    val h:Double
)
