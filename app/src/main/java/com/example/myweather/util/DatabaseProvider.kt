package com.example.myweather.util

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    //DB명
    private const val DB_NAME = "WeatherDB"
    //RoomDatabase에 접근하는 함수
    fun getAppDatabase(context: Context):AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME).build()

}