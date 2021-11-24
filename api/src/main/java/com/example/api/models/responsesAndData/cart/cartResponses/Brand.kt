package com.example.api.models.responsesAndData.cart.cartResponses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Brand(
    @Json(name = "name")
    var name: String
)