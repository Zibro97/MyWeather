package com.example.myweather.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myweather.RetrofitClient
import com.example.myweather.model.WeatherDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherViewModel:ViewModel() {
    val service = RetrofitClient.weatherService
    val weatherLiveData:MutableLiveData<WeatherDTO> = MutableLiveData()

    fun getWeather(latitude:Double,longitude:Double){
        service.getWeather(latitude = latitude,longitude = longitude)
            .enqueue(object : Callback<WeatherDTO>{
                //통신 성공 시
                override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
                    if(response.isSuccessful){
                        response.body()?.let {
                            weatherLiveData.value = response.body()
                        }
                    }
                }
                //통신 실패 시
                override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
                    Log.e("getWeather()", t.toString())
                }
            })
    }
}