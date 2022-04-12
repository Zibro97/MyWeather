package com.example.myweather.domain.usecase

import com.example.myweather.domain.entity.favorite.FavoriteEntity
import com.example.myweather.domain.repository.FavoriteRepository

class GetAllFavoriteUseCase(
    private val favoriteRepository: FavoriteRepository
) {
    suspend operator fun invoke() : List<FavoriteEntity> = favoriteRepository.getAllFavorite()
}