package com.example.myweather.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Favorite(
    @PrimaryKey(autoGenerate = true) val id:Int?,
    @ColumnInfo(name = "latitude") val latitude:Double,
    @ColumnInfo(name = "longitude") val longitude:Double,
)
