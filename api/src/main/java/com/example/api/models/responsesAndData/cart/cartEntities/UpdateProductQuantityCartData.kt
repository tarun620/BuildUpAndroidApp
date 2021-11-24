package com.example.api.models.responsesAndData.cart.cartEntities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UpdateProductQuantityCartData(
    @Json(name="productId")
    var productId:String,

    @Json(name="quantity")
    var quantity:Int

)