package com.example.myweather.domain.usecase

import com.example.myweather.domain.repository.WeatherRepository

class GetLocationIdUseCase(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(latitude:Double,longitude:Double) = repository.getLocationId(latitude,longitude)
}