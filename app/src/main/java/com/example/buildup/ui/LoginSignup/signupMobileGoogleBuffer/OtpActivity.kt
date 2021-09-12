package com.example.buildup.ui.LoginSignup.signupMobileGoogleBuffer

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.util.TypedValue
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.buildup.AuthViewModel
import com.example.buildup.R
import com.example.buildup.databinding.ActivityOtpBinding
import com.example.buildup.ui.LoginSignup.loginSignupGoogle.SignupGoogleFinalProfileActivity
import com.example.buildup.ui.Property.layouts.PropertiesActivity
import java.text.DecimalFormat
import java.text.NumberFormat


class OtpActivity : AppCompatActivity() {
//    private var _binding:ActivityOtpBinding?=null
    private var _binding:ActivityOtpBinding?=null
    lateinit var authViewModel: AuthViewModel

     var mobileNoEditText: String?=null
     var nameEditText: String?=null
     var emailEditText: String?=null
     var passwordEditText: String?=null
     var mobileNoGoogle: String?=null
     var emailGoogle: String?=null

    var START_MILLI_SECONDS = 60000L
    lateinit var countdown_timer: CountDownTimer
    var time_in_milli_seconds = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding= ActivityOtpBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)
        setContentView(_binding?.root)

        if (getSupportActionBar() != null) {
            getSupportActionBar()?.hide();
        }

        mobileNoEditText= intent.getStringExtra("mobileNo")
        nameEditText=intent.getStringExtra("name")
        emailEditText=intent.getStringExtra("email")
        passwordEditText=intent.getStringExtra("password")
        mobileNoGoogle=intent.getStringExtra("mobileNoGoogle")
        emailGoogle=intent.getStringExtra("emailGoogle")


        _binding?.resendOTP?.visibility= INVISIBLE
        _binding?.timerText?.visibility= VISIBLE
        OtpTimer()
        verifyMobileNumberWithOTP()

        _binding?.resendOTP?.setOnClickListener {
            if(mobileNoGoogle.isNullOrBlank())
                sendOTP(mobileNoEditText!!)
            else
                sendOTP(mobileNoGoogle!!)

            OtpTimer()
            _binding?.resendOTP?.visibility= INVISIBLE
            _binding?.timerText?.visibility= VISIBLE
        }
    }

    private fun verifyMobileNumberWithOTP(){
        _binding?.apply {
            submit.setOnClickListener {
                if(validationOTP()){
                    if(mobileNoGoogle.isNullOrBlank()){ // it means it is signup using mobile number not GOOGLE
                        authViewModel.verifyOTP(mobileNoEditText!!,otpEditText.text.toString())

                        authViewModel.resp.observe({lifecycle}){
                            if(it?.success!!){
                                Toast.makeText(this@OtpActivity,it.message,Toast.LENGTH_SHORT).show()
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
                else{
                    Toast.makeText(this@OtpActivity,"Please fill all required fields correctly",Toast.LENGTH_SHORT).show()
                }


            }
        }
    }

    private fun completeProfile(name:String?,email:String?,mobileNo:String?,password:String?){
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

    private fun sendOTP(mobileNo:String){
        authViewModel.signup(mobileNo)

        authViewModel.resp.observe({lifecycle}){
            if(it?.success!!){
                Toast.makeText(this,"Otp Sent Successfully,Please check your inbox..",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
                Log.d("errorSignup",it.error.toString())
            }
        }
    }

    private fun OtpTimer(){
        time_in_milli_seconds = 1 *10000L // 10 sec
        startTimer(time_in_milli_seconds)
    }

    private fun startTimer(time_in_seconds: Long) {
        countdown_timer = object : CountDownTimer(time_in_seconds, 1000) {
            override fun onFinish() {
                _binding?.resendOTP?.visibility= VISIBLE
                _binding?.timerText?.visibility= INVISIBLE
            }

            override fun onTick(p0: Long) {
                time_in_milli_seconds = p0
                updateTextUI()
            }
        }
        countdown_timer.start()

    }

    private fun updateTextUI() {
        val minute = (time_in_milli_seconds / 1000) / 60
        val seconds = (time_in_milli_seconds / 1000) % 60

        if(seconds.toString().length==1)
            _binding?.timerText?.text = "$minute:0$seconds" + " " + "left"
        else
            _binding?.timerText?.text = "$minute:$seconds" + " " + "left"
    }

    private fun validationOTP(): Boolean {
        _binding?.apply {

            otpTextInputLayout.error=null

            if(otpEditText.text.toString().isNullOrBlank() || otpEditText.text.toString().length<4){
                otpTextInputLayout.error= "Please enter Valid OTP"
            }
            else {
                return true
            }
        }
        return false
    }


}