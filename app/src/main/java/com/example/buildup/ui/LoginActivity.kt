package com.example.buildup.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.api.BuildUpClient
import com.example.api.models.entities.LoginData
import com.example.api.models.responses.LoginResponse
import com.example.buildup.AuthViewModel
import com.example.buildup.R
import com.example.buildup.databinding.ActivityLoginBinding
import com.example.buildup.databinding.ActivitySignupBinding
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class LoginActivity : AppCompatActivity() {
    val api= BuildUpClient.api
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
                val call=api.login(LoginData( mobileNoEditText.text.toString(),passwordEditText.text.toString()))
                call.enqueue(object : retrofit2.Callback<LoginResponse>{
                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(this@LoginActivity,"Login Failure: ${t.message}",Toast.LENGTH_SHORT).show()

                    }

                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        if (response.code() == 200){
                            Toast.makeText(this@LoginActivity,"Login Succcessful",Toast.LENGTH_SHORT).show()
                        }
                        if(response.code()==500){
                            val loginResponse=response.errorBody()
                            Toast.makeText(this@LoginActivity,"Login Failure "+loginResponse,Toast.LENGTH_SHORT).show()
                        }
                    }
                })




//                authViewModel.login(mobileNoEditText.text.toString(),passwordEditText.text.toString())
//
//                authViewModel.respNewImage.observe({lifecycle}){
//                    if(it?.token!=null && it.success!!){
//                        Log.d("errorLogintoken",it.token!!)
//                        Toast.makeText(this@LoginActivity,it.message, Toast.LENGTH_SHORT).show()
//                        val intent= Intent(this@LoginActivity,LoggedInActivity::class.java)
//                        startActivity(intent)
//                    }
//                    else{
////                        Toast.makeText(this@LoginActivity,it?.error,Toast.LENGTH_SHORT).show()
//                        Log.d("errorLogin",it?.error.toString())
//                    }
//                }
            }
        }
    }
}