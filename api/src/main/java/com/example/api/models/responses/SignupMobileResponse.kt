package com.example.api.models.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SignupMobileResponse(
    @Json(name = "message")
    val message: String?,
    @Json(name = "success")
    val success: Boolean?
)