package com.example.myweather.domain.usecase

import com.example.myweather.domain.entity.favorite.FavoriteEntity
import com.example.myweather.domain.repository.FavoriteRepository

class InsertFavoriteUseCase(
    private val favoriteRepository: FavoriteRepository
) {
    suspend operator fun invoke(favorite:FavoriteEntity) = favoriteRepository.insertFavorite(favorite)
}