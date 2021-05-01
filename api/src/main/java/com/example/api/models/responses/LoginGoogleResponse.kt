package com.example.api.models.responses


import com.example.api.models.entities.UserWithImage
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginGoogleResponse(
    @Json(name = "message")
    val message: String,
    @Json(name = "success")
    val success: Boolean,
    @Json(name = "token")
    val token:String?,
    @Json(name = "user")
    val user: UserWithImage?

)