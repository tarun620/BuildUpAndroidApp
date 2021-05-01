package com.example.api.models.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SignupData(
    @Json(name = "mobileNo")
    val mobileNo: String,

    @Json(name = "name")
    val name : String,

    @Json(name="email")
    val email : String,

    @Json(name="password")
    val password : String

)