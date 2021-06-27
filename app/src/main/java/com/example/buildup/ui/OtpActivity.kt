package com.example.buildup.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.buildup.databinding.ActivityOtpBinding
import com.example.buildup.AuthViewModel

class OtpActivity : AppCompatActivity() {
    private var _binding:ActivityOtpBinding?=null
    lateinit var authViewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mobileNo:String?=intent.getStringExtra("mobileNo")
//        val mobileNoGoogle:String?=intent.getStringExtra("mobileNoGoogle")
//        Toast.makeText(this,"this is "+mobileNo,Toast.LENGTH_SHORT).show()
//        Toast.makeText(this,"this is "+mobileNoGoogle+" from google",Toast.LENGTH_SHORT).show()

        _binding= ActivityOtpBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)
        setContentView(_binding?.root)

        _binding?.apply {
            submitOTPButton.setOnClickListener {
              authViewModel.verifyOTP(mobileNo!!,otpEditText.text.toString())

                authViewModel.resp.observe({lifecycle}){
                    if(it?.success!!){
                        Toast.makeText(this@OtpActivity,it.message,Toast.LENGTH_SHORT).show()
                        val intent= Intent(this@OtpActivity,CompleteProfileActivity::class.java)
                        intent.putExtra("mobileNo",mobileNo)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this@OtpActivity,it.error,Toast.LENGTH_SHORT).show()
                        Log.d("errorOtp",it.error.toString())
                    }
                }
            }
        }

    }
}