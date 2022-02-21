package com.example.myweather.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myweather.common.util.ItemDiffUtil
import com.example.myweather.databinding.ItemLocationBinding
import com.example.myweather.model.vworld.Location

//지역 검색 결과 RecyclerView Adapter
class LocationAdapter(
    val onClickItem : (Location) -> Unit
):ListAdapter<Location,LocationAdapter.ViewHolder>(ItemDiffUtil.getInstance().getDiffUtil(Location::class.java)) {
    inner class ViewHolder(private val binding:ItemLocationBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(location:Location){
            binding.locationTextView.text = location.title
            binding.root.setOnClickListener {
                onClickItem(location)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemLocationBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

//    companion object{
//        private val diffUtil = object : DiffUtil.ItemCallback<Location>(){
//            override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
//                return oldItem.id == newItem.id
//            }
//            override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
//                return oldItem.title == newItem.title
//            }
//
//        }
//    }
}