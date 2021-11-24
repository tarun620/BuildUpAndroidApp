package com.example.buildup.ui.LoginSignup.loginSignupMobileGoogleHomePage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.buildup.AuthViewModel
import com.example.buildup.databinding.ActivityForgotPasswordBinding

class ForgetPasswordActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityForgotPasswordBinding
    lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        setContentView(_binding.root)

        if (getSupportActionBar() != null) {
            getSupportActionBar()?.hide();
        }

        _binding.backBtn.setOnClickListener {
//            startActivity(Intent(this, LoginSignupActivity::class.java))
            finish()
        }

        _binding.submitButton.setOnClickListener {
            if(validationMobileNumber()){
                authViewModel.forgetPassword(_binding.mobileEditText.text.toString())

                authViewModel.respForgetPassword.observe({lifecycle}){
                    if(it?.success!!){
                        Toast.makeText(this,"Otp Sent Successfully,Please check your inbox..",Toast.LENGTH_SHORT).show()
                        val intent=Intent(this,ForgetPasswordOtpActivity::class.java)
                        intent.putExtra("mobileNo",_binding.mobileEditText.text.toString())
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this,it.error, Toast.LENGTH_SHORT).show()
                        Log.d("errorSignup",it.error.toString())
                    }
                }

            }
            else{
                Toast.makeText(this,"Please fill all required fields correctly", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun validationMobileNumber(): Boolean {

        _binding?.apply {

            mobileTextInputLayout.error=null
            if (mobileEditText.text.toString().isNullOrBlank() || mobileEditText.text.toString().length<10) {
                mobileTextInputLayout.error = "Please enter valid mobile number"
                mobileTextInputLayout.requestFocus()
                return false
            }
            else {
                return true
            }
        }
        return false
    }
}