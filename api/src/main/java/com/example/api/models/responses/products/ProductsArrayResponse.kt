package com.example.api.models.responses.products


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductsArrayResponse(
    @Json(name = "hasNext")
    val hasNext: Boolean,
    @Json(name = "products")
    val products: List<Products>,
    @Json(name = "success")
    val success: Boolean
)