package com.example.api.models.responsesAndData.expenditure.expenditureEntities


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductIdObject(
    @Json(name = "_id")
    val id: String,
    @Json(name = "image")
    val image: List<String>,
    @Json(name = "name")
    val name: String
)