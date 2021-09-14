package com.example.api.models.responses.updates


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WorkCategoryId2(
    @Json(name = "workCategoryId")
    var workCategoryId: WorkCategoryId
)