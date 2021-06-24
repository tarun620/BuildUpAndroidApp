package com.example.api.models.responses.expenditure


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ExpenditureAmount(
    @Json(name = "totalPaid")
    val totalPaid: Int,
    @Json(name = "totalReceived")
    val totalReceived: Int
)