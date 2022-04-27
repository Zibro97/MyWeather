package com.example.myweather.di

import android.content.Context
import com.example.myweather.BuildConfig
import com.example.myweather.data.api.LocationApi
import com.example.myweather.data.api.WeatherApi
import com.example.myweather.presentation.util.Constants.FAVORITE_BASE_URL
import com.example.myweather.presentation.util.Constants.WEATHER_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @Named("weather_retrofit")
    fun provideWeatherRetrofit(okHttpClient:OkHttpClient) : Retrofit =
        Retrofit.Builder()
            .baseUrl(WEATHER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    @Named("location_retrofit")
    fun provideLocationApiRetrofit(okHttpClient: OkHttpClient) : Retrofit =
        Retrofit.Builder()
            .baseUrl(FAVORITE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    @Provides
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
    ) : OkHttpClient = OkHttpClient.Builder().apply {
        if(BuildConfig.DEBUG){
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            addInterceptor(loggingInterceptor)
        }
    }
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(30,TimeUnit.SECONDS)
        .writeTimeout(30,TimeUnit.SECONDS)
        .build()


    @Provides
    fun provideWeatherApiService(@Named("weather_retrofit") retrofit: Retrofit) : WeatherApi = retrofit.create(WeatherApi::class.java)

    @Provides
    fun provideLocationApiService(@Named("location_retrofit") retrofit: Retrofit) : LocationApi = retrofit.create(LocationApi::class.java)
}