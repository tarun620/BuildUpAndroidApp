package com.example.api.models.responsesAndData.cart.cartResponses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetCostDeliveryDetailsResponse(
    @Json(name = "cost")
    var cost: Cost?,
    @Json(name = "estimatedDelivery")
    var estimatedDelivery: String?,
    @Json(name = "success")
    var success: Boolean?,
    @Json(name = "error")
    var error:String?
)