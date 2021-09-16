package com.example.api.models.responsesAndData.loginSignup.loginSignupEntities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class sendOTPMobile(
    @Json(name = "mobileNo")
    val mobileNo: String,
    @Json(name = "otp")
    val otp: String

)
