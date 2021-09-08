package com.example.api.services

import com.example.api.models.responses.property.propertyEntities.AddPropertyData
import com.example.api.models.responses.loginSignup.loginSignupResponses.SuccessMessageResponse
import com.example.api.models.responses.expenditure.expenditureResponses.ExpendituresResponse
import com.example.api.models.responses.expenditure.expenditureResponses.TotalExpenditureResponse
import com.example.api.models.responses.products.productsResponses.ProductCategoriesResponse
import com.example.api.models.responses.products.productsResponses.ProductResponse
import com.example.api.models.responses.products.productsResponses.ProductSubCategoriesResponse
import com.example.api.models.responses.products.productsResponses.ProductsResponse
import com.example.api.models.responses.property.propertyResponses.PropertiesResponse
import com.example.api.models.responses.property.propertyResponses.SinglePropertyResponse
import com.example.api.models.responses.updates.UpdatesResponse
import retrofit2.Response
import retrofit2.http.*

interface BuildUpAuthAPI {

    @POST("api/property")
    suspend fun addProperty(
        @Body propertyData: AddPropertyData
    ):Response<SuccessMessageResponse>

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
    ):Response<ExpendituresResponse>

    @GET("api/productCategory")
    suspend fun getProductCategories():Response<ProductCategoriesResponse>

    @GET("api/productCategory/{id}/subCategory")
    suspend fun getProductSubCategories(
        @Path("id") productCategoryId:String
    ):Response<ProductSubCategoriesResponse>

    @GET("api/product/productSubCategory/{id}")
    suspend fun getProducts(
            @Path("id") productSubCategoryId:String
    ):Response<ProductsResponse>

    @GET("api/product/{id}")
    suspend fun getProduct(
        @Path("id") productId:String
    ):Response<ProductResponse>
}