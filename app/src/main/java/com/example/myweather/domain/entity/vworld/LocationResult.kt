package com.example.myweather.domain.entity.vworld

import com.google.gson.annotations.SerializedName

data class LocationResult(
    //지역 검색 결과 List
    @SerializedName("items")
    val items : List<Location>
)
