package com.example.myweather.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.myweather.R
import com.example.myweather.databinding.FragmentWeatherBinding

class WeatherFragment : Fragment() {
    private lateinit var binding : FragmentWeatherBinding
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

        setBackground()
    }

    private fun setBackground(){
        Glide.with(this)
            .asGif()
            .fitCenter()
            .load(R.drawable.ic_rain_background)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(binding.weatherBackground)
    }
}