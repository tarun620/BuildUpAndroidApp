package com.example.api.models.responsesAndData.rating

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AddProductRatingDataCount(
    @Json(name = "count")
    var count:Int
)
