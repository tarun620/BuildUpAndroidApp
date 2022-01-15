package com.example.buildup.extensions

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.icu.text.TimeZoneFormat
import android.widget.TextView
import java.text.ParseException
import java.util.*
import java.util.TimeZone.getTimeZone

@SuppressLint("ConstantLocale")
val isoDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())

@SuppressLint("ConstantLocale")
val appDateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())



var TextView.getReturnValidyDate: String
    set(value) {
//        isoDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
//        isoDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+05:30"));

        val c = Calendar.getInstance()
        try {
            c.time = isoDateFormat.parse(value)
//            c.timeZone=getTimeZone("GMT+05:30")
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        c.add(Calendar.HOUR,5)
        c.add(Calendar.MINUTE,30)
        c.add(Calendar.DAY_OF_MONTH, 7)

        text = appDateFormat.format(c.time)
    }
    get() {
        val c = Calendar.getInstance()
        try {
            c.time = appDateFormat.parse(text.toString())
//            c.timeZone=getTimeZone("GMT+05:30")
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        c.add(Calendar.HOUR,5)
        c.add(Calendar.MINUTE,30)
        c.add(Calendar.DAY_OF_MONTH, 7)
        return isoDateFormat.format(c.time)
    }

var TextView.timeStamp:String
    set(value){
//        val date = isoDateFormat.parse(value)
//        text = appDateFormat.format(date)

        val c = Calendar.getInstance()
        try {
            c.time = isoDateFormat.parse(value)
//            c.timeZone=getTimeZone("GMT+05:30")
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        c.add(Calendar.HOUR,5)
        c.add(Calendar.MINUTE,30)

        text = appDateFormat.format(c.time)
    }

    get() {
//        val date = appDateFormat.parse(text.toString())
//        return isoDateFormat.format(date)

        val c = Calendar.getInstance()
        try {
            c.time = appDateFormat.parse(text.toString())
//            c.timeZone=getTimeZone("GMT+05:30")
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        c.add(Calendar.HOUR,5)
        c.add(Calendar.MINUTE,30)
        return isoDateFormat.format(c.time)
    }


