package com.example.api.models.requests

import com.example.api.models.entities.SignupMobileData
import com.example.api.models.entities.SignupMobileOTPData
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SignupMobileRequest(
    @Json(name = "user")
    val signupMobileData: SignupMobileData
)