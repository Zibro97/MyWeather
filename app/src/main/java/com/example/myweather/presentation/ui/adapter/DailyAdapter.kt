package com.example.myweather.presentation.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myweather.databinding.ItemDailyBinding
import com.example.myweather.domain.entity.weather.DailyWeatherModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class DailyAdapter:ListAdapter<DailyWeatherModel, DailyAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(private val binding: ItemDailyBinding):RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(item : DailyWeatherModel) = with(binding){
            val simpleDataFormat = SimpleDateFormat("E", Locale.KOREA)
            dailyItemDayTv.text = simpleDataFormat.format(item.dt * 1000L)
            dailyItemMaxTempTv.text = "${item.temp.maxTemp.roundToInt()}°"
            dailyItemMinTempTv.text ="${item.temp.minTemp.roundToInt()}°"
            dailySeekbar.isEnabled = false
            dailyItemWeatherIconIv.text = item.weather.first().main.emoji
            dailySeekbar.setValues(item.temp.minTemp.toFloat(),item.temp.maxTemp.toFloat())
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemDailyBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
    companion object{
        private val diffUtil = object : DiffUtil.ItemCallback<DailyWeatherModel>(){
            override fun areItemsTheSame(oldItem: DailyWeatherModel, newItem: DailyWeatherModel): Boolean {
                return oldItem==newItem
            }

            override fun areContentsTheSame(oldItem: DailyWeatherModel, newItem: DailyWeatherModel): Boolean {
                return oldItem.temp == newItem.temp ||oldItem.weather ==newItem.weather
            }
        }
    }
}