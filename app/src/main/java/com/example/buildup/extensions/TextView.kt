package com.example.buildup.extensions

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.widget.TextView
import java.text.ParseException
import java.util.*

@SuppressLint("ConstantLocale")
val isoDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())

@SuppressLint("ConstantLocale")
val appDateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())



var TextView.getReturnValidyDate: String
    set(value) {
        val c = Calendar.getInstance()
        try {
            c.time = isoDateFormat.parse(value)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        c.add(Calendar.DAY_OF_MONTH, 7)

        text = appDateFormat.format(c.time)
    }
    get() {
        val c = Calendar.getInstance()
        try {
            c.time = appDateFormat.parse(text.toString())
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        c.add(Calendar.DAY_OF_MONTH, 7)
        return isoDateFormat.format(c.time)
    }

var TextView.timeStamp:String
    set(value){
        val date = isoDateFormat.parse(value)
        text = appDateFormat.format(date)
    }

    get() {
        val date = appDateFormat.parse(text.toString())
        return isoDateFormat.format(date)
    }


