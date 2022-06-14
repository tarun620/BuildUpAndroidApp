package com.example.api.models.responsesAndData.order


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Product(
    @Json(name = "id")
    var id: Id,
    @Json(name = "quantity")
    var quantity: Int,
    @Json(name = "unitSp")
    var unitCost: Int,
    @Json(name = "unitMrp")
    var unitMrp: Int
)