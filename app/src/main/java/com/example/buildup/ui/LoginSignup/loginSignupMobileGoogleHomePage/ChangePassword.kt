package com.example.buildup.ui.LoginSignup.loginSignupMobileGoogleHomePage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.buildup.databinding.ActivityChangePasswordBinding

private lateinit var _binding: ActivityChangePasswordBinding
class ChangePassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(_binding.root)
    }
}