package com.example.api.models.responsesAndData.property.propertyEntities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AddPropertyData(
    @Json(name = "name")
    val name:String,
    @Json(name="type")
    val type:String,
    @Json(name="houseNo")
    val houseNo:String,
    @Json(name="colony")
    val colony:String,
    @Json(name = "city")
    val city:String,
    @Json(name="state")
    val state:String,
    @Json(name = "pincode")
    val pincode:Int,
    @Json(name = "coordinates")
    val coordinates:List<Double>?
)