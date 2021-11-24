package com.example.api.models.responsesAndData.wishlist


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetWishlistResponse(
    @Json(name = "hasNext")
    var hasNext: Boolean?,
    @Json(name = "products")
    var products: List<Product>?,
    @Json(name = "success")
    var success: Boolean?,
    @Json(name = "error")
    var error:String?
)