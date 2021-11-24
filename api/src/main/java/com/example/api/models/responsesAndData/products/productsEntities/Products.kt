package com.example.api.models.responsesAndData.products.productsEntities


import com.example.api.models.responsesAndData.cart.cartResponses.Brand
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Products(
    @Json(name = "amount")
    val amount: Int,
    @Json(name = "_id")
    val id: String,
    @Json(name = "image")
    val image: List<String>,
    @Json(name = "name")
    val name: String,
    @Json(name= "mrp")
    val mrp: Int,
    @Json(name= "isWishlisted")
    val isWishlisted:Boolean?,
    @Json(name= "brand")
    val brand:Brand

)