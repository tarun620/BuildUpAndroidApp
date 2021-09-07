package com.example.api.models.responses.loginSignup.loginSignupEntities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginGoogleData (
    @Json(name = "email")
    val email : String,
    @Json(name = "name")
    val name:String,
    @Json(name="profileImage")
    val profileImage:String?

)