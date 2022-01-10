package com.example.api.models.responsesAndData.returnOrder


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetOrderReturnDetailsResponse(
    @Json(name = "order")
    var order: Order?,
    @Json(name = "reasons")
    var reasons: List<String>?,
    @Json(name = "success")
    var success: Boolean?,
    @Json(name = "error")
    var error: String?
)