package com.example.api.models.responses.updates


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WorkCategoryId(
    @Json(name = "_id")
    var id: String,
    @Json(name = "image")
    var image: String,
    @Json(name = "name")
    var name: String
)