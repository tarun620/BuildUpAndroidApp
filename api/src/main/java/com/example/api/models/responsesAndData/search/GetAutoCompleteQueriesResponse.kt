package com.example.api.models.responsesAndData.search


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetAutoCompleteQueriesResponse(
    @Json(name = "queries")
    var queries: List<String>?,
    @Json(name = "success")
    var success: Boolean?,
    @Json(name = "error")
    var error : String?
)