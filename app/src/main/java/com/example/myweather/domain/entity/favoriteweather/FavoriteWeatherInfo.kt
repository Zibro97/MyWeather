package com.example.myweather.domain.entity.favoriteweather

import com.google.gson.annotations.SerializedName

data class FavoriteWeatherInfo(
    //날씨 정보, 날씨 설명, 날씨 아이콘
    @SerializedName("weather")
    val weather: List<WeatherDetailModel>,
    //온도,최고/최저기온
    @SerializedName("main")
    val main : MainWeatherModel
)