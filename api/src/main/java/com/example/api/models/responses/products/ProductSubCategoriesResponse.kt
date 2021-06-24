package com.example.api.models.responses.products


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductSubCategoriesResponse(
    @Json(name = "subCategories")
    val subCategories: List<SubCategory>,
    @Json(name = "success")
    val success: Boolean
)