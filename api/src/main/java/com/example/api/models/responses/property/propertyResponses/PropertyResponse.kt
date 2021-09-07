package com.example.api.models.responses.property.propertyResponses


import com.example.api.models.responses.property.propertyEntities.Eta
import com.example.api.models.responses.property.propertyEntities.LatestUpdate
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PropertyResponse(
    @Json(name = "completed")
    val completed: Int?,
    @Json(name = "eta")
    val eta: Eta?,
    @Json(name = "_id")
    val id: String?,
    @Json(name = "latestUpdate")
    val latestUpdate: LatestUpdate?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "type")
    val type: String?
)