package com.example.api.models.responsesAndData.returnOrder

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PackageId(
    @Json(name = "shipping")
    var shipping: Shipping,
    @Json(name = "payment")
    var payment: Payment,
)
