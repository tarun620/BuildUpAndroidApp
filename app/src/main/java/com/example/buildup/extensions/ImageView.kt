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
        .into(this)
}

fun ImageView.loadBrandImage(uri:String){
    Glide.with(this)
        .load(uri)
        .placeholder(R.drawable.jaquar_logo)
        .into(this)
}

fun ImageView.loadCategoryImage(uri:String?){
    Glide.with(this)
        .load(uri)
        .placeholder(R.drawable.icon_jaquar_logo)
        .into(this)
}

fun ImageView.loadHomeBanner(uri:String?){
    Glide.with(this)
        .load(uri)
        .placeholder(R.color.banner_bg)
        .into(this)
}

