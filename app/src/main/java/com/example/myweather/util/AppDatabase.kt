package com.example.myweather.util

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myweather.dao.FavoriteDao
import com.example.myweather.model.Favorite

@Database(entities = [Favorite::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun favoriteDao() : FavoriteDao
}