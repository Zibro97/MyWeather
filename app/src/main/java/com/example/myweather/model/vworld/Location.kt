package com.example.myweather.model.vworld

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
//검색 결과 지역 정보
data class Location(
    //지역 id
    @SerializedName("id")
    val id: Int,
    //지역 명
    @SerializedName("title")
    val title: String,
    //지역 위도경도
    @SerializedName("point")
    val point: LocationLatLng
) : Parcelable
