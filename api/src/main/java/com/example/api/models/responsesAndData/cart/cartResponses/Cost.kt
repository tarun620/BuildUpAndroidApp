package com.example.api.models.responsesAndData.cart.cartResponses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Cost(
    @Json(name = "shipping")
    var shipping: Int?,
    @Json(name = "subtotal")
    var subtotal: Int,
    @Json(name = "total")
    var total: Int
)