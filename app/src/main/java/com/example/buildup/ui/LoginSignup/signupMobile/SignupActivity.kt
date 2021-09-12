package com.example.buildup.ui.LoginSignup.signupMobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.buildup.AuthViewModel
import com.example.buildup.databinding.ActivitySignupBinding
import com.example.buildup.ui.LoginSignup.signupMobileGoogleBuffer.OtpActivity

class SignupActivity : AppCompatActivity() {

//    private var _binding:ActivitySignupBinding?=null
    private var _binding:ActivitySignupBinding?=null

    lateinit var authViewModel: AuthViewModel

//      val authViewModel: AuthViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_signup)

        _binding= ActivitySignupBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)

        setContentView(_binding?.root)

        if (getSupportActionBar() != null) {
            getSupportActionBar()?.hide();
        }
//        Toast.makeText(this@SignupActivity,"SignupActivity Launched",Toast.LENGTH_LONG).show()

        val emailEditText=intent.getStringExtra("email")
        val passwordEditText=intent.getStringExtra("password")
        Log.d("signupActivity",emailEditText.toString())
        Log.d("signupActivity",passwordEditText.toString())
        _binding?.apply {
            sendOTPButton.setOnClickListener {

                if(validationSignUp()){
                    authViewModel.signup(mobileEditText.text.toString())

                    authViewModel.resp.observe({lifecycle}){
                        if(it?.success!!){
                            Toast.makeText(this@SignupActivity,"Otp Sent Successfully,Please check your inbox..",Toast.LENGTH_SHORT).show()
                            val intent:Intent= Intent(this@SignupActivity, OtpActivity::class.java)
                            intent.putExtra("name",nameEditText.text.toString())
                            intent.putExtra("mobileNo",mobileEditText.text.toString())
                            intent.putExtra("email",emailEditText)
                            intent.putExtra("password",passwordEditText)
                            startActivity(intent)
                        }
                        else{
                            Toast.makeText(this@SignupActivity,it.error,Toast.LENGTH_SHORT).show()
                            Log.d("errorSignup",it.error.toString())
                        }
                    }
                }
                else{
                    Toast.makeText(this@SignupActivity,"Please fill all required fields correctly",Toast.LENGTH_SHORT).show()
                }

            }
        }


    }

    private fun validationSignUp(): Boolean {
        _binding?.apply {

            nameTextInputLayout.error=null
            mobileTextInputLayout.error=null
            val regex = """^[A-Za-z]+${'$'}""".toRegex()

            if(nameEditText.text.toString().isNullOrBlank() || !nameEditText.text?.matches(regex)!!){
                nameTextInputLayout.error= "Please enter Valid Name"
                nameTextInputLayout.requestFocus()
            }
            else if (mobileEditText.text.toString().isNullOrBlank() || mobileEditText.text.toString().length<10) {
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