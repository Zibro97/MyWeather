package com.example.myweather.model.favorite

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Favorite(
    @PrimaryKey val id:Int?,
    @ColumnInfo(name = "location") val location:String,
    @ColumnInfo(name = "latitude") val latitude:Double,
    @ColumnInfo(name = "longitude") val longitude:Double,
)