package com.example.myweather.model.vworld

import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("point")
    val point: LocationLatLng
)
