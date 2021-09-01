package com.example.api.models.entities


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class APIError(
    @Json(name = "error")
    val error: String
)