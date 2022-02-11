package com.example.myweather.model.vworld

import com.google.gson.annotations.SerializedName

data class LocationResponse(
    @SerializedName("result")
    val result:LocationResult?
)
