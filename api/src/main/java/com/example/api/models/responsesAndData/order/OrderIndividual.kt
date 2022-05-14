package com.example.api.models.responsesAndData.order


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OrderIndividual(
    @Json(name = "createdAt")
    var createdAt: String,
    @Json(name = "_id")
    var id: String,
    @Json(name = "payment")
    var payment: Payment?,
    @Json(name = "product")
    var product: Product,
    @Json(name = "packageId")
    var packageId: PackageIdIndivisual,
    @Json(name = "isReturnAvailed")
    val isReturnAvailed:Boolean?,
    @Json(name = "isReturn")
    val isReturn:Boolean?,
    @Json(name="isCancelled")
    val isCancelled : IsCancelledIndividual?,
    @Json(name="cancellationReason")
    val cancellationReason: String?
)