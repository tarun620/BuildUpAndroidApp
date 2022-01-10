package com.example.api.models.responsesAndData.order


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Status(
    @Json(name = "_id")
    var id: String,
    @Json(name = "name")
    var name: String,
    @Json(name = "time")
    var time: String
)