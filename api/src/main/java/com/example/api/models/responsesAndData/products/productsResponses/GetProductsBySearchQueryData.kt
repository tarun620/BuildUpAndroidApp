package com.example.api.models.responsesAndData.products.productsResponses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetProductsBySearchQueryData(
    @Json(name = "filters")
    var filters: Filters?,
    @Json(name = "sort")
    var sort: String?
)