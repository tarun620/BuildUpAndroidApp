package com.example.api.models.responsesAndData.rating


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Ratings(
    @Json(name = "avg")
    var avg: Double,
    @Json(name = "count")
    var count: List<Int>,
    @Json(name = "total")
    var total: Int
)