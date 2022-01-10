package com.example.api.models.responsesAndData.returnOrder


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlaceOrderReturnRequestData(
    @Json(name = "additionalReason")
    var additionalReason: String?,
    @Json(name = "bankDetails")
    var bankDetails: BankDetails,
    @Json(name = "quantity")
    var quantity: Int,
    @Json(name = "reason")
    var reason: String
)