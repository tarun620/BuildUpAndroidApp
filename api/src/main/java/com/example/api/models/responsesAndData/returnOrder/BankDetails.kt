package com.example.api.models.responsesAndData.returnOrder


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BankDetails(
    @Json(name = "accountNumber")
    var accountNumber: Long,
    @Json(name = "fullName")
    var fullName: String,
    @Json(name = "ifscCode")
    var ifscCode: String
)