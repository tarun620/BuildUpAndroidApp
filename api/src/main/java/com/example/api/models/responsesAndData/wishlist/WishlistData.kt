package com.example.api.models.responsesAndData.wishlist

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class WishlistData(
    @Json(name="inCart")
    val inCart:Boolean,
    @Json(name="productId")
    val productId:String
)
