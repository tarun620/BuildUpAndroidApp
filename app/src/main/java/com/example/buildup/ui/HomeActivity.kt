package com.example.buildup.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.buildup.AuthViewModel
import com.example.buildup.databinding.ActivityCartBinding
import com.example.buildup.databinding.ActivityHomeBinding
import com.example.buildup.ui.Cart.adapters.CartAdapter

class HomeActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityHomeBinding
    private lateinit var authViewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityHomeBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)
        setContentView(_binding.root)


    }
}