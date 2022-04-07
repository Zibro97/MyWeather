package com.example.myweather.domain.entity.vworld

import com.google.gson.annotations.SerializedName

data class VworldLocation(
    @SerializedName("response")
    val response : LocationResponse
)
