package com.example.api.models.responsesAndData.order


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Tracking(
    @Json(name ="awbNo")
    var awbNo:String?,
    @Json(name="company")
    var company:String?,
    @Json(name = "estimatedDelivery")
    var estimatedDelivery: String?,
    @Json(name = "status")
    var status: List<Status>?
)