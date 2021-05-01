package com.example.api.models.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginData(
    @Json(name = "mobileNo")
    val mobileNo : String,

    @Json(name = "password")
    val password : String
)