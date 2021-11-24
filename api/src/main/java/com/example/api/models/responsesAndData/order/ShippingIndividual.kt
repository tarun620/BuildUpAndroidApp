package com.example.api.models.responsesAndData.order


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ShippingIndividual(
    @Json(name = "address")
    var address: String,
    @Json(name = "city")
    var city: String,
    @Json(name = "customer")
    var customer: String,
    @Json(name = "pincode")
    var pincode: String,
    @Json(name = "state")
    var state: String,
    @Json(name = "tracking")
    var tracking: Tracking
)