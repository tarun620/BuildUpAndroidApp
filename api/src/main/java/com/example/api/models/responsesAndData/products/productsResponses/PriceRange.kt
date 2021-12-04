package com.example.api.models.responsesAndData.products.productsResponses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PriceRange(
    @Json(name = "from")
    var from: Int?,
    @Json(name = "to")
    var to: Int?
)