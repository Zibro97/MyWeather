package com.example.myweather.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myweather.databinding.ItemWeatherBinding
import com.example.myweather.model.WeatherDTO
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


class WeatherAdapter(
    private val weathers : List<WeatherDTO>
): RecyclerView.Adapter<WeatherAdapter.ViewHolder>(){
    inner class ViewHolder(private val binding:ItemWeatherBinding):RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(weather: WeatherDTO){
            with(binding){
                tempTv.text = "${weather.current.temp.roundToInt()}°"
                weatherDestinationTv.text = weather.current.weather.first().description
                maxTempTv.text = "최고:${weather.daily.first().temp.maxTemp.roundToInt()}°"
                minTempTv.text = "최저:${weather.daily.first().temp.minTemp.roundToInt()}°"
                val simpleDataFormat = SimpleDateFormat("HH:mm", Locale.KOREA)
                sunriseTimeTextView.text = simpleDataFormat.format(weather.current.sunrise * 1000L)
                sunsetTimeTextView.text = "일몰: ${simpleDataFormat.format(weather.current.sunset * 1000L)}"
                windTimeTextView.text = "${weather.current.windSpeed.roundToInt()}m/s"
                realFeelTempTextView.text = "${weather.current.feelsLike.roundToInt()}°"
                realHumidityTextView.text = "${weather.current.humidity}%"
                dewPointTextView.text = "현재 이슬점이 ${weather.current.dewPoint.roundToInt()}°입니다."
                realVisibilityTextView.text = "${weather.current.visibility/1000}KM"
                tempDestinationTv.text = "${weather.current.temp.roundToInt()}°|${weather.current.weather.first().description}"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemWeatherBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(weathers[position])
    }

    override fun getItemCount(): Int = weathers.size
}