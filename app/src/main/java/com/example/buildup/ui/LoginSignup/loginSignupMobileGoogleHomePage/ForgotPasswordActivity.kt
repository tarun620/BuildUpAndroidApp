package com.example.buildup.ui.LoginSignup.loginSignupMobileGoogleHomePage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.buildup.AuthViewModel
import com.example.buildup.R
import com.example.buildup.databinding.ActivityForgotPasswordBinding
import com.example.buildup.ui.LoginSignup.loginSignupGoogle.ForgotPassOtpActivity


private lateinit var _binding: ActivityForgotPasswordBinding
lateinit var authViewModel: AuthViewModel


class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        setContentView(_binding.root)

        if (getSupportActionBar() != null) {
            getSupportActionBar()?.hide();
        }

        _binding.backBtn.setOnClickListener {
            startActivity(Intent(this, LoginSignupActivity::class.java))
        }

        _binding.submitButton.setOnClickListener {
//            if(validationMobileNumber()){
//                TODO("functionality not implemented yet in backend")
//            }
//            else{
//                Toast.makeText(this,"Please fill all required fields correctly", Toast.LENGTH_SHORT).show()
//            }

            startActivity(Intent(this, ForgotPassOtpActivity::class.java))
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