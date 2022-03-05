package com.example.myweather.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.myweather.R
import com.example.myweather.databinding.ItemWeatherBinding
import com.example.myweather.model.favorite.Favorite
import com.example.myweather.model.weather.WeatherDTO
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

//ViewPager Adapter
class WeatherAdapter: ListAdapter<Favorite,WeatherAdapter.ViewHolder>(diffUtil){
    var weather : WeatherDTO? = null
    private val hourlyAdapter = HourlyAdapter()
    private val dailyAdapter = DailyAdapter()
    inner class ViewHolder(private val binding:ItemWeatherBinding):RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(favorite: Favorite)= with(binding){
            //매 시각 날씨 예보 RecyclerView
            cityTv.text = favorite.location
            hourlyRv.layoutManager = LinearLayoutManager(root.context,LinearLayoutManager.HORIZONTAL,false)
            hourlyRv.adapter = hourlyAdapter
            dailyRv.layoutManager = LinearLayoutManager(root.context,LinearLayoutManager.VERTICAL,false)
            dailyRv.adapter = dailyAdapter

            weather ?.let { weather->
                hourlyAdapter.submitList(weather.hourly)
                //일주일간의 날씨 예보 RecyclerView
                dailyAdapter.submitList(weather.daily)
                tempTv.text = "${weather.current.temp.roundToInt()}°"
                weatherDestinationTv.text = weather.current.weather.first().main.label
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
                tempDestinationTv.text = "${weather.current.temp.roundToInt()}°|${weather.current.weather.first().main.label}"
                when(weather.current.weather.first().main.label){
                    "눈" -> {
                        snowRainTextView.text = root.context.resources.getString(R.string.snow_text)
                        realSnowRainTextView.text =
                            if(weather.current.snow ==null) "0mm"
                            else "${weather.current.snow.h}mm"
                    }
                    else -> {
                        snowRainTextView.text = root.context.resources.getString(R.string.rain_text)
                        realSnowRainTextView.text =
                            if(weather.current.rain ==null) "0mm"
                            else "${weather.current.rain.h}mm"
                        }
                    }

                    Glide.with(root)
                        .asGif()
                        .fitCenter()
                        .load(weather.current.weather.first().main.background)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .into(weatherBackground)
                }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemWeatherBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<Favorite>(){
            override fun areItemsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
                return oldItem.location == newItem.location
            }
        }
    }
}