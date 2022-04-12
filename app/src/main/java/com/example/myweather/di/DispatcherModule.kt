package com.example.myweather.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {
    @DefaultDispatcher
    @Provides
    fun provideDefaultDispatcher() : CoroutineDispatcher = Dispatchers.Default

    @IODispatcher
    @Provides
    fun provideIODisPatcher() : CoroutineDispatcher = Dispatchers.IO

    @MainDispatcher
    @Provides
    fun provideMainDisPatcher() : CoroutineDispatcher = Dispatchers.Main
}