package com.example.myweather.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.myweather.databinding.ItemFavoriteBinding
import com.example.myweather.model.favorite.Favorite
import com.example.myweather.model.weather.WeatherDTO
import java.text.SimpleDateFormat
import kotlin.math.roundToInt

//room에 있는 관심 지역 보여주는 RecyclerView
class FavoriteAdapter(
    val onItemClick : (Favorite) -> Unit,
    val weatherOfItem : (Favorite) -> Unit
): ListAdapter<Favorite, FavoriteAdapter.ViewHolder>(diffUtil) {
    var weather : WeatherDTO? = null
    inner class ViewHolder(private val binding:ItemFavoriteBinding):RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SimpleDateFormat", "SetTextI18n")
        fun bind(item: Favorite) = with(binding){
            val currentTime = SimpleDateFormat("HH:mm")
            root.setOnClickListener{
                onItemClick(item)
            }
            itemTitleTextView.text = item.location
            weather?.let { weather->
                descriptionTextView.text = weather.current.weather.first().description
                currentTempTextView.text = "${weather.current.temp.roundToInt()}°"
                maxMinTempTextView.text ="최고:${weather.daily.first().temp.maxTemp.roundToInt()}° 최저:${weather.daily.first().temp.minTemp.roundToInt()}°"

                Glide.with(root)
                    .asGif()
                    .fitCenter()
                    .load(weather.current.weather.first().main.background)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(itemBackgroundImageView)
            }
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
        weatherOfItem(currentList[position])
        holder.bind(currentList[position])
    }

    companion object{
        private val diffUtil = object : DiffUtil.ItemCallback<Favorite>() {
            override fun areItemsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
                return oldItem == newItem
            }
        }
    }
}