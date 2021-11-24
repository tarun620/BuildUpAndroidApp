package com.example.api.models.responsesAndData.order


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Payment(
    @Json(name = "method")
    var method: String,
    @Json(name = "transactionId")
    var transactionId: String
)