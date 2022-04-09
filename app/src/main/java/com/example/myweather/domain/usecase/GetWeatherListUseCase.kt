package com.example.myweather.domain.usecase

import com.example.myweather.domain.repository.WeatherRepository

class GetWeatherListUseCase(
    private val repository : WeatherRepository
) {
    suspend operator fun invoke(idList : String) = repository.getWeatherList(idList)
}