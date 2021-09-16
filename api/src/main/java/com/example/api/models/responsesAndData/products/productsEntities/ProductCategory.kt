package com.example.api.models.responsesAndData.products.productsEntities


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductCategory(
    @Json(name = "_id")
    val id: String,
    @Json(name = "image")
    val image: String,
    @Json(name = "name")
    val name: String
)