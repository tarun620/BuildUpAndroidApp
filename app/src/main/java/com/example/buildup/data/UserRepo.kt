package com.example.buildup.data

import android.util.Log
import com.example.api.BuildUpClient
import com.example.api.models.entities.*
import com.example.api.models.responses.LoginGoogleResponse
import com.example.api.models.responses.LoginResponse
import com.example.api.models.responses.SignupMobileFinalResponse
import com.example.api.models.responses.SignupMobileResponse
import com.example.api.models.responses.property.PropertiesResponse
import com.example.api.models.responses.property.SinglePropertyResponse
import com.example.api.models.responses.updates.UpdatesResponse
import retrofit2.Response

//import org.junit.Assert.*

object UserRepo {
    val api=BuildUpClient.api
    val authApi=BuildUpClient.authApi

    suspend fun signup(mobileNo : String): SignupMobileResponse? {
        val response=api.mobileNumberInputFunc(SignupMobileData(mobileNo))
        return response.body()

    }

    suspend fun verifyOTP(mobileNo: String,otp:String): retrofit2.Response<SignupMobileResponse>{
        val response =api.verifyOTPFunc(SignupMobileOTPData(mobileNo,otp))
        return response
    }

    suspend fun completeProfile(mobileNo: String,name:String,email:String,password:String):retrofit2.Response<SignupMobileFinalResponse>{
        val response=api.signpMobileFinal(SignupData(mobileNo,name,email,password))
        BuildUpClient.authToken=response.body()?.token
        return response
    }

    suspend fun login(mobileNo:String,password: String):Response<LoginResponse>{
        val response=api.login(LoginData(mobileNo, password))
        BuildUpClient.authToken=response.body()?.token
        return response
    }

    suspend fun loginSignupGoogle(name:String,email:String,profileImage:String?):Response<LoginGoogleResponse>{
        val response=api.loginGoogle(LoginGoogleData(email,name,profileImage))
        BuildUpClient.authToken=response.body()?.token
        return response
    }

    suspend fun signupGoogleSaveMobile(mobileNo:String,email:String):Response<SignupMobileResponse>{
        val response=api.signupGoogleSaveMobile(SignupGoogleMobileData(mobileNo, email))
        return response
    }

    suspend fun completeProfileGoogle(email: String,mobileNo: String,password: String):Response<SignupMobileFinalResponse>{
        val response=api.completeProfileGoogle(CompleteProfileGoogleData(email, mobileNo, password))
        BuildUpClient.authToken=response.body()?.token
        return response
    }

    suspend fun addProperty(
            name:String,
            type:String,
            houseNo:String,
            colony:String,
            city:String,
            state:String,
            pincode:Int):Response<SignupMobileResponse>{
        val response= authApi.addProperty(AddPropertyData(name, type, houseNo, colony, city, state, pincode))

        return response
    }

    suspend fun getProperties(): Response<PropertiesResponse> {
        val response= authApi.getProperties()
        return response
    }

    suspend fun getProperty(propertyId: String):Response<SinglePropertyResponse>{
        val response= authApi.getProperty(propertyId)
        return response
    }
    suspend fun getUpdates(propertyId:String):Response<UpdatesResponse>{
        val resposne= authApi.getUpdates(propertyId)
        return resposne
    }
}