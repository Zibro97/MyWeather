package com.example.myweather.model.favoriteweather

import com.google.gson.annotations.SerializedName

data class FavoriteWeatherModel(
    @SerializedName("list")
    val favoriteList : List<FavoriteWeatherInfo>
)
