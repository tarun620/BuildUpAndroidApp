package com.example.api.models.responsesAndData.returnOrder


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Order(
    @Json(name = "createdAt")
    var createdAt: String,
    @Json(name = "_id")
    var id: String,
    @Json(name = "payment")
    var payment: Payment,
    @Json(name = "product")
    var product: Product,
    @Json(name = "propertyId")
    var propertyId: String,
    @Json(name = "packageId")
    var packageId: PackageId,
    @Json(name = "userId")
    var userId: String
)