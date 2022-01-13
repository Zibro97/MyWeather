package com.example.myweather.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myweather.R
import com.example.myweather.databinding.ItemHourlyBinding
import com.example.myweather.model.HourlyWeatherModel
import java.text.SimpleDateFormat
import java.util.*

class HourlyAdapter : ListAdapter<HourlyWeatherModel,HourlyAdapter.ViewHolder>(diffUtil) {

    private lateinit var itemHourlyBinding: ItemHourlyBinding
    inner class ViewHolder(private val binding: ItemHourlyBinding) : RecyclerView.ViewHolder(binding.root){
        //날씨 데이터 바인딩
        fun bind(hourly : HourlyWeatherModel){
            binding.weather = hourly.weather.first()
            binding.hourly = hourly
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyAdapter.ViewHolder {
        itemHourlyBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_hourly,parent,false)
        return ViewHolder(itemHourlyBinding)
    }

    override fun onBindViewHolder(holder: HourlyAdapter.ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    /*@BindingAdapter("bindSampleList")
    private fun bindSampleList(recyclerView: RecyclerView,items:List<HourlyWeatherModel>){
        val adpter = recyclerView.adapter as HourlyAdapter
        adpter?.submitList(items)
    }*/

    companion object{
        //시간 별 날씨 아이콘 Glide 통해서 이미지 넣는 함수
        @BindingAdapter("imageFromGlide")
        @JvmStatic
        fun setImageFromGlide(view : ImageView, imageUrl : String){
            val iconUrl = "http://openweathermap.org/img/wn/$imageUrl@2x.png"
            Glide.with(view.context)
                .load(iconUrl)
                .into(view)
        }
        //timeStamp 타입의 시간들을 DateTime으로 변환하는 함수
        @BindingAdapter("timeToDate")
        @JvmStatic
        fun timeToDate(view: TextView, time:Int) {
            val simpleDataFormat = SimpleDateFormat("HH시", Locale.KOREA)
            view.text =  simpleDataFormat.format(time * 1000L)
        }
        //temp_textview 뒤에 °붙이기 위한 함수
        @BindingAdapter("setTempFormat")
        @JvmStatic
        fun setTempFormat(view:TextView,temp:Double){
            view.text = "$temp°"
        }
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