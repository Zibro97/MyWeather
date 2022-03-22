package com.example.myweather.model.vworld

import com.google.gson.annotations.SerializedName


data class LocationLatLng(
    @SerializedName("x")
    val x : Double,
    @SerializedName("y")
    val y : Double
)