package com.example.api.models.responsesAndData.rating


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetUserProductRatingResponse(
    @Json(name = "success")
    var success: Boolean?,
    @Json(name = "userRating")
    var userRating: Int?,
    @Json(name = "error")
    var error:String?
)