package com.example.api.models.responsesAndData.returnOrder


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Product(
    @Json(name = "id")
    var id: Id,
    @Json(name = "quantity")
    var quantity: Int,
    @Json(name = "unitCost")
    var unitCost: Int,
    @Json(name = "unitMrp")
    var unitMrp: Int
)