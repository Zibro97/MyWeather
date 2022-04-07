package com.example.myweather.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myweather.domain.entity.favorite.FavoriteEntity

//AppDatabase : 데이터 베이스 구성을 정의하고 지성되는 데이터에 대한 앱의 기본 액세스 지점 역할
@Database(entities = [FavoriteEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun favoriteDao() : FavoriteDao
}