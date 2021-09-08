package com.example.buildup.ui.LoginSignup.signupMobileGoogleBuffer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.buildup.AuthViewModel
import com.example.buildup.databinding.ActivityOtpBinding
import com.example.buildup.ui.LoginSignup.loginSignupGoogle.SignupGoogleFinalProfileActivity
import com.example.buildup.ui.Property.layouts.PropertiesActivity

class OtpActivity : AppCompatActivity() {
//    private var _binding:ActivityOtpBinding?=null
    private var _binding:ActivityOtpBinding?=null

    lateinit var authViewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mobileNoEditText:String?=intent.getStringExtra("mobileNo")
        val nameEditText=intent.getStringExtra("name")
        val emailEditText=intent.getStringExtra("email")
        val passwordEditText=intent.getStringExtra("password")

        val mobileNoGoogle=intent.getStringExtra("mobileNoGoogle")
        val emailGoogle=intent.getStringExtra("emailGoogle")

        Log.d("OtpActivity-mobile Number  ",mobileNoEditText.toString())
        Log.d("OtpActivity-name  ",nameEditText.toString())
        Log.d("OtpActivity-email  ",emailEditText.toString())
        Log.d("OtpActivity-password",passwordEditText.toString())
//        val mobileNoGoogle:String?=intent.getStringExtra("mobileNoGoogle")
//        Toast.makeText(this,"this is "+mobileNo,Toast.LENGTH_SHORT).show()
//        Toast.makeText(this,"this is "+mobileNoGoogle+" from google",Toast.LENGTH_SHORT).show()

        _binding= ActivityOtpBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)
        setContentView(_binding?.root)

        _binding?.apply {
            submit.setOnClickListener {
                if(mobileNoGoogle.isNullOrBlank()){ // it means it is signup using mobile number not GOOGLE
                    authViewModel.verifyOTP(mobileNoEditText!!,otpEditText.text.toString())

                    authViewModel.resp.observe({lifecycle}){
                        if(it?.success!!){
                            Toast.makeText(this@OtpActivity,it.message,Toast.LENGTH_SHORT).show()
//                        val intent= Intent(this@OtpActivity,CompleteProfileActivity::class.java)
//                        intent.putExtra("mobileNo",mobileNoEditText)
//                        intent.putExtra("email",emailEditText)
//                        intent.putExtra("password",passwordEditText)
//                        startActivity(intent)
                            completeProfile(nameEditText,emailEditText,mobileNoEditText,passwordEditText)
                        }
                        else{
                            Toast.makeText(this@OtpActivity,it.error,Toast.LENGTH_SHORT).show()
                            Log.d("errorOtp",it.error.toString())
                        }
                    }
                }

                else{  // it means it is a signup using mobile GOOGLE not Mobile Number .
                    authViewModel.verifyOTP(mobileNoGoogle!!,otpEditText.text.toString())

                    authViewModel.resp.observe({lifecycle}){
                        if(it?.success!!){
                            Toast.makeText(this@OtpActivity,it.message,Toast.LENGTH_SHORT).show()
                            val intent=Intent(this@OtpActivity, SignupGoogleFinalProfileActivity::class.java)
                            intent.putExtra("mobileNoGoogle",mobileNoGoogle)
                            intent.putExtra("emailGoogle",emailGoogle)
                            startActivity(intent)
                        }
                        else{
                            Toast.makeText(this@OtpActivity,it.error,Toast.LENGTH_SHORT).show()

                        }
                    }
                }

            }
        }

    }

    fun completeProfile(name:String?,email:String?,mobileNo:String?,password:String?){
        authViewModel.completeProfile(mobileNo!!,name!!,email!!,password!!)

        authViewModel.respNew.observe({lifecycle}){
            if(it?.token!=null && it.success!!){
                Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
                val intent=Intent(this, PropertiesActivity::class.java)
                startActivity(intent)
            }
            else{
                Toast.makeText(this,it?.error,Toast.LENGTH_SHORT).show()
            }
        }
    }
}