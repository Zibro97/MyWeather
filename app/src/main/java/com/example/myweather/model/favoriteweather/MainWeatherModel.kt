package com.example.myweather.model.favoriteweather

import com.google.gson.annotations.SerializedName

data class MainWeatherModel(
    @SerializedName("temp")
    val temp : Double,
    @SerializedName("temp_min")
    val min : Double,
    @SerializedName("temp_max")
    val max : Double
)
