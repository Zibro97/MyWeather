package com.example.myweather.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.myweather.databinding.ItemHourlyBinding
import com.example.myweather.model.weather.HourlyWeatherModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class HourlyAdapter : ListAdapter<HourlyWeatherModel,HourlyAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(private val binding: ItemHourlyBinding) : RecyclerView.ViewHolder(binding.root){
        //날씨 데이터 바인딩
        @SuppressLint("SetTextI18n")
        fun bind(hourly : HourlyWeatherModel){
            with(binding){
                val simpleDataFormat = SimpleDateFormat("HH시", Locale.KOREA)
                datetimeHourlyWeather.text =simpleDataFormat.format(hourly.dt * 1000L)
                tempHourlyWeather.text = "${hourly.temp.roundToInt()}°"
                iconHourlyWeather.text = hourly.weather.first().main.emoji
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyAdapter.ViewHolder {
        return ViewHolder(ItemHourlyBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }
    override fun onBindViewHolder(holder: HourlyAdapter.ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
    companion object{
        private val diffUtil = object : DiffUtil.ItemCallback<HourlyWeatherModel>(){
            //이미 있는 Item인지 체크
            override fun areItemsTheSame(oldItem: HourlyWeatherModel, newItem: HourlyWeatherModel): Boolean {
                return oldItem == newItem
            }
            //같은 내용의 Item이 있는지 체크
            override fun areContentsTheSame(oldItem: HourlyWeatherModel, newItem: HourlyWeatherModel): Boolean {
                return oldItem == newItem
            }

        }
    }
}