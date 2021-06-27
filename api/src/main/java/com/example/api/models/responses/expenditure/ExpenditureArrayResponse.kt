package com.example.api.models.responses.expenditure


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ExpenditureArrayResponse(
        @Json(name = "expenditures")
    val expenditures: List<Expenditure>?,
        @Json(name = "hasNext")
    val hasNext: Boolean?,
        @Json(name = "success")
    val success: Boolean?,
        @Json(name="error")
    val error:String?
)