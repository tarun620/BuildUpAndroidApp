package com.example.api.models.responsesAndData.loginSignup.loginSignupResponses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserExistResponse(
    @Json(name = "success")
    var success: Boolean,
    @Json(name = "userExists")
    var userExists: Boolean,
    @Json(name="error")
    var error:String?
)