package com.example.myweather.domain.repository

import com.example.myweather.domain.entity.vworld.VworldLocation

/**
 * 지역 정보를 가져오는 Repository
 * */
interface LocationRepository {
    //지역 정보를 가져오는 메서드
    suspend fun getLocationInfo(keyword: String) : VworldLocation
}