package com.example.myweather.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myweather.databinding.ItemHourlyBinding
import com.example.myweather.model.HourlyWeatherModel

class HourlyAdapter : ListAdapter<HourlyWeatherModel,HourlyAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemHourlyBinding) : RecyclerView.ViewHolder(binding.root){
        //날씨 데이터 바인딩
        fun bind(hourly : HourlyWeatherModel){
            binding.datetimeHourlyWeather.text = hourly.dt.toString()
            binding.tempHourlyWeather.text = hourly.temp.toString()
            Glide.with(binding.iconHourlyWeather)
                .load(hourly.weather.first().icon)
                .into(binding.iconHourlyWeather)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyAdapter.ViewHolder {
        return ViewHolder(ItemHourlyBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: HourlyAdapter.ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<HourlyWeatherModel>(){
            //이미 있는 Item인지 체크
            override fun areItemsTheSame(oldItem: HourlyWeatherModel, newItem: HourlyWeatherModel): Boolean {
                return oldItem == newItem
            }
            //같은 내용의 Item이 있는지 체크
            override fun areContentsTheSame(oldItem: HourlyWeatherModel, newItem: HourlyWeatherModel): Boolean {
                return oldItem.dt == newItem.dt
            }

        }
    }

}