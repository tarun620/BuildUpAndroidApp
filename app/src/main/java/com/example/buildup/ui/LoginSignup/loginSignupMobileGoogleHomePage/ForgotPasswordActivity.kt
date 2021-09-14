package com.example.buildup.ui.LoginSignup.loginSignupMobileGoogleHomePage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.buildup.R
import com.example.buildup.databinding.ActivityForgotPasswordBinding
import com.example.buildup.ui.LoginSignup.loginSignupGoogle.ForgotPassOtpActivity


private lateinit var _binding: ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        _binding.backBtn.setOnClickListener {
            startActivity(Intent(this, LoginSignupActivity::class.java))
        }

        _binding.submitButton.setOnClickListener {
            startActivity(Intent(this, ForgotPassOtpActivity::class.java))
        }
    }
}