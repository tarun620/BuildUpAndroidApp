package com.example.api.models.responsesAndData.address


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Location(
    @Json(name = "coordinates")
    var coordinates: List<Double>,
    @Json(name = "type")
    var type: String
)