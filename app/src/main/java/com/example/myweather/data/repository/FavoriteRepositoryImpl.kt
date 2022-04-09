package com.example.myweather.data.repository

import com.example.myweather.data.db.FavoriteDao
import com.example.myweather.domain.entity.favorite.FavoriteEntity
import com.example.myweather.domain.repository.FavoriteRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class FavoriteRepositoryImpl(
    private val favoriteDao: FavoriteDao,
    private val ioDispatcher : CoroutineDispatcher
) : FavoriteRepository {

    override suspend fun insertFavorite(favorite: FavoriteEntity) = withContext(ioDispatcher){
        favoriteDao.insertFavorite(favorite)
    }

    override suspend fun removeFavorite(favorite: FavoriteEntity) = withContext(ioDispatcher){
        favoriteDao.removeFavorite(favorite)
    }

    override suspend fun getAllFavorite(): List<FavoriteEntity> = withContext(ioDispatcher){
        favoriteDao.getAllFavorite()
    }

    override suspend fun updateCurrentFavorite(favorite: FavoriteEntity) = withContext(ioDispatcher){
        favoriteDao.updateCurrentFavorite(favorite)
    }
}