package com.example.api.models.responsesAndData.products.productsResponses


import com.example.api.models.responsesAndData.products.productsEntities.ProductSubCategory
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductSubCategoriesResponse(
    @Json(name = "subCategories")
    val productSubCategories: List<ProductSubCategory>?,
    @Json(name = "success")
    val success: Boolean?,
    @Json(name="error")
    val error:String?
)