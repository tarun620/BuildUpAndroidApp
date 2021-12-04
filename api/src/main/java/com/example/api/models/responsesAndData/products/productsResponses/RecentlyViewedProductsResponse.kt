package com.example.api.models.responsesAndData.products.productsResponses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RecentlyViewedProductsResponse(
    @Json(name = "products")
    var products: List<RecentProduct?>?,
    @Json(name = "success")
    var success: Boolean?,
    @Json(name = "error")
    var error :String?
)