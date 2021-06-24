package com.example.api.models.responses.expenditure


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TotalExpenditureResponse(
        @Json(name = "expenditure")
    val expenditureAmount: ExpenditureAmount,
        @Json(name = "success")
    val success: Boolean
)