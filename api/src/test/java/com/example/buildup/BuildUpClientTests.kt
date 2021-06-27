package com.example.buildup

import com.example.api.BuildUpClient
import com.example.api.models.entities.*
//import com.example.api.models.requests.SignupMobileOTPRequest
//import com.example.api.models.requests.SignupMobileRequest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import sun.rmi.runtime.Log


class BuildUpClientTests {

    @Test
    fun `send mobile number`(){
        runBlocking{
            val signupMobileData=SignupMobileData(
                mobileNo ="9211118292"
            )
            val resp=BuildUpClient.api.mobileNumberInputFunc(signupMobileData)
            resp.body()?.success?.let { assertTrue(it) }
        }
    }
@Test
    fun `verify otp`(){
        runBlocking {
            val signupMobileOTPData=SignupMobileOTPData(
                mobileNo = "9211118299",
                otp = "1261"
            )
            val resp=BuildUpClient.api.verifyOTPFunc(signupMobileOTPData)
            resp.body()?.success?.let { assertTrue(it) }
        }
    }

    @Test
    fun `signup complete profile`(){
        runBlocking {
            val signupData=SignupData(
                mobileNo = "9211118297",
                name = "test_user3",
                email = "testuser4@test.com",
                password = "Abcd@1234"
            )
            val resp=BuildUpClient.api.signpMobileFinal(signupData)
            assertNotNull(resp.body()?.token)
        }
    }

//    @Test
//    fun login(){
//        runBlocking {
//            val loginData=LoginData(
//                mobileNo = "9211118292",
//                password = "tarun62"
//            )
//            val resp=BuildUpClient.api.login(loginData)
//                assertNotNull(resp.body()?.token)
//
//        }
//    }

//    @Test
//    fun `signin with google`(){
//        runBlocking {
//            val loginGoogleData=LoginGoogleData(
//                email = "testuser2@test.com"
//            )
//            val resp=BuildUpClient.api.loginGoogle(loginGoogleData)
////            resp.body()?.success?.let { assertTrue(it) }
////            assertNotNull(resp.body()?.token)
//            assertNull(resp.body()?.token)
//
//        }
//    }
    @Test
    fun `signup gooogle mobile number save`(){
        runBlocking {
            val signupGoogleMobileData=SignupGoogleMobileData(
                    email = "testuser2@test.com",
                    mobileNo = "9123456783"
            )
            val resp=BuildUpClient.api.signupGoogleSaveMobile(signupGoogleMobileData)
            resp.body()?.success?.let { assertTrue(it) }
        }
    }

    @Test
    fun addProperty(){
        runBlocking {
            val addPropertyData=AddPropertyData(
                name = "Property A",
                type = "home",
                houseNo = "B5",
                colony = "badarpur",
                city = "south delhi",
                state = "delhi",
                pincode = 110044
            )

            val resp=BuildUpClient.authApi.addProperty(addPropertyData)
            resp.body()?.success?.let { assertTrue(it) }
        }
    }
}