package com.example.api.models.responses.property.propertyEntities


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LatestUpdate(
    @Json(name = "createdAt")
    val createdAt: String?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "expenditureId")
    val expenditureId: String?,
    @Json(name = "_id")
    val id: String?,
    @Json(name = "price")
    val price: Int?
)