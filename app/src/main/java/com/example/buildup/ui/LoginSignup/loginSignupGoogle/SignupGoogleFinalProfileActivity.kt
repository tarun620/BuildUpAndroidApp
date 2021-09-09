package com.example.buildup.ui.LoginSignup.loginSignupGoogle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.buildup.AuthViewModel
import com.example.buildup.databinding.ActivitySignupGoogleFinalProfileBinding
import com.example.buildup.ui.Property.layouts.PropertiesActivity

class SignupGoogleFinalProfileActivity : AppCompatActivity() {
//    private var _binding:ActivityCompleteProfileGoogleBinding?=null
    private var _binding:ActivitySignupGoogleFinalProfileBinding?=null
    lateinit var authViewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_signup_google_password)

//        _binding= ActivityCompleteProfileGoogleBinding.inflate(layoutInflater)
        _binding= ActivitySignupGoogleFinalProfileBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)

        setContentView(_binding?.root)

        if (getSupportActionBar() != null) {
            getSupportActionBar()?.hide();
        }

        val mobileNoGoogle:String?=intent.getStringExtra("mobileNoGoogle")
        val emailGoogle:String?=intent.getStringExtra("emailGoogle")

        _binding?.signInButton?.setOnClickListener {
            authViewModel.completeProfileGoogle(emailGoogle!!,mobileNoGoogle!!,_binding?.passwordEditText.toString())

            authViewModel.respNew.observe({lifecycle}){
                if(it?.token!=null && it.success!!){
                    Toast.makeText(this@SignupGoogleFinalProfileActivity,it.message, Toast.LENGTH_SHORT).show()
                    val intent=Intent(this, PropertiesActivity::class.java)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this@SignupGoogleFinalProfileActivity,it?.error,Toast.LENGTH_SHORT).show()
                    Log.d("errorCompleteProfileGoogle",it?.error.toString())
                }
            }
        }
    }
}