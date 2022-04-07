package com.example.myweather.domain.entity.vworld

import com.google.gson.annotations.SerializedName


data class LocationLatLng(
    @SerializedName("x")
    val x : Double,
    @SerializedName("y")
    val y : Double
)