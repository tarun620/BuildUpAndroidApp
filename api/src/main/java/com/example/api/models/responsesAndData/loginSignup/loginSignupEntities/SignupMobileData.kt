package com.example.api.models.responsesAndData.loginSignup.loginSignupEntities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SignupMobileData(
    @Json(name = "mobileNo")
    val mobileNo : String?

)