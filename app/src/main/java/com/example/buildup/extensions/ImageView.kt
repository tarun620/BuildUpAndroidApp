package com.example.buildup.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.buildup.R

fun ImageView.loadImage(uri:String){
    Glide.with(this)
            .load(uri)
            .placeholder(R.drawable.image_27)
            .into(this)
}

fun ImageView.newLoadImage(uri:String){
    Glide.with(this)
        .load(uri)
        .placeholder(R.drawable.basin_mixer_1x)
        .into(this)
}