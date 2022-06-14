package com.example.api.models.responsesAndData.products.productsEntities


import com.example.api.models.responsesAndData.cart.cartResponses.Brand
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Product(
    @Json(name = "sp")
    val amount: Int,
    @Json(name = "categoryId")
    val categoryId: String,
    @Json(name ="subCategoryId")
    val subCategoryId:String,
    @Json(name = "description")
    val description: String?,
    @Json(name = "_id")
    val id: String,
    @Json(name = "image")
    val image: List<String>,
    @Json(name = "name")
    val name: String,
    @Json(name ="mrp")
    val mrp:Int,
    @Json(name="brand")
    val brand:Brand,
    @Json(name="inCart")
    val inCart:Boolean,
    @Json(name="isWishlisted")
    val isWishlisted:Boolean?
)