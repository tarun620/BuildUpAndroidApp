package com.example.buildup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.buildup.databinding.ActivityTestBinding

class TestActivity : AppCompatActivity() {
//    var button=findViewById<Button>(R.id.button)
    private var _binding:ActivityTestBinding?=null
    lateinit var authViewModel:AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_test)
        _binding= ActivityTestBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)
        setContentView(_binding?.root)

//        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)
        _binding?.apply {
            button.setOnClickListener {
                Toast.makeText(this@TestActivity,"button clicked",Toast.LENGTH_SHORT).show()
//                authViewModel.signup("9211118293")
                authViewModel.signup(input.text.toString())

            }
        }
//        button.setOnClickListener {
////            Toast.makeText(this,mobileEditText.text.toString(), Toast.LENGTH_SHORT).show()
//            authViewModel.signup("9211118293")
//        }

    }
}