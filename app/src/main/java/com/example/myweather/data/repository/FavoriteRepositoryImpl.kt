package com.example.myweather.data.repository

import com.example.myweather.data.db.FavoriteDao
import com.example.myweather.di.IODispatcher
import com.example.myweather.domain.entity.favorite.FavoriteEntity
import com.example.myweather.domain.repository.FavoriteRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val favoriteDao: FavoriteDao,
    @IODispatcher private val ioDispatcher : CoroutineDispatcher = Dispatchers.IO
) : FavoriteRepository {
    override val allFavoriteEntity: Flow<List<FavoriteEntity>>
        get() = favoriteDao.getAllFavorite()

    override suspend fun insertFavorite(favorite: FavoriteEntity) = withContext(ioDispatcher){
        favoriteDao.insertFavorite(favorite)
    }

    override suspend fun removeFavorite(favorite: FavoriteEntity) = withContext(ioDispatcher){
        favoriteDao.removeFavorite(favorite)
    }

    override fun getAllFavorite(): Flow<List<FavoriteEntity>> = flow {
        favoriteDao.getAllFavorite().collect {
            Timber.d("Favorite List : $it")
            if(it.isNotEmpty()) emit(it)
        }
    }

    override suspend fun updateCurrentFavorite(favorite: FavoriteEntity) = withContext(ioDispatcher){
        favoriteDao.updateCurrentFavorite(favorite)
    }
}