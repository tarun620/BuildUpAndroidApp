package com.example.api.models.responsesAndData.address


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Property(
    @Json(name = "address")
    var address: Address,
    @Json(name = "name")
    var propertyName:String,
    @Json(name = "_id")
    var id: String,
    @Json(name = "isUnderConstruction")
    var isUnderConstruction: Boolean,
    @Json(name = "mobileNo")
    var mobileNo: Long,
    @Json(name = "type")
    var type: String
)