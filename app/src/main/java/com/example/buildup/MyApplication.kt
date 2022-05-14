package com.example.buildup

import android.app.Application
import android.util.Log

class MyApplication: Application() {
    companion object {
        private lateinit var myAppInstance: MyApplication

        fun getInstance(): MyApplication {
            return myAppInstance
        }
    }
    private var brandList = ArrayList<String>()
    private var fromRange:Int?=null
    private var toRange:Int?=null
    private var propertyId:String?=null

    init {
        myAppInstance = this
    }
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

    fun setFromRange(fromRangeInt:Int){
        fromRange=fromRangeInt
    }
    fun getFromRange():Int?{
        return fromRange
    }

    fun clearFromRange(){
        fromRange=null
    }

    fun setToRange(toRangeInt:Int){
        toRange=toRangeInt
    }

    fun getToRange():Int?{
        return toRange
    }

    fun clearToRange(){
        toRange=null
    }

    fun addPropertyId(propertyIdData:String){
        propertyId=propertyIdData
//        Log.d("propertyIdAddPropertyId()",propertyId.toString())
//        Log.d("propertyIdDataAddPropertyId()",propertyIdData)

    }
    fun getPropertyId():String?{
//        Log.d("propertyIdGetPropertyId()",propertyId.toString())
        return propertyId
    }

    fun clearPropertyId(){
        propertyId=null
    }


}