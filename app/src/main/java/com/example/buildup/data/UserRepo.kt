package com.example.buildup.data

import android.util.Log
import com.example.api.BuildUpClient
import com.example.api.models.responsesAndData.expenditure.expenditureResponses.ExpendituresResponse
import com.example.api.models.responsesAndData.expenditure.expenditureResponses.TotalExpenditureResponse
import com.example.api.models.responsesAndData.loginSignup.loginSignupEntities.*
import com.example.api.models.responsesAndData.loginSignup.loginSignupResponses.*
import com.example.api.models.responsesAndData.products.productsResponses.ProductCategoriesResponse
import com.example.api.models.responsesAndData.products.productsResponses.ProductResponse
import com.example.api.models.responsesAndData.products.productsResponses.ProductSubCategoriesResponse
import com.example.api.models.responsesAndData.products.productsResponses.ProductsResponse
import com.example.api.models.responsesAndData.property.propertyEntities.AddPropertyData
import com.example.api.models.responsesAndData.property.propertyResponses.PropertiesResponse
import com.example.api.models.responsesAndData.property.propertyResponses.SinglePropertyResponse
import com.example.api.models.responsesAndData.updates.UpdatesResponse
//import com.example.buildup.helpers.ErrorUtils.parseError
import com.example.buildup.helpers.ErrorUtilsNew
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

    suspend fun verifyOTP(mobileNo: String,otp:String): SuccessMessageResponse?{
        try{
            val response =api.verifyOTPFunc(sendOTPMobile(mobileNo,otp))
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

    suspend fun completeProfile(mobileNo: String,name:String,email:String,password:String): SignupMobileResponse?{
        try{
            val response=api.signpMobileFinal(CompleteProfileMobileData(mobileNo,name,email,password))

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

    suspend fun login(mobileNo:String,password: String): LoginResponse? {

        try{
            val response=api.login(LoginData(mobileNo,password))

            if(response.isSuccessful){
                BuildUpClient.authToken=response.body()?.token
                return response.body()
            }
            else{
//              val gson = Gson()
//              val type = object : TypeToken<APIError>() {}.type
//              var apiErrorNew: APIError = gson.fromJson(response.errorBody()!!.charStream(), type)
                val apiErrorNew= ErrorUtilsNew.parseError(response)
                return LoginResponse(null,false,null,null,apiErrorNew.error)
            }


        }catch (e:IOException){
            Log.d("TagUserRepo","Network failure")
            return LoginResponse(null,false,null,null,"Network Failure")
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
            type:String,
            houseNo:String,
            colony:String,
            city:String,
            state:String,
            pincode:Int,
            coordinates:ArrayList<Double>,
            landmark:String?): SuccessMessageResponse?{

        try{
            val response= authApi.addProperty(AddPropertyData(name, type, houseNo, colony, city, state, pincode,coordinates,landmark))

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

    suspend fun getProductCategories(): ProductCategoriesResponse?{

        try{
            val response= authApi.getProductCategories()

            if(response.isSuccessful){
                return response.body()
            }
            else{
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

    suspend fun getProduct(productId:String): ProductResponse?{

        try{
            val response= authApi.getProduct(productId)

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

    suspend fun isUserExist(email: String): UserExistResponse?{
        try{
            val response= api.isUserExist(UserExistData(email))
            if(response.isSuccessful){
                return response.body()
            }
            else{
                val apiErrorNew=ErrorUtilsNew.parseError(response)
                return UserExistResponse(false,false,apiErrorNew.error)
            }
        }catch (e:IOException){
            return UserExistResponse(false,false,"Network Failure")
        }
    }
}