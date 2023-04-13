package com.example.api.models.responsesAndData.appData

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Details(
    @Json(name = "email")
    var email:String,
    @Json(name = "mobileNo")
    var mobileNo:String
)
