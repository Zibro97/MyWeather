package com.example.myweather.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myweather.domain.entity.favorite.FavoriteEntity

//AppDatabase : 데이터 베이스 구성을 정의하고 지성되는 데이터에 대한 앱의 기본 액세스 지점 역할
@Database(entities = [FavoriteEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun favoriteDao() : FavoriteDao

    companion object{
        private var INSTANCE:AppDatabase? = null
        //DB명
        private const val DB_NAME = "WeatherDB"
        //RoomDatabase 인스턴스에 접근하는 함수
        private fun getAppDatabase(context: Context): AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME).build()
        fun getInstance(context :Context) : AppDatabase = INSTANCE ?: synchronized(this){
            INSTANCE ?: getAppDatabase(context).also { INSTANCE = it }
        }
    }
}