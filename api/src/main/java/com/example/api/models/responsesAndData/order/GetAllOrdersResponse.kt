package com.example.api.models.responsesAndData.order


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetAllOrdersResponse(
    @Json(name = "hasNext")
    var hasNext: Boolean?,
    @Json(name = "orders")
    var orders: MutableList<Order>?,
    @Json(name = "success")
    var success: Boolean?,
    @Json(name = "error")
    var error:String?
)