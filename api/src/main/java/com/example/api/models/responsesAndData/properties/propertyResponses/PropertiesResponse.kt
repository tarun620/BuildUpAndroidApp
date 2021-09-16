package com.example.api.models.responsesAndData.property.propertyResponses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PropertiesResponse(
    @Json(name = "success")
    val success: Boolean?,

    @Json(name = "properties")
    val properties: List<PropertyResponse>?,

    @Json(name = "error")
    val error : String?

)