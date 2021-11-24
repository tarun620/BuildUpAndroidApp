package com.example.api.models.responsesAndData.wishlist

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductIdForWishlistData(
    @Json(name="productId")
    var productId:String
)
