package com.example.api.models.responses.property


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SinglePropertyResponse(
    @Json(name = "property")
    val property: Property?,
    @Json(name = "success")
    val success: Boolean?,
    @Json(name = "error")
    val error:String?
)