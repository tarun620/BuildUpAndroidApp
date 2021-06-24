package com.example.buildup.extensions

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.util.Log
import android.widget.TextView
import java.util.*
import kotlin.math.log

@SuppressLint("ConstantLocale")
val isoDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())

@SuppressLint("ConstantLocale")
val appDateFormat = SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault())

var TextView.timeStamp: String
    set(value) {
        val date = isoDateFormat.parse(value)
        text = appDateFormat.format(date)
//        val new_date=appDateFormat.format(date)
//        var sub_str=new_date.substring(new_date.length-5 ,new_date.length-3)
//        Log.d("time1",new_date)
//        Log.d("time2",sub_str)
    }
    get() {
        val date = appDateFormat.parse(text.toString())
        return isoDateFormat.format(date)
    }


