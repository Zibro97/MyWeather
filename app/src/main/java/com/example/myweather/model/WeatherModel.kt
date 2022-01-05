package com.example.myweather.model

import com.google.gson.annotations.SerializedName

data class WeatherModel(
    //날씨
    @SerializedName("main")
    val main : String,
    //날씨 정보
    @SerializedName("description")
    val description : String,
    //날씨 정보 아이콘
    @SerializedName("icon")
    val icon : String
)
