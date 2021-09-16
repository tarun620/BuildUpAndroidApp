package com.example.api.models.responsesAndData.expenditure.expenditureResponses


import com.example.api.models.responsesAndData.expenditure.expenditureEntities.ExpenditureAmount
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TotalExpenditureResponse(
    @Json(name = "expenditure")
    val expenditureAmount: ExpenditureAmount?,
    @Json(name = "success")
    val success: Boolean?,
    @Json(name="error")
    val error:String?
)