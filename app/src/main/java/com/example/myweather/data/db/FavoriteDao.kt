package com.example.myweather.data.db

import androidx.room.*
import com.example.myweather.domain.entity.favorite.FavoriteEntity

@Dao
interface FavoriteDao {
    //즐겨찾기 된 지역 리스트 불러오는 쿼리
    @Query("SELECT * FROM FavoriteEntity")
    suspend fun getAllFavorite():List<FavoriteEntity>
    //즐겨찾기 추가하는 쿼리
    @Insert
    suspend fun insertFavorite(favorite: FavoriteEntity)
    //즐겨찾기 삭제하는 쿼리
    @Delete
    suspend fun removeFavorite(favorite: FavoriteEntity)
    //현재위치가 등록되어있는지 확인하는 쿼리
    @Update
    suspend fun updateCurrentFavorite(favoriteEntity: FavoriteEntity)

    //추가 후 모든 관심지역 조회
    @Transaction
    suspend fun setFavorite(favorite: FavoriteEntity){
        insertFavorite(favorite)
        getAllFavorite()
    }

    //삭제 후 모든 관심지역 조회
    @Transaction
    suspend fun deleteFavorite(favorite: FavoriteEntity){
        removeFavorite(favorite)
        getAllFavorite()
    }
}
