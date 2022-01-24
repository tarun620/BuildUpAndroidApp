package com.example.api.models.responsesAndData.appData


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetAppDataResponse(
    @Json(name = "homeData")
    var homeData: HomeData?,
    @Json(name = "introData")
    var introData: List<IntroData>?,
    @Json(name = "success")
    var success: Boolean?,
    @Json(name = "error")
    var error:String?
)