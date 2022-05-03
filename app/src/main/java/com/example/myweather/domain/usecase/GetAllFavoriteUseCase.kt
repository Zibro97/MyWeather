package com.example.myweather.domain.usecase

import com.example.myweather.domain.entity.favorite.FavoriteEntity
import com.example.myweather.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow

class GetAllFavoriteUseCase(
    private val favoriteRepository: FavoriteRepository
) {
    operator fun invoke() : Flow<List<FavoriteEntity>> = favoriteRepository.getAllFavorite()
}