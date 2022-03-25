package com.example.myweather.model.weather

import androidx.annotation.DrawableRes
import com.example.myweather.R
import com.google.gson.annotations.SerializedName

enum class WeatherGrade(
    val label:String,
    val emoji:String,
    @DrawableRes val background : Int
) {
    @SerializedName("Thunderstorm")
    THUNDERSTORM("뇌우","🌩",R.drawable.ic_lightning_background),

    @SerializedName("Drizzle")
    DRIZZLE("소나기","🌦",R.drawable.ic_rain_background),

    @SerializedName("Rain")
    RAIN("비","🌧",R.drawable.ic_rain_background),

    @SerializedName("Snow")
    SNOW("눈","❄️",R.drawable.ic_snow_background),

    @SerializedName("Mist")
    MIST("안개","🌫",R.drawable.fog_background),

    @SerializedName("Smoke")
    SMOKE("연기","😷",R.drawable.fog_background),

    @SerializedName("Haze")
    Haze("안개","🌫",R.drawable.fog_background),

    @SerializedName("Dust")
    DUST("미세먼지","😷",R.drawable.fog_background),

    @SerializedName("Fog")
    FOG("안개","🌫",R.drawable.fog_background),

    @SerializedName("Sand")
    SAND("미세먼지","😷",R.drawable.fog_background),

    @SerializedName("Ash")
    ASH("화산재","😷",R.drawable.fog_background),

    @SerializedName("Squall")
    SQUALL("돌풍","🌪",R.drawable.ic_tornado_background),

    @SerializedName("Tornado")
    TORNADO("폭풍","🌪",R.drawable.ic_tornado_background),

    @SerializedName("Clear")
    CLEAR("맑음","☀️",R.drawable.ic_clear_background),

    @SerializedName("Clouds")
    CLOUDS("구름","☁️",R.drawable.ic_cloudy_background),
}