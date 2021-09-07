package com.example.api.models.responses.loginSignup.loginSignupResponses


import com.example.api.models.entities.User
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SignupMobileResponse(
    @Json(name = "message")
    val message: String?,
    @Json(name = "success")
    val success: Boolean?,
    @Json(name = "token")
    val token: String?,
    @Json(name = "user")
    val user: User?,
    @Json(name = "error")
    val error:String?
)