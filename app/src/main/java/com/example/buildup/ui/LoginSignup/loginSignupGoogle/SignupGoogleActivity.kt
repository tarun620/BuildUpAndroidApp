package com.example.buildup.ui.LoginSignup.loginSignupGoogle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.buildup.AuthViewModel
import com.example.buildup.databinding.ActivitySignupGoogleBinding
import com.example.buildup.ui.LoginSignup.signupMobileGoogleBuffer.OtpActivity

class SignupGoogleActivity : AppCompatActivity() {
//    private var _binding:ActivitySignupGoogleBinding?=null
    private var _binding:ActivitySignupGoogleBinding?=null

    lateinit var authViewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_signup_google)

//        _binding= ActivitySignupGoogleBinding.inflate(layoutInflater)
        _binding= ActivitySignupGoogleBinding.inflate(layoutInflater)

        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)

        setContentView(_binding?.root)

        if (getSupportActionBar() != null) {
            getSupportActionBar()?.hide();
        }

        val emailGoogle:String?=intent.getStringExtra("emailgoogle")

        _binding?.sendOTPButton?.setOnClickListener {
            if(validationMobileNumber()){
                authViewModel.signupGoogleSaveMobile(_binding?.mobileEditText?.text.toString(),emailGoogle!!)

                authViewModel.resp.observe({lifecycle}){
                    if(it?.success!!){
                        Toast.makeText(this,"Otp Sent Successfully,Please check your inbox..",Toast.LENGTH_SHORT).show()
                        val intent= Intent(this, OtpActivity::class.java)
                        intent.putExtra("mobileNoGoogle",_binding?.mobileEditText?.text.toString())
                        intent.putExtra("emailGoogle",emailGoogle)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
                        Log.d("errorSignupGoogle",it.error.toString())
                    }
                }
            }
            else{
                Toast.makeText(this,"Please fill all required fields correctly",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validationMobileNumber(): Boolean {

        _binding?.apply {

            mobileTextInputLayout.error=null
            if (mobileEditText.text.toString().isNullOrBlank() || mobileEditText.text.toString().length<10) {
                mobileTextInputLayout.error = "Please enter valid mobile number"
                return false
            }
            else {
                return true
            }
        }
        return false
    }


}