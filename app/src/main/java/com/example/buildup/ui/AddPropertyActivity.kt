package com.example.buildup.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.buildup.AuthViewModel
import com.example.buildup.R
import com.example.buildup.databinding.ActivityAddPropertyBinding
import com.example.buildup.databinding.ActivityLoginBinding

class AddPropertyActivity : AppCompatActivity() {
    private lateinit var _binding:ActivityAddPropertyBinding
    private lateinit var authViewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_add_property)
        _binding= ActivityAddPropertyBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)

        setContentView(_binding?.root)

        _binding?.apply{
            submitButton.setOnClickListener {
                authViewModel.addProperty(
                    etName.text.toString(),
                        etType.text.toString(),
                        etHouseNo.text.toString(),
                        etColony.text.toString(),
                        etCity.text.toString(),
                        etState.text.toString(),
                        etPincode.text.toString().toInt()
                )
            }

            authViewModel.resp.observe({lifecycle}){
                if(it?.success!!){
                    Toast.makeText(this@AddPropertyActivity,it.message,Toast.LENGTH_SHORT).show()
                    finish()
                }
                else{
                    Toast.makeText(this@AddPropertyActivity,it?.error,Toast.LENGTH_SHORT).show()
                    Log.d("errorAddProperty",it?.error.toString())
                }
            }
        }
    }
}