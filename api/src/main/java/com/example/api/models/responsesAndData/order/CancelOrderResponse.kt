package com.example.api.models.responsesAndData.order

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CancelOrderResponse(
    @Json(name = "message")
    val message: String?,
    @Json(name = "success")
    val success: Boolean?,
    @Json(name="ordersInPackage")
    var ordersInPackage:Int?,
    @Json(name="error")
    var error:String?
)