package com.example.buildup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.buildup.databinding.ActivityMainBinding
import com.example.buildup.ui.GoogleLoginActivity
import com.example.buildup.ui.LoginActivity
import com.example.buildup.ui.SignupActivity

class MainActivity : AppCompatActivity() {
    private var _binding:ActivityMainBinding?=null
    private lateinit var authViewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityMainBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)
        setContentView(_binding?.root)

        _binding?.loginButton?.setOnClickListener {
            val intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }

        _binding?.signupButton?.setOnClickListener {
            val intent= Intent(this,SignupActivity::class.java)
            startActivity(intent)
        }

        _binding?.googleLoginButton?.setOnClickListener {
            val intent=Intent(this,GoogleLoginActivity::class.java)
            startActivity(intent)
        }


    }
}