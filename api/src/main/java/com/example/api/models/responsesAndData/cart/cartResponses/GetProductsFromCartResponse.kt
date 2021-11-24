package com.example.api.models.responsesAndData.cart.cartResponses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetProductsFromCartResponse(
    @Json(name = "items")
    var items: List<Item>?,
    @Json(name = "success")
    var success: Boolean?,
    @Json(name="error")
    var error:String?
)