package com.example.buildup.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.buildup.R
import com.example.buildup.databinding.ActivityCartBinding
import com.example.buildup.ui.Property.layouts.PropertiesActivity

private lateinit var _binding: ActivityCartBinding

class CartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        _binding.bottomNavigationView.background = null

        setupBottomNavigationBar()

    }


    private fun setupBottomNavigationBar() {

        _binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {

                R.id.nav_home -> {

                    startActivity(Intent(this, PropertiesActivity::class.java))


                }
                R.id.nav_cart -> {


                }

                R.id.nav_wishlist -> {

                    startActivity(Intent(this, WishlistActivity::class.java))


                }

                R.id.nav_profile -> {

                    startActivity(Intent(this, ProfileActivity::class.java))


                }
            }
            true
        }
    }
}