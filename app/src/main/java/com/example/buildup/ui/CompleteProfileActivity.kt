package com.example.buildup.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.buildup.databinding.ActivityCompleteProfileBinding
import com.example.buildup.AuthViewModel

class CompleteProfileActivity : AppCompatActivity() {
    private var _binding:ActivityCompleteProfileBinding?=null
    lateinit var authViewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityCompleteProfileBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)
        setContentView(_binding?.root)

        val mobileNo=intent.getStringExtra("mobileNo")
        Toast.makeText(this,"this is "+mobileNo, Toast.LENGTH_SHORT).show()

        _binding?.apply {
            submitButton.setOnClickListener {
                authViewModel.completeProfile(mobileNo!!,
                    nameEditText.text.toString(),
                    emailEditText.text.toString(),
                    passwordEditText.text.toString())

                authViewModel.respNew.observe({lifecycle}){
                    if(it?.token!=null && it.success){
                        Toast.makeText(this@CompleteProfileActivity,it.message,Toast.LENGTH_SHORT).show()
                        val intent=Intent(this@CompleteProfileActivity,LoggedInActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }


    }
}