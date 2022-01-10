package com.example.myweather.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.myweather.R
import com.example.myweather.databinding.FragmentWeatherBinding
import com.example.myweather.model.HourlyWeatherModel
import com.example.myweather.view.adapter.HourlyAdapter

class WeatherFragment : Fragment() {
    private lateinit var binding : FragmentWeatherBinding
    private lateinit var hourlyAdapter : HourlyAdapter
    private val hourlyList = mutableListOf<HourlyWeatherModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_weather, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindingRecyclerView()
        setBackground()
    }

    private fun bindingRecyclerView() {
        hourlyAdapter = HourlyAdapter()
        //RecyclerView가 어떻게 그려질 것인지 정의
        binding.hourlyRv.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        //RecyclerView Adapter 연결
        binding.hourlyRv.adapter = hourlyAdapter
    }

    //날씨에 따라 배경 gif를 설정
    private fun setBackground(){
        //배경 이미지를 띄우고자 Glide 라이브러리 사용
        //.asGif() : GIF를 로딩하고자 할때 사용하는 함수
        //.with() : View,Fragment 혹은 Activity로 부터 Context 가져오는 함수
        //.load() : 이미지를 로드하는 함수. 다양한 방법으로 이미지를 불러올 수 있음(Bitmap,Drawable,String,Uri,File,ByteArray).
        //.into() : 이미지를 보여줄 View를 지정하는 합수
        //.diskCacheStrategy(): 디스크에 캐싱하지 않으려면 DiskCacheStrategy.NONE 사용
        //.fitCenter() : 실제 이미지가 이미지뷰의 사이즈와 다를 때, 이미지와 이미지뷰의 중간을 맞춰서 이미지 크기를 스케일링하는 함수
        Glide.with(this)
            .asGif()
            .fitCenter()
            .load(R.drawable.ic_rain_background)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(binding.weatherBackground)
    }
}