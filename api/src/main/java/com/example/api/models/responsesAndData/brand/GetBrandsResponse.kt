package com.example.api.models.responsesAndData.brand


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetBrandsResponse(
    @Json(name = "brands")
    var brands: List<Brand>?,
    @Json(name = "success")
    var success: Boolean?,
    @Json(name = "error")
    var error: String?
)