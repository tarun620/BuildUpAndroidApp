package com.example.api.services

import com.example.api.models.entities.*
import com.example.api.models.responses.LoginGoogleResponse
import com.example.api.models.responses.LoginResponse
import com.example.api.models.responses.SignupMobileFinalResponse
//import com.example.api.models.requests.SignupMobileOTPRequest
//import com.example.api.models.requests.SignupMobileRequest
import com.example.api.models.responses.SignupMobileResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface
BuildUpAPI {

//sending mobile number
    @POST("signup/mobile")
    suspend fun mobileNumberInputFunc(
        @Body userCreds:SignupMobileData
    ): Response<SignupMobileResponse>

//sending mobile number and otp
    @POST("signup/verify-otp")
    suspend fun verifyOTPFunc(
        @Body userCredsOTP:SignupMobileOTPData
        ):Response<SignupMobileResponse>

//complete proile- final step for signup using mobile
    @POST("signup/mobile/complete-profile")
    suspend fun signpMobileFinal(
        @Body userCreds:SignupData
    ):Response<SignupMobileFinalResponse>

//login
    @POST("login/mobile")
    suspend fun login(
        @Body userCreds:LoginData
    ):Response<LoginResponse>


//    @POST("login/mobile")
//    fun login(@Body userCreds: LoginData):Call<LoginResponse>

//login/signup using google
    @POST("login/google")
    suspend fun loginGoogle(
        @Body userCreds: LoginGoogleData
    ):Response<LoginGoogleResponse>

//save mobile number while signup using google
    @POST("signup/google/save-mobile")
    suspend fun signupGoogleSaveMobile(
        @Body userCreds: SignupGoogleMobileData
    ):Response<SignupMobileResponse>

// compleete profile for google signup -last step for googe signup
    @POST("signup/google/complete-profile")
    suspend fun completeProfileGoogle(
        @Body userCreds:CompleteProfileGoogleData
    ):Response<SignupMobileFinalResponse>
}