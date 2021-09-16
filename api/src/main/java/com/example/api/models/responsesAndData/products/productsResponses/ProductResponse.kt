package com.example.api.models.responsesAndData.products.productsResponses


import com.example.api.models.responsesAndData.products.productsEntities.Product
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductResponse(
    @Json(name = "product")
    val product: Product?,
    @Json(name = "success")
    val success: Boolean?,
    @Json(name="error")
    val error:String?
)