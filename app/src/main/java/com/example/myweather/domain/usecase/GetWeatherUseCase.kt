package com.example.myweather.domain.usecase

import com.example.myweather.domain.repository.WeatherRepository

class GetWeatherUseCase(
    private val repository:WeatherRepository
) {
    suspend operator fun invoke(latitude : Double, longitude : Double) = repository.getWeather(latitude,longitude)
}