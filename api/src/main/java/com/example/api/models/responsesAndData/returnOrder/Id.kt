package com.example.api.models.responsesAndData.returnOrder


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Id(
    @Json(name = "brand")
    var brand: Brand,
    @Json(name = "_id")
    var id: String,
    @Json(name = "image")
    var image: List<String>,
    @Json(name = "name")
    var name: String
)