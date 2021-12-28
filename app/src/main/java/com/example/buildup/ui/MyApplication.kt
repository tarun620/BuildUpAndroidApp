package com.example.buildup.ui

import android.app.Application
import java.util.*
import kotlin.collections.ArrayList

class MyApplication: Application() {
    private var brandList = ArrayList<String>()

    fun getList():ArrayList<String>{
        return brandList
    }

    fun addElement(brandId:String){
        brandList.add(brandId)
    }
    fun removeElement(brandId: String){
        if(brandList.contains(brandId))
            brandList.remove(brandId)
    }
    fun clearList(){
        brandList.clear()
    }
}