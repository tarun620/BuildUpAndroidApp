package com.example.api.models.responsesAndData.address


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetAddressbyByIdResponse(
    @Json(name = "property")
    var `property`: Property?,
    @Json(name = "success")
    var success: Boolean?,
    @Json(name = "error")
    var error:String?
)