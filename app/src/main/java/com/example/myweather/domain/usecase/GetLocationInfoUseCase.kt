package com.example.myweather.domain.usecase

import com.example.myweather.domain.repository.LocationRepository

class GetLocationInfoUseCase(
    private val repository: LocationRepository
) {
    suspend operator fun invoke(keyword : String) = repository.getLocationInfo(keyword)
}