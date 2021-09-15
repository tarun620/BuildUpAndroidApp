package com.example.api.models.responses.loginSignup.loginSignupEntities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserExistData(

    @Json(name="email")
    val email : String
)
