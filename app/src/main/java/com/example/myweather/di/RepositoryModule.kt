package com.example.myweather.di

import com.example.myweather.data.repository.FavoriteRepositoryImpl
import com.example.myweather.data.repository.LocationRepositoryImpl
import com.example.myweather.data.repository.WeatherRepositoryImpl
import com.example.myweather.domain.repository.FavoriteRepository
import com.example.myweather.domain.repository.LocationRepository
import com.example.myweather.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds //하나의 객체를 변수로 받아 해당 변수를 생성하고 프로퍼티, 메서드까지 @Inject가 붙은 모든 주입을  처리하고 나서 반환하는 Annotation
    abstract fun bindFavoriteRepository(
        favoriteRepositoryImpl: FavoriteRepositoryImpl
    ) : FavoriteRepository

    @Singleton
    @Binds
    abstract fun bindLocationRepository(
        locationRepositoryImpl: LocationRepositoryImpl
    ) : LocationRepository

    @Singleton
    @Binds
    abstract fun bindWeatherRepository(
        weatherRepositoryImpl: WeatherRepositoryImpl
    ) : WeatherRepository
}