package com.example.api.models.responses.expenditure


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Expenditure(
        @Json(name = "amount")
    val amount: Int,
        @Json(name = "categoryId")
    val categoryId: CategoryIdObject?,
        @Json(name = "productId")
    val productId: ProductIdObject?,
        @Json(name = "createdAt")
    val createdAt: String,
        @Json(name = "description")
    val description: String?,
        @Json(name = "fromCustomer")
    val fromCustomer: Boolean,
        @Json(name = "_id")
    val id: String
)