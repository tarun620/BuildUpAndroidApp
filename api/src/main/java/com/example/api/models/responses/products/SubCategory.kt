package com.example.api.models.responses.products


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SubCategory(
    @Json(name = "_id")
    val id: String,
    @Json(name = "image")
    val image: String,
    @Json(name = "name")
    val name: String
)