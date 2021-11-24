package com.example.api.models.responsesAndData.order

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateOrderData(
    @Json(name="propertyId")
    val propertyId: String,
    @Json(name = "payment")
    val payment:PaymentData,
    @Json(name = "isPersonalUse")
    val isPersonalUse:Boolean?
)
