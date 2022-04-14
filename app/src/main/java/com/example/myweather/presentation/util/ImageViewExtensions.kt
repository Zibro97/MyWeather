package com.example.myweather.presentation.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

fun ImageView.loadGif(url:Int) =
    Glide.with(context)
        .asGif()
        .centerCrop()
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        .into(this)

fun ImageView.loadGifFitCenter(url: Int) =
    Glide.with(context)
        .asGif()
        .fitCenter()
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        .into(this)