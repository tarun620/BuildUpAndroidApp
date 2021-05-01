package com.example.api.models.responses.property


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Eta(
    @Json(name = "unit")
    val unit: String?,
    @Json(name = "value")
    val value: Int?
)