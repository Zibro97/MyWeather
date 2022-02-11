package com.example.myweather.model.vworld

import com.google.gson.annotations.SerializedName

data class LocationResult(
    @SerializedName("items")
    val items : List<Location>
)
