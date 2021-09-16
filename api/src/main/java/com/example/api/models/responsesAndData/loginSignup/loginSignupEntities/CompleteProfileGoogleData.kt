package com.example.api.models.responsesAndData.loginSignup.loginSignupEntities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CompleteProfileGoogleData(
        @Json(name="email")
        val email : String,

        @Json(name = "mobileNo")
        val mobileNo: String,

        @Json(name="password")
        val password : String

)