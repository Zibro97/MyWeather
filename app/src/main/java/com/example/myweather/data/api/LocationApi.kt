package com.example.myweather.data.api

import com.example.myweather.BuildConfig
import com.example.myweather.domain.entity.vworld.VworldLocation
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationApi {
    //지역 검색을 위한 API
    @GET("/req/search?&request=search&size=100&type=district&category=L4&key=${BuildConfig.VWORLD_SEARCH_ADDRESS_API_KEY}")
    suspend fun locationInfo(
        @Query("query") keyword: String
    ): VworldLocation
}