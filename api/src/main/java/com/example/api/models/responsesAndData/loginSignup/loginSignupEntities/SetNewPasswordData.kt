package com.example.api.models.responsesAndData.loginSignup.loginSignupEntities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SetNewPasswordData(
    @Json(name="mobileNo")
    val mobileNo: String,
    @Json(name="password")
    val password:String
    )