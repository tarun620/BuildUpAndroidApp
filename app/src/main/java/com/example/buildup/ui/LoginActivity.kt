package com.example.buildup.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.buildup.AuthViewModel
import com.example.buildup.R
import com.example.buildup.databinding.ActivityLoginBinding
import com.example.buildup.databinding.ActivitySignupBinding

class LoginActivity : AppCompatActivity() {
    private var _binding:ActivityLoginBinding?=null
    lateinit var authViewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login)

        _binding= ActivityLoginBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)

        setContentView(_binding?.root)

        _binding?.apply {
            LoginButton.setOnClickListener {
                authViewModel.login(mobileNoEditText.text.toString(),passwordEditText.text.toString())

                authViewModel.respNewImage.observe({lifecycle}){
                    if(it?.token!=null && it.success){
                        Toast.makeText(this@LoginActivity,it.message, Toast.LENGTH_SHORT).show()
                        val intent= Intent(this@LoginActivity,LoggedInActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }
    }
}