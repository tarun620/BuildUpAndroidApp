package com.example.api.models.responsesAndData.address


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Address(
    @Json(name = "city")
    var city: String,
    @Json(name = "colony")
    var colony: String,
    @Json(name = "houseNo")
    var houseNo: String,
    @Json(name = "landmark")
    var landmark: String?,
    @Json(name = "location")
    var location: Location,
    @Json(name = "pincode")
    var pincode: Int,
    @Json(name = "state")
    var state: String
)