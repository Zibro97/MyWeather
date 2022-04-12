package com.example.myweather.di

import com.example.myweather.data.api.LocationApi
import com.example.myweather.data.api.WeatherApi
import com.example.myweather.presentation.util.Constants.WEATHER_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideWeatherRetrofit(okHttpClient:OkHttpClient) : Retrofit =
        Retrofit.Builder()
            .baseUrl(WEATHER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    @Provides
    fun provideWeatherApiService(retrofit: Retrofit) : WeatherApi = retrofit.create(WeatherApi::class.java)

    @Provides
    fun provideLocationApiService(retrofit: Retrofit) : LocationApi = retrofit.create(LocationApi::class.java)
}