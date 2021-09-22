package com.example.api.models.requests

import com.example.api.models.entities.SignupMobileOTPData
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SignupMobileOTPRequest(
    @Json(name = "")
    val signupMobileOTPData: SignupMobileOTPData
)