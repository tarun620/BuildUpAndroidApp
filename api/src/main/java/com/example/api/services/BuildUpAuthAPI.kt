package com.example.api.services

import com.example.api.models.entities.AddPropertyData
import com.example.api.models.responses.SignupMobileResponse
import com.example.api.models.responses.property.PropertiesResponse
import com.example.api.models.responses.property.SinglePropertyResponse
import com.example.api.models.responses.updates.UpdatesResponse
import retrofit2.Response
import retrofit2.http.*

interface BuildUpAuthAPI {

    @POST("api/property")
    suspend fun addProperty(
        @Body propertyData: AddPropertyData
    ):Response<SignupMobileResponse>

    @GET("api/property")
    suspend fun getProperties():Response<PropertiesResponse>

    @GET("api/property/{id}")
    suspend fun getProperty(
        @Path("id") propertyId: String
    ):Response<SinglePropertyResponse>

    @GET("api/property/{id}/update")
    suspend fun getUpdates(
            @Path("id") propertyId:String
//            @Query("page") pageNumber:Int
    ):Response<UpdatesResponse>
}