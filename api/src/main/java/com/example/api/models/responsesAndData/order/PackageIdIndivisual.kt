package com.example.api.models.responsesAndData.order

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PackageIdIndivisual(
    @Json(name = "shipping")
    var shipping: ShippingIndividual
)
