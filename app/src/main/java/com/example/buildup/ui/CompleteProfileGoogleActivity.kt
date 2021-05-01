package com.example.buildup.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.buildup.AuthViewModel
import com.example.buildup.R
import com.example.buildup.databinding.ActivityCompleteProfileGoogleBinding
import com.example.buildup.databinding.ActivitySignupBinding

class CompleteProfileGoogleActivity : AppCompatActivity() {
    private var _binding:ActivityCompleteProfileGoogleBinding?=null
    lateinit var authViewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_signup_google_password)

        _binding= ActivityCompleteProfileGoogleBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)

        setContentView(_binding?.root)

        val mobileNoGoogle:String?=intent.getStringExtra("mobileNoGoogle")
        val emailGoogle:String?=intent.getStringExtra("emailGoogle")

        _binding?.submitButton?.setOnClickListener {
            authViewModel.completeProfileGoogle(emailGoogle!!,mobileNoGoogle!!,_binding?.passwordEditText.toString())

            authViewModel.respNew.observe({lifecycle}){
                if(it?.token!=null && it.success){
                    Toast.makeText(this@CompleteProfileGoogleActivity,it.message, Toast.LENGTH_SHORT).show()
                    val intent=Intent(this,LoggedInActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}