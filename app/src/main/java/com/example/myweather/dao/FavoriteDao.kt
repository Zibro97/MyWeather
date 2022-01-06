package com.example.myweather.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myweather.model.Favorite

@Dao
interface FavoriteDao {
    //즐겨찾기 된 지역 리스트 불러오는 쿼리
    @Query("SELECT * FROM favorite")
    fun getAll():List<Favorite>
    //즐겨찾기한 특정 지역 좌표 가져오는 쿼리
    @Query("SELECT * FROM favorite WHERE id == :id")
    fun getFavoriteInfo(id : Int) : Favorite
    //즐겨찾기 추가하는 쿼리
    @Insert
    fun insertFavorite(favorite: Favorite)
    //즐겨찾기 삭제하는 쿼리
    @Query("DELETE FROM favorite WHERE id == :id")
    fun delete(id : Int)
}
