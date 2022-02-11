package com.example.myweather.data.api

import com.example.myweather.BuildConfig
import com.example.myweather.model.vworld.Location
import com.example.myweather.model.vworld.VworldLocation
import retrofit2.http.GET
import retrofit2.http.Query

interface FavoriteService {
    @GET("/req/search?&request=search&size=20&type=district&category=L4&key=${BuildConfig.VWORLD_SEARCH_ADDRESS_API_KEY}")
    suspend fun locationInfo(
        @Query("query") query: String
    ): VworldLocation
}