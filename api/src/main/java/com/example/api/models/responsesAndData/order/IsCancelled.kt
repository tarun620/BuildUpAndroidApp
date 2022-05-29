package com.example.api.models.responsesAndData.order

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class IsCancelled(
    @Json(name = "value")
    var value: Boolean,
    @Json(name = "time")
    var time:String
)
