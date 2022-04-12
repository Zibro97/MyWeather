package com.example.myweather.data.repository

import com.example.myweather.data.api.WeatherApi
import com.example.myweather.domain.entity.favoriteweather.FavoriteWeatherModel
import com.example.myweather.domain.entity.favoriteweather.LocationIdModel
import com.example.myweather.domain.entity.weather.WeatherDTO
import com.example.myweather.domain.repository.WeatherRepository
import com.example.myweather.domain.util.State
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class WeatherRepositoryImpl(
    private val weatherApi: WeatherApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : WeatherRepository {
    override suspend fun getWeather(latitude: Double, longitude: Double): WeatherDTO =
        withContext(ioDispatcher){
            weatherApi.weatherInfo(latitude,longitude)
        }

    override suspend fun getWeatherList(idList: String): FavoriteWeatherModel =
        withContext(ioDispatcher){
            weatherApi.locations(idList)
        }

    override suspend fun getLocationId(latitude: Double, longitude: Double): LocationIdModel =
        withContext(ioDispatcher){
            weatherApi.getLocationId(latitude,longitude)
        }
}