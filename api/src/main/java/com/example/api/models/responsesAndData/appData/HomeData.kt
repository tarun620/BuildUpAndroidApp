package com.example.api.models.responsesAndData.appData


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HomeData(
    @Json(name = "heading1")
    var heading1: String,
    @Json(name = "heading2")
    var heading2: String,
    @Json(name = "subHeading")
    var subHeading: String,
    @Json(name = "text1")
    var text1: String,
    @Json(name = "text2")
    var text2: String
)