package com.example.api.models.responsesAndData.order


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Order(
    @Json(name = "createdAt")
    var createdAt: String,
    @Json(name = "_id")
    var id: String,
    @Json(name = "product")
    var product: Product,
    @Json(name = "packageId")
    var packageId: PackageId,
    @Json(name = "isReturnAvailed")
    var isReturnAvailed:Boolean?,
    @Json(name = "isCancelled")
    var isCancelled:IsCancelled?
)