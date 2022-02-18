package com.example.myweather.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myweather.databinding.ItemDailyBinding
import com.example.myweather.model.weather.DailyWeatherModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class DailyAdapter:ListAdapter<DailyWeatherModel,DailyAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(private val binding: ItemDailyBinding):RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(item : DailyWeatherModel){
            val simpleDataFormat = SimpleDateFormat("E", Locale.KOREA)
            binding.dailyItemDayTv.text = simpleDataFormat.format(item.dt * 1000L)
            binding.dailyItemMaxTempTv.text = "${item.temp.maxTemp.roundToInt()}°"
            binding.dailyItemMinTempTv.text ="${item.temp.minTemp.roundToInt()}°"
            binding.dailySeekbar.isEnabled = false
            binding.dailyItemWeatherIconIv.text = item.weather.first().main.emoji
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyAdapter.ViewHolder {
        return ViewHolder(ItemDailyBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: DailyAdapter.ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
    companion object{
        private val diffUtil = object : DiffUtil.ItemCallback<DailyWeatherModel>(){
            override fun areItemsTheSame(oldItem: DailyWeatherModel, newItem: DailyWeatherModel): Boolean {
                return oldItem==newItem
            }

            override fun areContentsTheSame(oldItem: DailyWeatherModel, newItem: DailyWeatherModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}