package com.example.api.models.responsesAndData.products.productsResponses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RecentProduct(
    @Json(name = "amount")
    var amount: Int,
    @Json(name = "brand")
    var brand: Brand,
    @Json(name = "_id")
    var id: String,
    @Json(name = "image")
    var image: List<String>,
    @Json(name = "mrp")
    var mrp: Int,
    @Json(name = "name")
    var name: String
)