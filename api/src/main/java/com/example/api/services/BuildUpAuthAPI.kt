package com.example.api.services

import com.example.api.models.responsesAndData.address.GetAddressbyByIdResponse
import com.example.api.models.responsesAndData.address.GetAddressesResponse
import com.example.api.models.responsesAndData.brand.GetBrandsResponse
import com.example.api.models.responsesAndData.cart.cartEntities.ProductIdForCartData
import com.example.api.models.responsesAndData.cart.cartEntities.ProductIdForCartFromWishlistData
import com.example.api.models.responsesAndData.cart.cartEntities.UpdateProductQuantityCartData
import com.example.api.models.responsesAndData.cart.cartResponses.GetProductsFromCartResponse
import com.example.api.models.responsesAndData.property.propertyEntities.AddPropertyData
import com.example.api.models.responsesAndData.loginSignup.loginSignupResponses.SuccessMessageResponse
import com.example.api.models.responsesAndData.expenditure.expenditureResponses.ExpendituresResponse
import com.example.api.models.responsesAndData.expenditure.expenditureResponses.TotalExpenditureResponse
import com.example.api.models.responsesAndData.order.CreateOrderData
import com.example.api.models.responsesAndData.order.GetAllOrdersResponse
import com.example.api.models.responsesAndData.order.GetOrderByIdResponse
import com.example.api.models.responsesAndData.products.productsResponses.*
import com.example.api.models.responsesAndData.property.propertyResponses.PropertiesResponse
import com.example.api.models.responsesAndData.property.propertyResponses.SinglePropertyResponse
import com.example.api.models.responsesAndData.search.GetAutoCompleteQueriesResponse
import com.example.api.models.responsesAndData.updates.UpdatesResponse
import com.example.api.models.responsesAndData.wishlist.GetWishlistResponse
import com.example.api.models.responsesAndData.wishlist.ProductIdForWishlistData
import okhttp3.internal.connection.RealConnectionPool
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
    suspend fun getProductCategories(
        @Query("image") isImage: Boolean?,
        @Query("limit") limit: Int?
    ):Response<ProductCategoriesResponse>

    @GET("api/productCategory/{id}/subCategory")
    suspend fun getProductSubCategories(
        @Path("id") productCategoryId:String
    ):Response<ProductSubCategoriesResponse>

    @GET("api/product/productSubCategory/{id}")
    suspend fun getProducts(
            @Path("id") productSubCategoryId:String
    ):Response<ProductsResponse>

    @GET("api/product/productCategory")
    suspend fun getProductsByProductCategoryId(
        @Path("id") productCategoryId : String
    ):Response<ProductsResponse>

    @GET("api/product/{id}")
    suspend fun getProduct(
        @Path("id") productId:String,
        @Query("brand") isBrand:Boolean,
        @Query("inCart") inCart:Boolean,
        @Query("isWishlisted") isWishlisted:Boolean
    ):Response<ProductResponse>

    @POST("api/cart")
    suspend fun addProductToCart(
        @Body productIdForCartData: ProductIdForCartFromWishlistData
    ):Response<SuccessMessageResponse>

    @GET("api/cart")
    suspend fun getProductsFromCart():Response<GetProductsFromCartResponse>

    @PUT("api/cart")
    suspend fun updateProductQuantityCart(
        @Body updateProductQuantityCartData: UpdateProductQuantityCartData
    ):Response<SuccessMessageResponse>

    @HTTP(method = "DELETE", path= "api/cart",hasBody = true)
    suspend fun removeProductFromCart(
        @Body productIdForCartData: ProductIdForCartData
    ):Response<SuccessMessageResponse>

//    @DELETE("api/cart")
//    suspend fun removeProductFromCart(
//        @Body productIdForCartData: ProductIdForCartData
//    ):Response<SuccessMessageResponse>

    @PUT("api/product/{id}/wishlist")
    suspend fun addProductToWishlist(
        @Path("id") productId:String
    ):Response<SuccessMessageResponse>

    @HTTP(method = "DELETE", path= "api/product/wishlist",hasBody = true)
    suspend fun removeProductFromWishlist(
        @Body productIdForWishlistData: ProductIdForWishlistData
    ):Response<SuccessMessageResponse>

//    @DELETE("api/product/wishlist")
//    suspend fun removeProductFromWishlist(
//        @Body productId: String
//    ):Response<SuccessMessageResponse>

    @GET("api/product/wishlist")
    suspend fun getWishlist():Response<GetWishlistResponse>

    @POST("api/order")
    suspend fun createOrder(
        @Body createOrderData: CreateOrderData
    ):Response<SuccessMessageResponse>

    @GET("api/order")
    suspend fun getAllOrders():Response<GetAllOrdersResponse>

    @GET("api/order/{id}")
    suspend fun getOrderById(
        @Path("id") orderId:String
    ):Response<GetOrderByIdResponse>

    @GET("api/property/address")
    suspend fun getAddresses() :  Response<GetAddressesResponse>

    @GET("api/property/address/{id}")
    suspend fun getAddressById(
        @Path("id") propertyId:String
    ) : Response<GetAddressbyByIdResponse>

    @POST("api/search/products")
    suspend fun getProductsBySearchQuery(
        @Query("page") pageNum:Int,
        @Body getProductsBySearchQueryData: GetProductsBySearchQueryData
    ):Response<ProductsResponse>

    @POST("api/search/products")
    suspend fun getProductsBySearchQuery2(
        @Query("q") searchQuery:String?,
        @Body getProductsBySearchQueryData: GetProductsBySearchQueryData
    ):Response<ProductsResponse>

    @PUT("api/property/{id}")
    suspend fun editPropertyAddress(
        @Body addPropertyData: AddPropertyData,
        @Path("id") propertyId: String
    ):Response<SuccessMessageResponse>

    @HTTP(method = "DELETE", path= "api/property/{id}",hasBody = false)
    suspend fun deletePropertyAddress(
        @Path("id") propertyId:String
    ):Response<SuccessMessageResponse>

    @GET("api/search/product-history")
    suspend fun getRecentlyViewedProducts():Response<RecentlyViewedProductsResponse>

    @HTTP(method = "DELETE", path= "api/product/wishlist",hasBody = false)
    suspend fun deleteWishlist():Response<SuccessMessageResponse>

    @GET("api/search/auto-complete")
    suspend fun getAutocompleteQueries(
        @Query("q") searchQuery:String
    ):Response<GetAutoCompleteQueriesResponse>

    @GET("api/brand")
    suspend fun getBrands(
        @Query("image") isImage:Boolean?,
        @Query("limit") limit:Int?
    ):Response<GetBrandsResponse>


}