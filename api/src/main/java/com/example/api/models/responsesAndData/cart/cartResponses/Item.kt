package com.example.api.models.responsesAndData.cart.cartResponses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Item(
    @Json(name = "productId")
    var product: Product,
    @Json(name = "quantity")
    var quantity: Int
)