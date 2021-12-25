package com.example.buildup.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.api.BuildUpClient
import com.example.buildup.AuthViewModel
import com.example.buildup.databinding.ActivityLoginSignupSelectorBinding
import com.example.buildup.ui.LoginSignup.loginSignupMobileGoogleHomePage.LoginSignupActivity

class LoginSignupSelectorActivity : AppCompatActivity() {

    private var _binding: ActivityLoginSignupSelectorBinding?=null
    lateinit var authViewModel: AuthViewModel
    private var doubleBackToExitPressedOnce = false
    private var isLogin=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login_signup_selector)
        _binding= ActivityLoginSignupSelectorBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)

        setContentView(_binding?.root)

        _binding?.apply {
            LoginButton.setOnClickListener {
                isLogin=true
                val intent=Intent(this@LoginSignupSelectorActivity,NewSendOtpActivity::class.java)
                intent.putExtra("isLogin", isLogin)
                startActivity(intent)
            }

            SignupButton.setOnClickListener {
                isLogin=false
                val intent=Intent(this@LoginSignupSelectorActivity,NewSendOtpActivity::class.java)
                intent.putExtra("isLogin", isLogin)
                startActivity(intent)
            }
        }
    }
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
//            return  //closes the current activity only
            this.finishAffinity();   //closes the entire application
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }
}