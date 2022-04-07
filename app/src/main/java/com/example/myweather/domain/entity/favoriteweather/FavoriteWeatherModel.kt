package com.example.myweather.domain.entity.favoriteweather

import com.google.gson.annotations.SerializedName

data class FavoriteWeatherModel(
    @SerializedName("list")
    val favoriteList : List<FavoriteWeatherInfo>
)
