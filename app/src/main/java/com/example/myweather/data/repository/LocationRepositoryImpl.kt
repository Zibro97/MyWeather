package com.example.myweather.data.repository

import com.example.myweather.data.api.LocationApi
import com.example.myweather.di.IODispatcher
import com.example.myweather.domain.entity.vworld.VworldLocation
import com.example.myweather.domain.repository.LocationRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val locationApi: LocationApi,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
):LocationRepository {
    override suspend fun getLocationInfo(keyword: String): VworldLocation = withContext(ioDispatcher) {
        return@withContext locationApi.locationInfo(keyword)
    }
}