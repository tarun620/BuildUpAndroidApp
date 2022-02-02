package com.example.api.models.responsesAndData.products.productsEntities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class ProductCategoryIdData(
    val productCategoryId:String,
    val productCategoryName:String,
    val productCategoryImage:String
)
