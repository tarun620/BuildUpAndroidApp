package com.example.api.models.responses.updates


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Update(
    @Json(name = "amount")
    val amount: Int?,
    @Json(name = "createdAt")
    val createdAt: String,
    @Json(name = "description")
    val description: String,
    @Json(name = "expenditureId")
    val expenditureId: String?,
    @Json(name = "workCategoryId")
    val workCategoryId: WorkCategoryId?,
    @Json(name = "_id")
    val id: String
)