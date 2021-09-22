package com.example.buildup.ui.BottomNavigation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.example.buildup.R
import com.example.buildup.databinding.ActivityProfileBinding
import com.example.buildup.ui.Property.layouts.PropertiesActivity



class ProfileActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityProfileBinding
    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        _binding.bottomNavigationView.background = null

        _binding.bottomNavigationView.menu.getItem(4).isEnabled = false
        _binding.bottomNavigationView.menu.getItem(2).isChecked = true
        setupBottomNavigationBar()

    }

    private fun setupBottomNavigationBar() {

        _binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {

                R.id.nav_home -> {
                    startActivity(Intent(this, PropertiesActivity::class.java))


                }
                R.id.nav_cart -> {

                    startActivity(Intent(this, CartActivity::class.java))

                }

                R.id.nav_wishlist -> {

                    startActivity(Intent(this, WishlistActivity::class.java))

                }

                R.id.nav_profile -> {


                }
            }
            true
        }
    }


    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
//            return  //closes the current activity only
            this.finishAffinity();   //closes the entire application
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }


}