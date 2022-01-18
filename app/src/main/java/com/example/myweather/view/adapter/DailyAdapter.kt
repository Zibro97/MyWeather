package com.example.myweather.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myweather.databinding.ItemDailyBinding
import com.example.myweather.model.DailyWeatherModel

class DailyAdapter:ListAdapter<DailyWeatherModel,DailyAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(private val binding: ItemDailyBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(item : DailyWeatherModel){
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