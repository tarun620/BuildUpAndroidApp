package com.example.api.models.responsesAndData.order

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PaymentData(
    @Json(name = "method")
    val method:String,
    @Json(name = "transactionId")
    val transactionId:String?
)
