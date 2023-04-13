package com.example.api.services

//import com.example.api.models.requests.SignupMobileOTPRequest
//import com.example.api.models.requests.SignupMobileRequest
import com.example.api.models.responsesAndData.appData.GetAppDataResponse
import com.example.api.models.responsesAndData.appData.GetContactDetailsResponse
import com.example.api.models.responsesAndData.loginSignup.loginSignupEntities.*
import com.example.api.models.responsesAndData.loginSignup.loginSignupResponses.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface
BuildUpAPI {

//sending mobile number
    @POST("signup/mobile")
    suspend fun mobileNumberInputFunc(
        @Body userCreds: SignupMobileData
    ): Response<SuccessMessageResponse>

//sending mobile number and otp for signup
    @POST("verify-otp/signup")
    suspend fun verifyOTPFuncSignup(
        @Body userCredsOTP: SendOTPMobile
        ):Response<SuccessMessageResponse>


//check if user exixts with given email

//    @POST("api/user/exists")
//    suspend fun isUserExist(
//        @Body userCreds : UserExistData
//    ) : Response<UserExistResponse>

//complete proile- final step for signup using mobile
    @POST("signup/mobile/complete-profile")
    suspend fun signpMobileFinal(
        @Body userCreds: CompleteProfileMobileData
    ):Response<SignupMobileResponse>

//login
    @POST("login/mobile")
    suspend fun login(
        @Body userCreds: LoginData
    ):Response<SuccessMessageResponse>

    //sending mobile number and otp for login
    @POST("verify-otp/login")
    suspend fun verifyOTPFuncLogin(
        @Body userCredsOTP: SendOTPMobile
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
    ):Response<SuccessMessageResponse>


    //sending mobile number and otp for signup
    @POST("verify-otp/signup")
    suspend fun verifyOTPFuncSignupGoogle(
        @Body userCredsOTP: SendOTPMobile
    ):Response<LoginGoogleResponse>

// compleete profile for google signup -last step for googe signup
    @POST("signup/google/complete-profile")
    suspend fun completeProfileGoogle(
        @Body userCreds: CompleteProfileGoogleData
    ):Response<SignupMobileResponse>

// forget password
    @POST("signup/mobile/forgot-password")
    suspend fun forgetPassword(
        @Body userCreds: SignupMobileData
    ):Response<SuccessMessageResponse>

//set new password
    @POST("signup/mobile/forgot-password/set-password")
    suspend fun setNewPassword(
        @Body userCreds:SetNewPasswordData
    ):Response<SuccessMessageResponse>

    @GET("api/appdata/{screen}")
    suspend fun getAppData(
        @Path("screen") screenType:String
    ):Response<GetAppDataResponse>

    @GET("api/contact/support")
    suspend fun getContactDetails() : Response<GetContactDetailsResponse>
}