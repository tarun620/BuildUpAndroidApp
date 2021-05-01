package com.example.api.models.responses.property


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Property(
    @Json(name = "completed")
    val completed: Int,
    @Json(name = "eta")
    val eta: Eta,
    @Json(name = "_id")
    val id: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "type")
    val type: String
)