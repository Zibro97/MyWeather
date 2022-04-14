package com.example.myweather.di

import android.content.Context
import com.example.myweather.data.db.AppDatabase
import com.example.myweather.data.db.FavoriteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    fun provideFavoriteDatabase(@ApplicationContext context: Context) : AppDatabase =
        AppDatabase.getInstance(context)

    @Provides
    fun provideFavoriteDao(appDatabase: AppDatabase) : FavoriteDao = appDatabase.favoriteDao()
}