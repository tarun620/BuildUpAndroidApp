package com.example.buildup.ui.BottomNavigation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.buildup.R
import com.example.buildup.databinding.ActivityWishlistBinding
import com.example.buildup.ui.Property.layouts.PropertiesActivity


class  WishlistActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityWishlistBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityWishlistBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        _binding.backBtn.setOnClickListener {
            startActivity(Intent(this,WishlistActivity::class.java))
        }


    }



}