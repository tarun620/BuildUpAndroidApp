package com.example.api.models.responsesAndData.loginSignup.loginSignupResponses


import com.example.api.models.entities.UserWithImage
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginGoogleResponse(
    @Json(name = "message")
    val message: String?,
    @Json(name = "success")
    val success: Boolean?,
    @Json(name = "token")
    val token:String?,
    @Json(name = "user")
    val user: UserWithImage?,
    @Json(name = "error")
    val error: String?


)