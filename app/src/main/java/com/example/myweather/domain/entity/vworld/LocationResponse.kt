package com.example.myweather.domain.entity.vworld

import com.google.gson.annotations.SerializedName

data class LocationResponse(
    @SerializedName("result")
    val result: LocationResult?
)
