package com.example.myweather.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myweather.databinding.ItemFavoriteBinding
import com.example.myweather.model.favorite.Favorite

//room에 있는 관심 지역 보여주는 RecyclerView
class FavoriteAdapter(
    val onItemClick : (Favorite) -> Unit
): ListAdapter<Favorite, FavoriteAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(private val binding:ItemFavoriteBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(item: Favorite){
            binding.root.setOnClickListener{
                onItemClick(item)
            }
            binding.itemTitleTextView.text = item.location
            if(item.location != "나의 위치") binding.countryTextView.text = item.location
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