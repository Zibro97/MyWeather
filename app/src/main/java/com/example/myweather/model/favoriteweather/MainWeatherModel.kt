package com.example.myweather.model.favoriteweather

import com.google.gson.annotations.SerializedName

data class MainWeatherModel(
    //현재 기온
    @SerializedName("temp")
    val temp : Double,
    //최저 기온
    @SerializedName("temp_min")
    val min : Double,
    //최고 기온
    @SerializedName("temp_max")
    val max : Double
)
