package com.example.myweather.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myweather.databinding.ItemFavoriteBinding
import com.example.myweather.model.Favorite
import com.example.myweather.viewmodel.WeatherViewModel

class FavoriteAdapter: ListAdapter<Favorite, FavoriteAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(private val binding:ItemFavoriteBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(item:Favorite){
            binding.itemTitleTextView.text = item.location
            binding.countryTextView.text = item.location
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
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