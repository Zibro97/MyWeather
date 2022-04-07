package com.example.myweather.domain.entity.weather

import com.google.gson.annotations.SerializedName

data class WeatherDTO(
    //현재 날씨 데이터
    @SerializedName("current")
    val current : CurrentWeatherModel,
    //향후 시간별 날씨 예측 데이터
    @SerializedName("hourly")
    val hourly : List<HourlyWeatherModel>,
    //7일 동안의 날씨 예측 데이터
    @SerializedName("daily")
    val daily : List<DailyWeatherModel>
)
