package com.example.buildup.ui.LoginSignup.loginSignupGoogle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.buildup.databinding.ActivityForgotPassOtpBinding

private lateinit var _binding: ActivityForgotPassOtpBinding
class ForgotPassOtpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityForgotPassOtpBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        _binding.etOtp1.requestFocus()
    }
}