package com.example.myweather.domain.entity.favorite

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true) val id:Int?,
    @ColumnInfo(name = "locationId") val locationId:Int?,
    @ColumnInfo(name = "location") val location:String,
    @ColumnInfo(name = "latitude") val latitude:Double,
    @ColumnInfo(name = "longitude") val longitude:Double,
)