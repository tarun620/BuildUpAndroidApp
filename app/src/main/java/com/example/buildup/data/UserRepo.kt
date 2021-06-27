package com.example.buildup.data

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.example.api.BuildUpClient
import com.example.api.models.entities.*
import com.example.api.models.responses.LoginGoogleResponse
import com.example.api.models.responses.LoginResponse
import com.example.api.models.responses.SignupMobileFinalResponse
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
import com.example.buildup.helpers.APIError
import com.example.buildup.helpers.ErrorUtils
import com.example.buildup.helpers.ErrorUtils.parseError
import com.example.buildup.helpers.ErrorUtilsNew
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.lang.Exception
import java.util.*
import kotlin.coroutines.coroutineContext

//import org.junit.Assert.*

object UserRepo {
    val api=BuildUpClient.api
    val authApi=BuildUpClient.authApi

    suspend fun signup(mobileNo : String): SignupMobileResponse? {
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
//                val type = object : TypeToken<APIErrorNew>() {}.type
//                var apiErrorNew: APIErrorNew = gson.fromJson(response.errorBody()!!.charStream(), type)
                val apiErrorNew= ErrorUtilsNew.parseError(response)
                return SignupMobileResponse(null,false,apiErrorNew.error)
//                return SignupMobileResponse(null,null,"hard coded error")

            }

        }catch (e:IOException){
            Log.d("TagUserRepo","Network failure")
            return SignupMobileResponse(null,false,"Network Failure")
        }

    }

    suspend fun verifyOTP(mobileNo: String,otp:String):SignupMobileResponse?{
        try{
            val response =api.verifyOTPFunc(SignupMobileOTPData(mobileNo,otp))
            if(response.isSuccessful){
                return response.body()
            }
            else{
                val apiErrorNew= ErrorUtilsNew.parseError(response)
                return SignupMobileResponse(null,false,apiErrorNew.error)
            }
        }catch (e:IOException){
            Log.d("TagUserRepo","Network failure")
            return SignupMobileResponse(null,false,"Network Failure")
        }

    }

    suspend fun completeProfile(mobileNo: String,name:String,email:String,password:String):SignupMobileFinalResponse?{
        try{
            val response=api.signpMobileFinal(SignupData(mobileNo,name,email,password))

            if(response.isSuccessful){
                BuildUpClient.authToken=response.body()?.token
                return response.body()
            }
            else{
                val apiErrorNew= ErrorUtilsNew.parseError(response)
                return SignupMobileFinalResponse(null,false,null,null,apiErrorNew.error)
            }
        }catch (e:IOException){
            Log.d("TagUserRepo","Network failure")
            return SignupMobileFinalResponse(null,false,null,null,"Network Failure")
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
//              val type = object : TypeToken<APIErrorNew>() {}.type
//              var apiErrorNew: APIErrorNew = gson.fromJson(response.errorBody()!!.charStream(), type)
                val apiErrorNew= ErrorUtilsNew.parseError(response)
                return LoginResponse(null,false,null,null,apiErrorNew.error)
            }


        }catch (e:IOException){
            Log.d("TagUserRepo","Network failure")
            return LoginResponse(null,false,null,null,"Network Failure")
        }
    }

    suspend fun loginSignupGoogle(name:String,email:String,profileImage:String?):LoginGoogleResponse?{
        try{
            val response=api.loginGoogle(LoginGoogleData(email,name,profileImage))

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

    suspend fun signupGoogleSaveMobile(mobileNo:String,email:String):SignupMobileResponse?{

        try{
            val response=api.signupGoogleSaveMobile(SignupGoogleMobileData(mobileNo, email))

            if(response.isSuccessful){
                return response.body()
            }
            else{
                val apiErrorNew= ErrorUtilsNew.parseError(response)
                return SignupMobileResponse(null,false,apiErrorNew.error)
            }
        }catch (e:IOException){
            Log.d("TagUserRepo","Network failure")
            return SignupMobileResponse(null,false,"Network Failure")
        }
    }

    suspend fun completeProfileGoogle(email: String,mobileNo: String,password: String):SignupMobileFinalResponse?{

        try{
            val response=api.completeProfileGoogle(CompleteProfileGoogleData(email, mobileNo, password))

            if(response.isSuccessful){
                BuildUpClient.authToken=response.body()?.token
                return response.body()
            }
            else{
                val apiErrorNew= ErrorUtilsNew.parseError(response)
                return SignupMobileFinalResponse(null,false,null,null,apiErrorNew.error)
            }
        }catch (e:IOException){
            Log.d("TagUserRepo","Network failure")
            return SignupMobileFinalResponse(null,false,null,null,"Network Failure")
        }
    }

    suspend fun addProperty(
            name:String,
            type:String,
            houseNo:String,
            colony:String,
            city:String,
            state:String,
            pincode:Int):SignupMobileResponse?{

        try{
            val response= authApi.addProperty(AddPropertyData(name, type, houseNo, colony, city, state, pincode))

            if(response.isSuccessful){
                return response.body()
            }
            else{
                val apiErrorNew= ErrorUtilsNew.parseError(response)
                return SignupMobileResponse(null,false,apiErrorNew.error)
            }
        }catch (e:IOException){
            Log.d("TagUserRepo","Network failure")
            return SignupMobileResponse(null,false,"Network Failure")
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

    suspend fun getProperty(propertyId: String):SinglePropertyResponse?{
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

    suspend fun getTotalExpenditure(propertyId: String):TotalExpenditureResponse?{

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

    suspend fun getExpenditureArray(propertyId: String):ExpenditureArrayResponse?{
        try{
            val response= authApi.getExpenditureArray(propertyId)

            if(response.isSuccessful){
                return response.body()
            }
            else{
                val apiErrorNew= ErrorUtilsNew.parseError(response)
                return ExpenditureArrayResponse(null,null,false,apiErrorNew.error)
            }
        }catch (e:IOException){
            Log.d("TagUserRepo","Network failure")
            return ExpenditureArrayResponse(null,null,false,"Network Failure")
        }
    }

    suspend fun getProductCategories():ProductCategoriesResponse?{

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
    suspend fun getProductSubCategories(productCategoryId:String):ProductSubCategoriesResponse?{

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

    suspend fun getProducts(productSubCategoryId:String):ProductsArrayResponse?{

        try{
            val response= authApi.getProducts(productSubCategoryId)

            if(response.isSuccessful){
                return response.body()
            }
            else{
                val apiErrorNew= ErrorUtilsNew.parseError(response)
                return ProductsArrayResponse(null,null,false,apiErrorNew.error)
            }
        }catch (e:IOException){
            Log.d("TagUserRepo","Network failure")
            return ProductsArrayResponse(null,null,false,"Network Failure")
        }
    }

    suspend fun getProduct(productId:String):ProductResponse?{

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
}