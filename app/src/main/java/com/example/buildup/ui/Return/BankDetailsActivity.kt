package com.example.buildup.ui.Return

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.buildup.AuthViewModel
import com.example.buildup.databinding.ActivityBankDetailsBinding
import com.example.buildup.databinding.ActivityReturnBinding

class BankDetailsActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityBankDetailsBinding
    private lateinit var authViewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_bank_details)
        _binding= ActivityBankDetailsBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)
        setContentView(_binding.root)


    }
}