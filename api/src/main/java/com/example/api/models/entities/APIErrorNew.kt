package com.example.api.models.entities


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.lang.reflect.Constructor

@JsonClass(generateAdapter = true)
data class APIErrorNew (
    @Json(name = "error")
    val error: String
)

