package com.example.myweather.model.vworld

import com.google.gson.annotations.SerializedName

data class VworldLocation(
    @SerializedName("response")
    val response : LocationResponse
)
