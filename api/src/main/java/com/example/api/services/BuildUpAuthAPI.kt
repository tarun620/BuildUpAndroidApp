package com.example.api.services

import com.example.api.models.entities.AddPropertyData
import com.example.api.models.responses.SignupMobileResponse
import com.example.api.models.responses.expenditure.ExpenditureArrayResponse
import com.example.api.models.responses.expenditure.TotalExpenditureResponse
import com.example.api.models.responses.products.ProductCategoriesResponse
import com.example.api.models.responses.products.ProductResponse
import com.example.api.models.responses.products.ProductSubCategoriesResponse
import com.example.api.models.responses.products.ProductsArrayResponse
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

    @GET("api/property/{id}/total-expenditure")
    suspend fun getTotalExpenditure(
        @Path("id") propertyId: String
    ):Response<TotalExpenditureResponse>

    @GET("api/property/{id}/expenditure")
    suspend fun getExpenditureArray(
            @Path("id") propertyId: String
    ):Response<ExpenditureArrayResponse>

    @GET("api/productCategory")
    suspend fun getProductCategories():Response<ProductCategoriesResponse>

    @GET("api/productCategory/{id}/subCategory")
    suspend fun getProductSubCategories(
        @Path("id") productCategoryId:String
    ):Response<ProductSubCategoriesResponse>

    @GET("api/product/productSubCategory/{id}")
    suspend fun getProducts(
            @Path("id") productSubCategoryId:String
    ):Response<ProductsArrayResponse>

    @GET("api/product/{id}")
    suspend fun getProduct(
        @Path("id") productId:String
    ):Response<ProductResponse>
}