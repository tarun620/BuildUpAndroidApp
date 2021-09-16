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
        _binding.bottomNavigationView.background = null

        setupBottomNavigationBar()

    }


    private fun setupBottomNavigationBar() {

        _binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {

                R.id.nav_home -> {

                    startActivity(Intent(this,PropertiesActivity::class.java))

                }
                R.id.nav_cart -> {

                    startActivity(Intent(this, CartActivity::class.java))

                }

                R.id.nav_wishlist -> {



                }

                R.id.nav_profile -> {

                    startActivity(Intent(this, ProfileActivity::class.java))


                }
            }
            true
        }
    }

}