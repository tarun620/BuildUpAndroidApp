package com.example.buildup.ui.BottomNavigation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.buildup.R
import com.example.buildup.databinding.ActivityCartBinding
import com.example.buildup.databinding.CartLayoutBinding
import com.example.buildup.ui.Property.layouts.PropertiesActivity

class CartActivity : AppCompatActivity() {
    private lateinit var _binding: CartLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = CartLayoutBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        _binding.backBtn.setOnClickListener {
            startActivity(Intent(this,PropertiesActivity::class.java))
        }



    }



}