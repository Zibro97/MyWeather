package com.example.myweather.domain.entity.weather

import androidx.annotation.DrawableRes
import com.example.myweather.R
import com.google.gson.annotations.SerializedName

enum class WeatherGrade(
    val label:String,
    val emoji:String,
    @DrawableRes val background : Int
) {
    @SerializedName("Thunderstorm")
    THUNDERSTORM("λμ°","π©",R.drawable.ic_lightning_background),

    @SerializedName("Drizzle")
    DRIZZLE("μλκΈ°","π¦",R.drawable.ic_rain_background),

    @SerializedName("Rain")
    RAIN("λΉ","π§",R.drawable.ic_rain_background),

    @SerializedName("Snow")
    SNOW("λ","βοΈ",R.drawable.ic_snow_background),

    @SerializedName("Mist")
    MIST("μκ°","π«",R.drawable.fog_background),

    @SerializedName("Smoke")
    SMOKE("μ°κΈ°","π·",R.drawable.fog_background),

    @SerializedName("Haze")
    Haze("μκ°","π«",R.drawable.fog_background),

    @SerializedName("Dust")
    DUST("λ―ΈμΈλ¨Όμ§","π·",R.drawable.fog_background),

    @SerializedName("Fog")
    FOG("μκ°","π«",R.drawable.fog_background),

    @SerializedName("Sand")
    SAND("λ―ΈμΈλ¨Όμ§","π·",R.drawable.fog_background),

    @SerializedName("Ash")
    ASH("νμ°μ¬","π·",R.drawable.fog_background),

    @SerializedName("Squall")
    SQUALL("λν","πͺ",R.drawable.ic_tornado_background),

    @SerializedName("Tornado")
    TORNADO("ν­ν","πͺ",R.drawable.ic_tornado_background),

    @SerializedName("Clear")
    CLEAR("λ§μ","βοΈ",R.drawable.ic_clear_background),

    @SerializedName("Clouds")
    CLOUDS("κ΅¬λ¦","βοΈ",R.drawable.ic_cloudy_background),
}