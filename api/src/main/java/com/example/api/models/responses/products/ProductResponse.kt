package com.example.api.models.responses.products


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductResponse(
    @Json(name = "product")
    val product: Product,
    @Json(name = "success")
    val success: Boolean
)