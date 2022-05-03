package com.example.myweather.domain.repository

import com.example.myweather.domain.entity.favorite.FavoriteEntity
import com.example.myweather.domain.util.State
import kotlinx.coroutines.flow.Flow

/**
 * 즐겨찾기 관련 Repository
 * */
//Repository : 여러 데이터 액세스를 추상화한 인터페이스
interface FavoriteRepository {
    val allFavoriteEntity : Flow<List<FavoriteEntity>>
    //관심 지역 추가
    suspend fun insertFavorite(favorite : FavoriteEntity)

    //관심 지역 삭제
    suspend fun removeFavorite(favorite: FavoriteEntity)

    //모든 관심 지역 가져옴
    fun getAllFavorite() : Flow<List<FavoriteEntity>>

    //관심 지역 정보 수정
    suspend fun updateCurrentFavorite(favorite: FavoriteEntity)
}