package com.example.buildup.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.buildup.AuthViewModel
import com.example.buildup.R
import com.example.buildup.databinding.ActivitySignupBinding
import com.example.buildup.databinding.ActivitySignupGoogleBinding

class SignupGoogleActivity : AppCompatActivity() {
    private var _binding:ActivitySignupGoogleBinding?=null
    lateinit var authViewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_signup_google)

        _binding= ActivitySignupGoogleBinding.inflate(layoutInflater)
//        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)
//
//        setContentView(_binding?.root)
//
//        val emailGoogle:String?=intent.getStringExtra("emailgooogle")
//        Toast.makeText(this,"this is "+emailGoogle,Toast.LENGTH_SHORT).show()
//
//        _binding?.sendOTPButton?.setOnClickListener {
//            authViewModel.signupGoogleSaveMobile(_binding?.mobileEditText?.text.toString(),emailGoogle!!)
//
//            authViewModel.resp.observe({lifecycle}){
//                if(it?.success!!){
//                    Toast.makeText(this,"Otp Sent Successfully,Please check your inbox..",Toast.LENGTH_SHORT).show()
//                    val intent= Intent(this,OtpGoogleActivity::class.java)
//                    intent.putExtra("mobileNoGoogle",_binding?.mobileEditText?.text.toString())
//                    intent.putExtra("emailGoogle",emailGoogle)
//                    startActivity(intent)
//                }
//                else{
//                    Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
//                    Log.d("errorSignupGoogle",it.error.toString())
//                }
//            }
//        }
    }

}