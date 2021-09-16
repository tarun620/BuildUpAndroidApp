package com.example.api.models.responsesAndData.property.propertyResponses


import com.example.api.models.responsesAndData.property.propertyEntities.Eta
import com.example.api.models.responsesAndData.property.propertyEntities.LatestUpdate
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