package com.example.myweather.model

import com.google.gson.annotations.SerializedName

//현재 날씨 데이터
data class CurrentWeatherModel(
    //일출 시간
    @SerializedName("sunrise")
    val sunrise : Int,
    //일몰시간
    @SerializedName("sunset")
    val sunset : Int,
    //기온
    @SerializedName("temp")
    val temp:Double,
    //체감온도
    @SerializedName("feels_like")
    val feelsLike : Double,
    //습도
    @SerializedName("humidity")
    val humidity : Int,
    //이슬점
    @SerializedName("dew_point")
    val dewPoint : Double,
    //가시거리(미터)
    @SerializedName("visibility")
    val visibility : Int,
    //풍속
    @SerializedName("wind_speed")
    val windSpeed : Double,
    //풍향
    @SerializedName("wind_deg")
    val windDeg : Double,
    //강우량(비가 올 경우)
    @SerializedName("rain")
    val rain : Int,
    //적설량(눈 올 경우)
    @SerializedName("snow")
    val snow:Int,
    //날씨
    @SerializedName("weather")
    val weather : List<WeatherModel>
)