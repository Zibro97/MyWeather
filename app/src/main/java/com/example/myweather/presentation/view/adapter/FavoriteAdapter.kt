package com.example.myweather.presentation.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.myweather.databinding.ItemFavoriteBinding
import com.example.myweather.domain.entity.favorite.FavoriteEntity
import com.example.myweather.domain.entity.favoriteweather.FavoriteWeatherInfo
import java.text.SimpleDateFormat
import kotlin.math.roundToInt

//room에 있는 관심 지역 보여주는 RecyclerView
class FavoriteAdapter(
    val onItemClick : (Int) -> Unit,
): ListAdapter<FavoriteEntity, FavoriteAdapter.ViewHolder>(diffUtil) {
    var weatherList = listOf<FavoriteWeatherInfo>()
    inner class ViewHolder(private val binding:ItemFavoriteBinding):RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SimpleDateFormat", "SetTextI18n")
        fun bind(item: FavoriteEntity, weatherInfo: FavoriteWeatherInfo, position: Int) = with(binding){
            val currentTime = SimpleDateFormat("HH:mm")
            root.setOnClickListener{
                onItemClick(position)
            }
            itemTitleTextView.text = item.location
            descriptionTextView.text = weatherInfo.weather.first().main.label
            currentTempTextView.text = "${weatherInfo.main.temp.roundToInt()}°"
            maxMinTempTextView.text ="최고:${weatherInfo.main.max.roundToInt()}° 최저:${weatherInfo.main.min.roundToInt()}°"

            Glide.with(root)
                .asGif()
                .centerCrop()
                .load(weatherInfo.weather.first().main.background)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(itemBackgroundImageView)
            when(item.location){
                "나의 위치" -> {
                    countryTextView.text = "광명시"
                }
                else -> {
                    countryTextView.text = currentTime.format(System.currentTimeMillis())
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position],weatherList[position],position)
    }

    companion object{
        private val diffUtil = object : DiffUtil.ItemCallback<FavoriteEntity>() {
            override fun areItemsTheSame(oldItem: FavoriteEntity, newItem: FavoriteEntity): Boolean {
                return oldItem.location == newItem.location
            }
            override fun areContentsTheSame(oldItem: FavoriteEntity, newItem: FavoriteEntity): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}