package com.example.myweather.di

import com.example.myweather.domain.repository.FavoriteRepository
import com.example.myweather.domain.repository.LocationRepository
import com.example.myweather.domain.repository.WeatherRepository
import com.example.myweather.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {
    @Provides
    fun providesGetAllFavoriteUseCase(
        repository : FavoriteRepository
    ) : GetAllFavoriteUseCase = GetAllFavoriteUseCase(repository)

    @Provides
    fun providesGetLocationIdUseCase(
        repository : WeatherRepository
    ) : GetLocationIdUseCase = GetLocationIdUseCase(repository)

    @Provides
    fun providesGetLocationInfoUseCase(
        repository: LocationRepository
    ) : GetLocationInfoUseCase = GetLocationInfoUseCase(repository)

    @Provides
    fun providesGetWeatherListUseCase(
        repository: WeatherRepository
    ) : GetWeatherListUseCase = GetWeatherListUseCase(repository)

    @Provides
    fun providesGetWeatherUseCase(
        repository: WeatherRepository
    ) : GetWeatherUseCase = GetWeatherUseCase(repository)

    @Provides
    fun providesInsertFavoriteUseCase(
        repository:FavoriteRepository
    ) : InsertFavoriteUseCase = InsertFavoriteUseCase(repository)

    @Provides
    fun providesRemoveFavoriteUseCase(
        repository : FavoriteRepository
    ) : RemoveFavoriteUseCase = RemoveFavoriteUseCase(repository)

    @Provides
    fun providesUpdateFavoriteUseCase(
        repository: FavoriteRepository
    ) : UpdateCurrentFavoriteUseCase = UpdateCurrentFavoriteUseCase(repository)
}