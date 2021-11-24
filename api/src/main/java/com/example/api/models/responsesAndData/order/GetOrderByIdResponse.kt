package com.example.api.models.responsesAndData.order


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetOrderByIdResponse(
    @Json(name = "order")
    var order: OrderIndividual?,
    @Json(name = "success")
    var success: Boolean?,
    @Json(name = "error")
    var error:String?
)