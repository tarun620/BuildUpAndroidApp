package com.example.buildup.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.buildup.AuthViewModel
import com.example.buildup.R
import com.example.buildup.databinding.ActivityCompleteProfileBinding.inflate
import com.example.buildup.databinding.ActivityOtpGoogleBinding
import com.example.buildup.databinding.ActivitySignupBinding

class OtpGoogleActivity : AppCompatActivity() {
    private var _binding:ActivityOtpGoogleBinding?=null
    lateinit var authViewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_otp_google)

        _binding= ActivityOtpGoogleBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)

        setContentView(_binding?.root)

        val mobileNoGoogle:String?=intent.getStringExtra("mobileNoGoogle")
        val emailGoogle:String?=intent.getStringExtra("emailGoogle")
        Toast.makeText(this,"this is "+mobileNoGoogle+" for google",Toast.LENGTH_SHORT).show()
        Toast.makeText(this,"this is "+emailGoogle+" for google",Toast.LENGTH_LONG).show()

        _binding?.submitOTPButton?.setOnClickListener {
            authViewModel.verifyOTP(mobileNoGoogle!!,_binding?.otpEditText?.text.toString())

            authViewModel.resp.observe({lifecycle}){
                if(it?.success!!){
                    Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
                    val intent=Intent(this,CompleteProfileGoogleActivity::class.java)
                    intent.putExtra("mobileNoGoogle",mobileNoGoogle)
                    intent.putExtra("emailGoogle",emailGoogle)
                    startActivity(intent)
                }
            }
        }
    }
}