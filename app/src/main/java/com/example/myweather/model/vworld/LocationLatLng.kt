package com.example.myweather.model.vworld

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class LocationLatLng(
    @SerializedName("x")
    val x : Double,
    @SerializedName("y")
    val y : Double
) : Parcelable
