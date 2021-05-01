package com.example.api.models.responses.updates


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UpdatesResponse(
    @Json(name = "success")
    val success: Boolean,
    @Json(name = "hasNext")
    val hasNext:Boolean,
    @Json(name = "updates")
    val updates: List<Update>
)