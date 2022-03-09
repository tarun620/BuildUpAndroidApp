package com.example.api.models.responsesAndData.appData

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegistrationData(
    @Json(name = "text")
    var text:String
)
