package com.example.buildup.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.buildup.AuthViewModel
import com.example.buildup.databinding.ActivityNewestOtpBinding
import com.example.buildup.ui.LoginSignup.signupMobileGoogleBuffer.OtpActivity

class NewSendOtpActivity : AppCompatActivity() {
    private var _binding: ActivityNewestOtpBinding?=null
    lateinit var authViewModel: AuthViewModel
    private var isLogin=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_newest_otp)

        _binding= ActivityNewestOtpBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)
        setContentView(_binding?.root)

        if (getSupportActionBar() != null) {
            getSupportActionBar()?.hide()
        }

        isLogin=intent.getBooleanExtra("isLogin",false)


        _binding?.apply {
            sendOTPButton.setOnClickListener {
                if(validationMobileNumber()){
                    if(isLogin){
                        authViewModel.login(mobileEditText.text.toString())
                    }
                    else{
                        authViewModel.signup(mobileEditText.text.toString())
                    }

                    authViewModel.resp.observe({lifecycle}){
                        if(it?.success!!){
                            Toast.makeText(this@NewSendOtpActivity,"Otp Sent Successfully,Please check your inbox..",
                                Toast.LENGTH_SHORT).show()
                            val intent: Intent = Intent(this@NewSendOtpActivity, NewOtpActivity::class.java)
                            intent.putExtra("mobileNo",mobileEditText.text.toString())
                            intent.putExtra("isLogin",isLogin)
                            startActivity(intent)
                        }
                        else{
                            Toast.makeText(this@NewSendOtpActivity,it.error, Toast.LENGTH_SHORT).show()
                            Log.d("errorSignup",it.error.toString())
                        }
                    }
                }
                else{
                    Toast.makeText(this@NewSendOtpActivity,"Please fill all required fields correctly",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun validationMobileNumber(): Boolean {
        _binding?.apply {

            mobileTextInputLayout.error=null

            if (mobileEditText.text.toString().isNullOrBlank() || mobileEditText.text.toString().length<10) {
                mobileTextInputLayout.error = "Please enter valid mobile number"
                mobileTextInputLayout.requestFocus()
                return false
            }
            else {
                return true
            }
        }
        return false
    }
}