package com.example.api.models.responsesAndData.products.productsResponses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Filters(
    @Json(name = "brand")
    var brand: List<String>?,
    @Json(name = "priceRange")
    var priceRange: PriceRange?,
    @Json(name = "productCategoryId")
    var productCategoryId: String?,
    @Json(name = "productSubCategoryId")
    var productSubCategoryId: String?
)