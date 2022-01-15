package com.example.buildup.ui.LoginSignup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.buildup.AuthViewModel
import com.example.buildup.databinding.ActivityGoogleSignupBinding

class GoogleSignupActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityGoogleSignupBinding
    private lateinit var authViewModel: AuthViewModel
    private var isGoogleSignup=true
    var emailGoogle:String?=null
    var profileImageGoogle:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityGoogleSignupBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)
        setContentView(_binding.root)

        val extras = intent.extras
        emailGoogle = extras?.getString("emailGoogle")
        profileImageGoogle = extras?.getString("profileImageGoogle")

        _binding.sendOTPButton.setOnClickListener {
            if(validationMobileNumber()){
                authViewModel.signupGoogleSaveMobile(_binding.mobileEditText.text.toString(),emailGoogle!!)
                authViewModel.resp.observe({lifecycle}){
                    if(it?.success!!){
                        val intent= Intent(this, NewOtpActivity::class.java)
                        intent.putExtra("mobileGoogle",_binding.mobileEditText.text.toString())
                        intent.putExtra("isGoogleSignup",isGoogleSignup)
                        startActivity(intent)
                    }
                    else
                        Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this,"Please fill all required fields correctly",
                    Toast.LENGTH_SHORT).show()
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