package com.example.myweather.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myweather.api.WeatherService
import com.example.myweather.databinding.ActivityMainBinding
import com.example.myweather.model.WeatherDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    //바인딩 클래스
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val weatherService = retrofit.create(WeatherService::class.java)

        weatherService.getWeather(37.41664127743908, 126.88485337117136)
            .enqueue(object : Callback<WeatherDTO>{
                override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
                    // TODO: 성공처리
                    if(response.isSuccessful.not()){
                        Log.e(TAG, "Response is Empty")
                        return
                    }
                    response.body()?.let {
                        Log.d(TAG, it.toString())
                    }
                }

                override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
                    // TODO: 실패처리
                    Log.e(TAG, t.toString() )
                }

            })
    }
    companion object{
        private const val TAG = "MainActivity"
    }
}