package com.example.buildup.data

import android.util.Log
import com.example.api.BuildUpClient
import com.example.api.models.responsesAndData.address.GetAddressbyByIdResponse
import com.example.api.models.responsesAndData.address.GetAddressesResponse
import com.example.api.models.responsesAndData.appData.GetAppDataResponse
import com.example.api.models.responsesAndData.appData.GetContactDetailsResponse
import com.example.api.models.responsesAndData.brand.GetBrandsResponse
import com.example.api.models.responsesAndData.cart.cartEntities.ProductIdForCartData
import com.example.api.models.responsesAndData.cart.cartEntities.ProductIdForCartFromWishlistData
import com.example.api.models.responsesAndData.cart.cartEntities.UpdateProductQuantityCartData
import com.example.api.models.responsesAndData.cart.cartResponses.GetCostDeliveryDetailsResponse
import com.example.api.models.responsesAndData.cart.cartResponses.GetProductsFromCartResponse
import com.example.api.models.responsesAndData.cart.cartResponses.PropertyIdData
import com.example.api.models.responsesAndData.expenditure.expenditureResponses.ExpendituresResponse
import com.example.api.models.responsesAndData.expenditure.expenditureResponses.TotalExpenditureResponse
import com.example.api.models.responsesAndData.loginSignup.loginSignupEntities.*
import com.example.api.models.responsesAndData.loginSignup.loginSignupResponses.*
import com.example.api.models.responsesAndData.order.*
import com.example.api.models.responsesAndData.products.productsResponses.*
import com.example.api.models.responsesAndData.property.propertyEntities.AddPropertyData
import com.example.api.models.responsesAndData.property.propertyResponses.PropertiesResponse
import com.example.api.models.responsesAndData.property.propertyResponses.SinglePropertyResponse
import com.example.api.models.responsesAndData.rating.AddProductRatingDataCount
import com.example.api.models.responsesAndData.rating.GetProductRatingResponse
import com.example.api.models.responsesAndData.rating.GetUserProductRatingResponse
import com.example.api.models.responsesAndData.returnOrder.GetOrderReturnDetailsResponse
import com.example.api.models.responsesAndData.returnOrder.PlaceOrderReturnRequestData
import com.example.api.models.responsesAndData.search.GetAutoCompleteQueriesResponse
import com.example.api.models.responsesAndData.updates.UpdatesResponse
import com.example.api.models.responsesAndData.wishlist.GetWishlistResponse
import com.example.api.models.responsesAndData.wishlist.ProductIdForWishlistData
//import com.example.buildup.helpers.ErrorUtils.parseError
import com.example.buildup.helpers.ErrorUtilsNew
import retrofit2.Response
import retrofit2.http.Query
import java.io.IOException

//import org.junit.Assert.*

object UserRepo {
    val api=BuildUpClient.api
    val authApi=BuildUpClient.authApi

    suspend fun signup(mobileNo : String): SuccessMessageResponse? {
        try{
            Log.d("TagUserRepo","Entered signup func")
            val response=api.mobileNumberInputFunc(SignupMobileData(mobileNo))
            if(response.isSuccessful){
                Log.d("TagUserRepo","Signup api func called successfully")
                Log.d("TagUserRepo",response.body()?.message.toString())
                Log.d("TagUserRepo",response.body()?.error.toString())
                return response.body()
            }
            else{
                Log.d("TagUserRepo","code flow entered else block")
//                val gson = Gson()
//                val type = object : TypeToken<APIError>() {}.type
//                var apiErrorNew: APIError = gson.fromJson(response.errorBody()!!.charStream(), type)
                val apiErrorNew= ErrorUtilsNew.parseError(response)
                return SuccessMessageResponse(null,false,apiErrorNew.error)
//                return SuccessMessageResponse(null,null,"hard coded error")

            }

        }catch (e:IOException){
            Log.d("TagUserRepo","Network failure")
            return SuccessMessageResponse(null,false,"Network Failure")
        }

    }

    suspend fun verifyOTPSignup(mobileNo: String, otp:String): SuccessMessageResponse?{
        try{
            val response =api.verifyOTPFuncSignup(SendOTPMobile(mobileNo,otp))
            if(response.isSuccessful){
                return response.body()
            }
            else{
                val apiErrorNew= ErrorUtilsNew.parseError(response)
                return SuccessMessageResponse(null,false,apiErrorNew.error)
            }
        }catch (e:IOException){
            Log.d("TagUserRepo","Network failure")
            return SuccessMessageResponse(null,false,"Network Failure")
        }

    }



    suspend fun completeProfile(mobileNo: String,name:String,email:String): SignupMobileResponse?{
        try{
            val response=api.signpMobileFinal(CompleteProfileMobileData(mobileNo,name,email))

            if(response.isSuccessful){
                BuildUpClient.authToken=response.body()?.token
                return response.body()
            }
            else{
                val apiErrorNew= ErrorUtilsNew.parseError(response)
                return SignupMobileResponse(null,false,null,null,apiErrorNew.error)
            }
        }catch (e:IOException){
            Log.d("TagUserRepo","Network failure")
            return SignupMobileResponse(null,false,null,null,"Network Failure")
        }
    }

    suspend fun login(mobileNo:String): SuccessMessageResponse? {

        try{
            val response=api.login(LoginData(mobileNo))

            if(response.isSuccessful){
//                BuildUpClient.authToken=response.body()?.token
                return response.body()
            }
            else{
//              val gson = Gson()
//              val type = object : TypeToken<APIError>() {}.type
//              var apiErrorNew: APIError = gson.fromJson(response.errorBody()!!.charStream(), type)
                val apiErrorNew= ErrorUtilsNew.parseError(response)
                return SuccessMessageResponse(null,false,apiErrorNew.error)            }


        }catch (e:IOException){
            Log.d("TagUserRepo","Network failure")
            return SuccessMessageResponse(null,false,"Network Failure")
        }
    }

    suspend fun verifyOTPLogin(mobileNo: String, otp:String): LoginResponse?{
        try{
            val response =api.verifyOTPFuncLogin(SendOTPMobile(mobileNo,otp))
            if(response.isSuccessful){
                BuildUpClient.authToken=response.body()?.token
                return response.body()
            }
            else{
                val apiErrorNew= ErrorUtilsNew.parseError(response)
                return LoginResponse(null,false,null,null,apiErrorNew.error)
            }
        }catch (e:IOException){
            Log.d("TagUserRepo","Network failure")
            return LoginResponse(null,false,null,null,"Network failure")
        }

    }

    suspend fun loginSignupGoogle(name:String,email:String,profileImage:String?): LoginGoogleResponse?{
        try{
            val response=api.loginGoogle(LoginGoogleData(name,email,profileImage))

            if(response.isSuccessful){
                BuildUpClient.authToken=response.body()?.token
                return response.body()
            }
            else{
                val apiErrorNew= ErrorUtilsNew.parseError(response)
                return LoginGoogleResponse(null,false,null,null,apiErrorNew.error)
            }
        }catch (e:IOException){
            Log.d("TagUserRepo","Network failure")
            return LoginGoogleResponse(null,false,null,null,"Network Failure")
        }
    }

    suspend fun signupGoogleSaveMobile(mobileNo:String,email:String): SuccessMessageResponse?{

        try{
            val response=api.signupGoogleSaveMobile(SignupGoogleMobileData(mobileNo, email))

            if(response.isSuccessful){
                return response.body()
            }
            else{
                val apiErrorNew= ErrorUtilsNew.parseError(response)
                return SuccessMessageResponse(null,false,apiErrorNew.error)
            }
        }catch (e:IOException){
            Log.d("TagUserRepo","Network failure")
            return SuccessMessageResponse(null,false,"Network Failure")
        }
    }

    suspend fun verifyOTPFuncSignupGoogle(mobileNo: String, otp:String): LoginGoogleResponse?{
        try{
            val response =api.verifyOTPFuncSignupGoogle(SendOTPMobile(mobileNo,otp))
            if(response.isSuccessful){
                BuildUpClient.authToken=response.body()?.token
                return response.body()
            }
            else{
                val apiErrorNew= ErrorUtilsNew.parseError(response)
                return LoginGoogleResponse(null,false,null,null,apiErrorNew.error)
            }
        }catch (e:IOException){
            Log.d("TagUserRepo","Network failure")
            return LoginGoogleResponse(null,false,null,null,"Network failure")
        }

    }

    suspend fun completeProfileGoogle(email: String,mobileNo: String,password: String): SignupMobileResponse?{

        try{
            val response=api.completeProfileGoogle(CompleteProfileGoogleData(email, mobileNo, password))

            if(response.isSuccessful){
                BuildUpClient.authToken=response.body()?.token
                return response.body()
            }
            else{
                val apiErrorNew= ErrorUtilsNew.parseError(response)
                return SignupMobileResponse(null,false,null,null,apiErrorNew.error)
            }
        }catch (e:IOException){
            Log.d("TagUserRepo","Network failure")
            return SignupMobileResponse(null,false,null,null,"Network Failure")
        }
    }

    suspend fun addProperty(
            name:String,
            mobileNo:String,
            type:String,
            houseNo:String,
            colony:String,
            city:String,
            state:String,
            pincode:Int,
            coordinates:ArrayList<Double>,
            landmark:String?): SuccessMessageResponse?{

        try{
            val response= authApi.addProperty(AddPropertyData(name, mobileNo, type, houseNo, colony, city, state, pincode,coordinates,landmark))

            if(response.isSuccessful){
                return response.body()
            }
            else{
                val apiErrorNew= ErrorUtilsNew.parseError(response)
                return SuccessMessageResponse(null,false,apiErrorNew.error)
            }
        }catch (e:IOException){
            Log.d("TagUserRepo","Network failure")
            return SuccessMessageResponse(null,false,"Network Failure")
        }
    }

    suspend fun getProperties(): PropertiesResponse? {
        try{
            val response= authApi.getProperties()

            if(response.isSuccessful){
                return response.body()
            }
            else{
                val apiErrorNew= ErrorUtilsNew.parseError(response)
                return PropertiesResponse(false,null,apiErrorNew.error)
            }
        }catch (e:IOException){
            Log.d("TagUserRepo","Network failure")
            return PropertiesResponse(false,null,"Network Failure")
        }
    }

    suspend fun getProperty(propertyId: String): SinglePropertyResponse?{
        try{
            val response= authApi.getProperty(propertyId)

            if(response.isSuccessful){
                return response.body()
            }
            else{
                val apiErrorNew= ErrorUtilsNew.parseError(response)
                return SinglePropertyResponse(null,false,apiErrorNew.error)
            }
        }catch (e:IOException){
            Log.d("TagUserRepo","Network failure")
            return SinglePropertyResponse(null,false,"Network Failure")
        }
    }
    suspend fun getUpdates(propertyId:String):UpdatesResponse?{

        try{
            val response= authApi.getUpdates(propertyId)

            if(response.isSuccessful){
                return response.body()
            }
            else{
                val apiErrorNew= ErrorUtilsNew.parseError(response)
                return UpdatesResponse(false,null,null,apiErrorNew.error)
            }
        }catch (e:IOException){
            Log.d("TagUserRepo","Network failure")
            return UpdatesResponse(false,null,null,"Network Failure")
        }
    }

    suspend fun getTotalExpenditure(propertyId: String): TotalExpenditureResponse?{

        try{
            val response= authApi.getTotalExpenditure(propertyId)

            if(response.isSuccessful){
                return response.body()
            }
            else{
                val apiErrorNew= ErrorUtilsNew.parseError(response)
                return TotalExpenditureResponse(null,false,apiErrorNew.error)
            }
        }catch (e:IOException){
            Log.d("TagUserRepo","Network failure")
            return TotalExpenditureResponse(null,false,"Network Failure")
        }
    }

    suspend fun getExpenditureArray(propertyId: String): ExpendituresResponse?{
        try{
            val response= authApi.getExpenditureArray(propertyId)

            if(response.isSuccessful){
                return response.body()
            }
            else{
                val apiErrorNew= ErrorUtilsNew.parseError(response)
                return ExpendituresResponse(null,null,false,apiErrorNew.error)
            }
        }catch (e:IOException){
            Log.d("TagUserRepo","Network failure")
            return ExpendituresResponse(null,null,false,"Network Failure")
        }
    }

    suspend fun getProductCategories(isImage:Boolean,limit:Int?): ProductCategoriesResponse?{

        try{
            val response= authApi.getProductCategories(isImage, limit)

            if(response.isSuccessful){
                return response.body()
                Log.d("reachedTry","reached here")
            }
            else{
                Log.d("reachedCatch","reached here")
                val apiErrorNew= ErrorUtilsNew.parseError(response)
                return ProductCategoriesResponse(null,false,apiErrorNew.error)
            }
        }catch (e:IOException){
            Log.d("TagUserRepo","Network failure")
            return ProductCategoriesResponse(null,false,"Network Failure")
        }
    }
    suspend fun getProductSubCategories(productCategoryId:String): ProductSubCategoriesResponse?{

        try{
            val response= authApi.getProductSubCategories(productCategoryId)

            if(response.isSuccessful){
                return response.body()
            }
            else{
                val apiErrorNew= ErrorUtilsNew.parseError(response)
                return ProductSubCategoriesResponse(null,false,apiErrorNew.error)
            }
        }catch (e:IOException){
            Log.d("TagUserRepo","Network failure")
            return ProductSubCategoriesResponse(null,false,"Network Failure")
        }
    }

    suspend fun getProducts(productSubCategoryId:String): ProductsResponse?{

        try{
            val response= authApi.getProducts(productSubCategoryId)

            if(response.isSuccessful){
                return response.body()
            }
            else{
                val apiErrorNew= ErrorUtilsNew.parseError(response)
                return ProductsResponse(null,null,false,apiErrorNew.error)
            }
        }catch (e:IOException){
            Log.d("TagUserRepo","Network failure")
            return ProductsResponse(null,null,false,"Network Failure")
        }
    }

    suspend fun getProductsByProductCategoryId(productCategoryId:String):ProductsResponse?{
        try{
            val response= authApi.getProductsByProductCategoryId(productCategoryId)

            if(response.isSuccessful){
                return response.body()
            }
            else{
                val apiErrorNew= ErrorUtilsNew.parseError(response)
                return ProductsResponse(null,null,false,apiErrorNew.error)
            }
        }catch (e:IOException){
            return ProductsResponse(null,null,false,"Network Failure")
        }
    }

    suspend fun getProduct(productId:String,isBrand:Boolean,inCart:Boolean,isWishlisted:Boolean): ProductResponse?{

        try{
            val response= authApi.getProduct(productId,isBrand, inCart, isWishlisted)

            if(response.isSuccessful){
                return response.body()
            }
            else{
                val apiErrorNew= ErrorUtilsNew.parseError(response)
                return ProductResponse(null,false,apiErrorNew.error)
            }
        }catch (e:IOException){
            Log.d("TagUserRepo","Network failure")
            return ProductResponse(null,false,"Network Failure")
        }
    }

//    suspend fun isUserExist(email: String): UserExistResponse?{
//        try{
//            val response= api.isUserExist(UserExistData(email))
//            if(response.isSuccessful){
//                return response.body()
//            }
//            else{
//                val apiErrorNew=ErrorUtilsNew.parseError(response)
//                return UserExistResponse(false,false,apiErrorNew.error)
//            }
//        }catch (e:IOException){
//            return UserExistResponse(false,false,"Network Failure")
//        }
//    }

    suspend fun forgetPassword(mobileNo:String):SuccessMessageResponse?{
        try{
            val response=api.forgetPassword(SignupMobileData(mobileNo))
            if(response.isSuccessful){
                return response.body()
            }
            else{
                val apiErrorNew=ErrorUtilsNew.parseError(response)
                return SuccessMessageResponse(null,false,apiErrorNew.error)
            }
        }catch (e:IOException){
            return SuccessMessageResponse(null,false,"Network Failure")
        }
    }

    suspend fun setNewPassword(mobileNo: String,password: String):SuccessMessageResponse?{
        try{
            val response=api.setNewPassword(SetNewPasswordData(mobileNo, password))
            if(response.isSuccessful){
                return response.body()
            }
            else{
                val apiErrorNew=ErrorUtilsNew.parseError(response)
                return SuccessMessageResponse(null,false,apiErrorNew.error)
            }
        }catch (e:IOException){
            return SuccessMessageResponse(null,false,"Network Failure")
        }
    }

    suspend fun addProductToCart(productId: String,fromWishlist:Boolean):SuccessMessageResponse?{
        try{
            val response= authApi.addProductToCart(ProductIdForCartFromWishlistData(productId,fromWishlist))
            if(response.isSuccessful){
                return response.body()
            }
            else{
                val apiErrorNew=ErrorUtilsNew.parseError(response)
                return SuccessMessageResponse(null,false,apiErrorNew.error)
            }
        }catch (e:IOException){
            return SuccessMessageResponse(null,false,"Network Failure")
        }
    }

    suspend fun getProductsFromCart(): GetProductsFromCartResponse?{
        try{
            val response= authApi.getProductsFromCart()
            if(response.isSuccessful){
                return response.body()
            }
            else{
                val apiErrorNew=ErrorUtilsNew.parseError(response)
                return GetProductsFromCartResponse(null,false,apiErrorNew.error)
            }
        }catch (e:IOException){
            return GetProductsFromCartResponse(null,false,"Network Failure")
        }
    }

    suspend fun updateProductQuantityCart(productId: String, quantity:Int):SuccessMessageResponse?{
        try{
            val response= authApi.updateProductQuantityCart(UpdateProductQuantityCartData(productId,quantity))
            if(response.isSuccessful){
                return response.body()
            }
            else{
                val apiErrorNew=ErrorUtilsNew.parseError(response)
                return SuccessMessageResponse(null,false,apiErrorNew.error)
            }
        }catch (e:IOException){
            return SuccessMessageResponse(null,false,"Network Failure")
        }
    }

    suspend fun removeProductFromCart(productId: String):SuccessMessageResponse?{
        try{
            val response= authApi.removeProductFromCart(ProductIdForCartData(productId))
            if(response.isSuccessful){
                return response.body()
            }
            else{
                val apiErrorNew=ErrorUtilsNew.parseError(response)
                return SuccessMessageResponse(null,false,apiErrorNew.error)
            }
        }catch (e:IOException){
            return SuccessMessageResponse(null,false,"Network Failure")
        }
    }

    suspend fun addProductToWishlist(productId: String):SuccessMessageResponse?{
        try{
            val response= authApi.addProductToWishlist(productId)
            if(response.isSuccessful){
                return response.body()
            }
            else{
                val apiErrorNew=ErrorUtilsNew.parseError(response)
                return SuccessMessageResponse(null,false,apiErrorNew.error)

            }
        }catch (e:IOException){
            return SuccessMessageResponse(null,false,"Network Failure")
        }
    }

    suspend fun removeProductFromWishlist(productId: String):SuccessMessageResponse?{
        try{
            val response= authApi.removeProductFromWishlist(productId)
            if(response.isSuccessful){
                return response.body()
            }
            else{
                val apiErrorNew=ErrorUtilsNew.parseError(response)
                return SuccessMessageResponse(null,false,apiErrorNew.error)

            }
        }catch (e:IOException){
            return SuccessMessageResponse(null,false,"Network Failure")
        }
    }

    suspend fun getWishlist(pageNum:Int): GetWishlistResponse?{
        try{
            val response= authApi.getWishlist(pageNum)
            if(response.isSuccessful){
                return response.body()
            }
            else{
                val apiErrorNew=ErrorUtilsNew.parseError(response)
                return GetWishlistResponse(false,null,false,apiErrorNew.error)
            }
        }catch (e:IOException){
            return GetWishlistResponse(false,null,false,"Network Failure")
        }
    }

    suspend fun createOrder(propertyId: String,paymentMethod:String,transactionId:String,isPersonalUse:Boolean):SuccessMessageResponse?{
        try{
            val response= authApi.createOrder(CreateOrderData(propertyId, PaymentData(paymentMethod,transactionId),isPersonalUse))
            if(response.isSuccessful){
                return response.body()
            }
            else{
                val apiErrorNew=ErrorUtilsNew.parseError(response)
                return SuccessMessageResponse(null,false,apiErrorNew.error)
            }
        }catch (e:IOException){
            return SuccessMessageResponse(null,false,"Network Failure")
        }
    }

    suspend fun getAllOrders(pageNum:Int):GetAllOrdersResponse?{
        try {
            val response= authApi.getAllOrders(pageNum)
            if(response.isSuccessful){
                return response.body()
            }
            else{
                val apiErrorNew=ErrorUtilsNew.parseError(response)
                return GetAllOrdersResponse(false,null,false,apiErrorNew.error)
            }
        }catch (e:IOException){
            return GetAllOrdersResponse(false,null,false,"Network Failure")
        }
    }

    suspend fun getOrderById(orderId:String): GetOrderByIdResponse?{
        try{
            val response= authApi.getOrderById(orderId)
            if(response.isSuccessful){
                return response.body()
            }
            else{
                val apiErrorNew=ErrorUtilsNew.parseError(response)
                return GetOrderByIdResponse(null,false,apiErrorNew.error)
            }
        }catch (e:IOException){
            return GetOrderByIdResponse(null,false,"Network Failure")
        }
    }

    suspend fun getAddresses():GetAddressesResponse?{
        try{
            val response= authApi.getAddresses()
            if(response.isSuccessful){
                return response.body()
            }
            else{
                val apiErrorNew=ErrorUtilsNew.parseError(response)
                return GetAddressesResponse(null,false,apiErrorNew.error)
            }
        }catch (e:IOException){
            return GetAddressesResponse(null,false,"Network Failure")
        }
    }

    suspend fun getAddressById(propertyId: String):GetAddressbyByIdResponse?{
        try{
            val response= authApi.getAddressById(propertyId)
            if(response.isSuccessful){
                return response.body()
            }
            else{
                val apiErrorNew=ErrorUtilsNew.parseError(response)
                return GetAddressbyByIdResponse(null,false,apiErrorNew.error)
            }
        }catch (e:IOException){
            return GetAddressbyByIdResponse(null,false,"Network Failure")
        }
    }

    suspend fun getProductsBySearchQuery(pageNum:Int,getProductsBySearchQueryData:GetProductsBySearchQueryData): ProductsResponse?{
        try{
            val response= authApi.getProductsBySearchQuery(pageNum,getProductsBySearchQueryData)
            if(response.isSuccessful){
                return response.body()
            }
            else{
                val apiErrorNew=ErrorUtilsNew.parseError(response)
                return ProductsResponse(false,null,false,apiErrorNew.error)
            }
        }catch (e:IOException){
            return ProductsResponse(false,null,false,"Network Failure")
        }
    }

    suspend fun getProductsBySearchQuery2(searchQuery:String?,pageNum:Int?,getProductsBySearchQueryData:GetProductsBySearchQueryData): ProductsResponse?{
        try{
            val response= authApi.getProductsBySearchQuery2(searchQuery,pageNum,getProductsBySearchQueryData)
            if(response.isSuccessful){
                return response.body()
            }
            else{
                val apiErrorNew=ErrorUtilsNew.parseError(response)
                return ProductsResponse(false,null,false,apiErrorNew.error)
            }
        }catch (e:IOException){
            return ProductsResponse(false,null,false,"Network Failure")
        }
    }
    suspend fun editPropertyAddress(propertyId: String,addPropertyData: AddPropertyData):SuccessMessageResponse?{
        try{
            val response= authApi.editPropertyAddress(addPropertyData, propertyId)
            if(response.isSuccessful){
                return response.body()
            }
            else{
                val apiErrorNew=ErrorUtilsNew.parseError(response)
                return SuccessMessageResponse(null,false,apiErrorNew.error)
            }
        }catch (e:IOException){
            return SuccessMessageResponse(null,false,"Network Failure")
        }
    }

    suspend fun deletePropertyAddress(propertyId: String):SuccessMessageResponse?{
        try{
            val response= authApi.deletePropertyAddress(propertyId)
            if(response.isSuccessful){
                return response.body()
            }
            else{
                val apiErrorNew=ErrorUtilsNew.parseError(response)
                return SuccessMessageResponse(null,false,apiErrorNew.error)
            }
        }catch (e:IOException){
            return SuccessMessageResponse(null,false,"Network Failure")
        }
    }
        suspend fun getRecentlyViewedProducts():RecentlyViewedProductsResponse?{
        try{
            val response= authApi.getRecentlyViewedProducts()
            if(response.isSuccessful){
                return response.body()
            }
            else{
                val apiErrorNew=ErrorUtilsNew.parseError(response)
                return RecentlyViewedProductsResponse(null,false,apiErrorNew.error)
            }
        }catch (e:IOException){
            return RecentlyViewedProductsResponse(null,false,"Network Failure")
        }
    }

    suspend fun deleteWishlist():SuccessMessageResponse?{
        try{
            val response= authApi.deleteWishlist()
            if(response.isSuccessful){
                return response.body()
            }
            else{
                val apiErrorNew=ErrorUtilsNew.parseError(response)
                return SuccessMessageResponse(null,false,apiErrorNew.error)
            }
        }catch (e:IOException){
            return SuccessMessageResponse(null,false,"Network Failure")
        }
    }

    suspend fun getAutocompleteQueries(searchQuery: String):GetAutoCompleteQueriesResponse?{
        try{
            val response= authApi.getAutocompleteQueries(searchQuery)
            if(response.isSuccessful){
                return response.body()
            }
            else{
                val apiErrorNew=ErrorUtilsNew.parseError(response)
                return GetAutoCompleteQueriesResponse(null,false,apiErrorNew.error)
            }
        }catch (e:IOException){
            return GetAutoCompleteQueriesResponse(null,false,"Network Failure")
        }
    }

    suspend fun getBrands(isImage:Boolean,limit:Int?):GetBrandsResponse?{
        try{
            val response= authApi.getBrands(isImage, limit)
            if(response.isSuccessful){
                return response.body()
            }
            else{
                val apiErrorNew=ErrorUtilsNew.parseError(response)
                return GetBrandsResponse(null,false,apiErrorNew.error)
            }
        }catch (e:IOException){
            return GetBrandsResponse(null,false,"Network Failure")
        }
    }

    suspend fun addProductRating(productId:String,count:Int):SuccessMessageResponse?{
        try{
            val response= authApi.addProductRating(productId, AddProductRatingDataCount(count))
            if(response.isSuccessful){
                return response.body()
            }
            else{
                val apiErrorNew=ErrorUtilsNew.parseError(response)
                return SuccessMessageResponse(null,false,apiErrorNew.error)
            }
        }catch (e:IOException){
            return SuccessMessageResponse(null,false,"Network Failure")
        }
    }

    suspend fun getProductRating(productId:String):GetProductRatingResponse?{
        try{
            val response= authApi.getProductRating(productId)
            if(response.isSuccessful){
                return response.body()
            }
            else{
                val apiErrorNew=ErrorUtilsNew.parseError(response)
                return GetProductRatingResponse(null,false,null,apiErrorNew.error)
            }
        }catch (e:IOException){
            return GetProductRatingResponse(null,false,null,"Network Failure")
        }
    }

    suspend fun getUserProductRating(productId: String):GetUserProductRatingResponse?{
        return try{
            val response= authApi.getUserProductRating(productId)
            if(response.isSuccessful){
                response.body()
            } else{
                val apiErrorNew=ErrorUtilsNew.parseError(response)
                GetUserProductRatingResponse(false,null,apiErrorNew.error)
            }
        }catch (e:IOException){
            GetUserProductRatingResponse(false,null,"Network Failure")
        }
    }

    suspend fun getOrderReturnDetails(orderId: String):GetOrderReturnDetailsResponse?{
        return try{
            val response= authApi.getOrderReturnDetails(orderId)
            if(response.isSuccessful){
                response.body()
            } else{
                val apiErrorNew=ErrorUtilsNew.parseError(response)
                GetOrderReturnDetailsResponse(null,null,false,apiErrorNew.error)
            }
        }catch (e:IOException){
            GetOrderReturnDetailsResponse(null,null,false,"Network Failure")
        }
    }

    suspend fun placeOrderReturnRequest(orderId: String,placeOrderReturnRequestData:PlaceOrderReturnRequestData):SuccessMessageResponse?{
        return try{
            val response= authApi.placeOrderReturnRequest(orderId, placeOrderReturnRequestData)
            if(response.isSuccessful){
                response.body()
            } else{
                val apiErrorNew=ErrorUtilsNew.parseError(response)
                SuccessMessageResponse(null,false,apiErrorNew.error)
            }
        }catch (e:IOException){
            SuccessMessageResponse(null,false,"Network Failure")
        }
    }
    suspend fun cancelOrder(orderId: String, forceAll:Boolean):CancelOrderResponse?{
        return try{
            val response= authApi.cancelOrder(orderId,forceAll)
            Log.d("response",response.toString())
            if(response.isSuccessful){
                Log.d("responseIf",response.body().toString())
                response.body()
            } else{
                Log.d("responseElse",response.errorBody().toString())
                val apiErrorNew=ErrorUtilsNew.parseError(response)
                CancelOrderResponse(null,false,null,apiErrorNew.error)
//                response.errorBody()
            }
//            response.body()
        }catch (e:IOException){
            CancelOrderResponse(null,false,null,"Network Failure")
        }
    }

    suspend fun getCostDeliveryDetails(propertyId: String?):GetCostDeliveryDetailsResponse?{
        return try {
            val response= authApi.getCostDeliveryDetails(PropertyIdData(propertyId))
            if(response.isSuccessful){
                response.body()
            }else{
                val apiErrorNew=ErrorUtilsNew.parseError(response)
                GetCostDeliveryDetailsResponse(null,null,false,false,apiErrorNew.error)
            }
        }
        catch (e:IOException){
            GetCostDeliveryDetailsResponse(null,null,false,false,"Network Failure")
        }
    }

    suspend fun getAppData(screenType:String):GetAppDataResponse?{
        return try{
            val response= api.getAppData(screenType)
            if(response.isSuccessful){
                response.body()
            }else{
                val apiErrorNew=ErrorUtilsNew.parseError(response)
                GetAppDataResponse(null,null,null,false,apiErrorNew.error)
            }
        }catch (e:IOException){
            GetAppDataResponse(null,null,null,false,"Network Failure")
        }
    }

    suspend fun getContactDetails(): GetContactDetailsResponse?{
        return try{
            val response=api.getContactDetails()
            if(response.isSuccessful){
                response.body()
            }else{
                val apiErrorNew=ErrorUtilsNew.parseError(response)
                GetContactDetailsResponse(false,null,apiErrorNew.error)
            }
        }catch (e:IOException){
            GetContactDetailsResponse(false,null,"Network Failure")
        }
    }
//
//    suspend fun downloadInvoice(orderId:String): DownloadInvoiceResponse?{
//        return try{
//            val response= authApi.downloadInvoice(orderId)
//            if(response.isSuccessful){
//                response.body()
//            }else{
//                val apiErrorNew=ErrorUtilsNew.parseError(response)
//                DownloadInvoiceResponse(null,null,false,apiErrorNew.error)
//            }
//        }catch (e:IOException){
//            DownloadInvoiceResponse(null,null,false,"Network Failure")
//        }
//    }
}