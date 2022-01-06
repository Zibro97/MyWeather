package com.example.myweather

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.myweather.dao.FavoriteDao
import com.example.myweather.model.Favorite

@Database(entities = [Favorite::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun favoriteDao() : FavoriteDao
}

fun getAppDatabase(context:Context):AppDatabase{

    return Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "WeatherDB"
    ).build()
}