package com.example.api.models.responsesAndData.wishlist

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class IsWishlistedData(
    @Json(name="isWishlisted")
    val isWishlisted:Boolean,
    @Json(name="productId")
    val productId:String
)
