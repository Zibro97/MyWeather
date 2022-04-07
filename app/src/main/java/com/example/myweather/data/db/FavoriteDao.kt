package com.example.myweather.data.db

import androidx.room.*
import com.example.myweather.domain.entity.favorite.FavoriteEntity

@Dao
interface FavoriteDao {
    //즐겨찾기 된 지역 리스트 불러오는 쿼리
    @Query("SELECT * FROM FavoriteEntity")
    suspend fun getAll():List<FavoriteEntity>
    //즐겨찾기한 특정 지역 좌표 가져오는 쿼리
    @Query("SELECT * FROM FavoriteEntity WHERE id == :id")
    suspend fun getFavoriteInfo(id : Int) : FavoriteEntity
    //즐겨찾기 추가하는 쿼리
    @Insert
    suspend fun insertFavorite(favoriteEntity: FavoriteEntity)
    //즐겨찾기 삭제하는 쿼리
    @Delete
    suspend fun delete(favoriteEntity: FavoriteEntity)
    //현재위치가 등록되어있는지 확인하는 쿼리
    @Update
    suspend fun updateCurrentFavorite(favoriteEntity: FavoriteEntity)
    //컬럼 개수 확인 하는 쿼리
    @Query("SELECT COUNT(*) FROM FavoriteEntity")
    suspend fun favoriteCnt() : Int
}
