package com.example.buildup.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.buildup.databinding.ActivitySignupBinding
import com.example.buildup.AuthViewModel

class SignupActivity : AppCompatActivity() {

    private var _binding:ActivitySignupBinding?=null
    lateinit var authViewModel: AuthViewModel

//      val authViewModel: AuthViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_signup)

        _binding= ActivitySignupBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)

        setContentView(_binding?.root)

//        Toast.makeText(this@SignupActivity,"SignupActivity Launched",Toast.LENGTH_LONG).show()
        _binding?.apply {
            sendOTPButton.setOnClickListener {

//                Toast.makeText(this@SignupActivity,mobileEditText.text.toString(),Toast.LENGTH_SHORT).show()
                authViewModel.signup(mobileEditText.text.toString())

                authViewModel.resp.observe({lifecycle}){
//                    if(it==null)
//                        Log.d("TAG","response is null")
                    if(it?.success!!){
                        Toast.makeText(this@SignupActivity,"Otp Sent Successfully,Please check your inbox..",Toast.LENGTH_SHORT).show()
                        val intent:Intent= Intent(this@SignupActivity,OtpActivity::class.java)
                        intent.putExtra("mobileNo",mobileEditText.text.toString())
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this@SignupActivity,it.error,Toast.LENGTH_SHORT).show()
                        Log.d("errorSignup",it.error.toString())
                    }
                }

            }
        }


    }


}