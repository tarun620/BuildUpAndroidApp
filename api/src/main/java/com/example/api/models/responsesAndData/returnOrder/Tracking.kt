package com.example.api.models.responsesAndData.returnOrder


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Tracking(
    @Json(name = "company")
    var company: String,
    @Json(name = "status")
    var status: List<Status>
)