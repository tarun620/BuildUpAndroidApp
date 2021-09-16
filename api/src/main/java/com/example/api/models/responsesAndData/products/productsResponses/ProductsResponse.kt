package com.example.api.models.responsesAndData.products.productsResponses


import com.example.api.models.responsesAndData.products.productsEntities.Products
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductsResponse(
    @Json(name = "hasNext")
    val hasNext: Boolean?,
    @Json(name = "products")
    val products: List<Products>?,
    @Json(name = "success")
    val success: Boolean?,
    @Json(name = "error")
    val error:String?
)