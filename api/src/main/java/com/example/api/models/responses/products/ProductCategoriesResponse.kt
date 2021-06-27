package com.example.api.models.responses.products


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductCategoriesResponse(
    @Json(name = "productCategories")
    val productCategories: List<ProductCategory>?,
    @Json(name = "success")
    val success: Boolean?,
    @Json(name="error")
    val error:String?
)