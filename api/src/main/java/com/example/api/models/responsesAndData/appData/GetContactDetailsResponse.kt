package com.example.api.models.responsesAndData.appData

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetContactDetailsResponse(
    @Json(name = "success")
    var success:Boolean?,
    @Json(name = "details")
    var details:Details?,
    @Json(name = "error")
    var error:String?
)
