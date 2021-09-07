package com.example.buildup

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.buildup.databinding.ActivityMainBinding
import com.example.buildup.databinding.AddPropertyBinding
import com.example.buildup.ui.GoogleLoginActivity
import com.example.buildup.ui.LoggedInActivity
import com.example.buildup.ui.LoginActivity
import com.example.buildup.ui.LoginActivity.Companion.PREFS_KEY_TOKEN
import com.example.buildup.ui.SignupActivity

class MainActivity : AppCompatActivity() {

    //    companion object{
//        const val PREFS_FILE_AUTH="prefs_auth"
//        const val PREFS_KEY_TOKEN="token"
//    }
    private var _binding: ActivityMainBinding? = null
    private lateinit var authViewModel: AuthViewModel
//    private lateinit var sharedPrefrences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
//        sharedPrefrences=getSharedPreferences(PREFS_FILE_AUTH, Context.MODE_PRIVATE)

        setContentView(_binding?.root)

//        shared prefrences handling
//
//        sharedPrefrences.getString(PREFS_KEY_TOKEN,null)?.let{
//            val intent=Intent(this,LoggedInActivity::class.java)
//            startActivity(intent)
//
//        }

        _binding?.loginButton?.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        _binding?.signupButton?.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        _binding?.googleLoginButton?.setOnClickListener {
            val intent = Intent(this, GoogleLoginActivity::class.java)
            startActivity(intent)
        }


    }
}