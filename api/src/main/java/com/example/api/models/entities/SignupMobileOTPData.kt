package com.example.api.models.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SignupMobileOTPData(
    @Json(name = "mobileNo")
    val mobileNo: String,
    @Json(name = "otp")
    val otp: String

)
