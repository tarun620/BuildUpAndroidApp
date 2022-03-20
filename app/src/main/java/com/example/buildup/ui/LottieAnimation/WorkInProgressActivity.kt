package com.example.buildup.ui.LottieAnimation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.buildup.R
import com.example.buildup.databinding.ActivityCartBinding
import com.example.buildup.databinding.ActivityWorkInProgressBinding
import com.example.buildup.ui.BottomNavigation.CartActivity
import com.example.buildup.ui.BottomNavigation.ProfileActivity
import com.example.buildup.ui.HomeActivity

class WorkInProgressActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityWorkInProgressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityWorkInProgressBinding.inflate(layoutInflater)

        setContentView(_binding.root)

        _binding.apply {
            bottomNavigationView.background = null
            setupBottomNavigationBar()
//        _binding.bottomNavigationView.selectedItemId(R.id.nav_cart)
            bottomNavigationView.menu.getItem(2).isEnabled = false
            bottomNavigationView.menu.getItem(2).isChecked = true
        }
    }

    private fun setupBottomNavigationBar() {

        _binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {

                R.id.nav_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    overridePendingTransition(0,0)


                }
                R.id.nav_cart -> {

                    startActivity(Intent(this, CartActivity::class.java))
                    overridePendingTransition(0,0)

                }

                R.id.nav_property -> {


                }

                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    overridePendingTransition(0,0)

                }
            }
            true
        }
    }

    override fun onResume() {
        super.onResume()
        _binding.bottomNavigationView.menu.getItem(2).isEnabled = false
        _binding.bottomNavigationView.menu.getItem(2).isChecked = true
    }

}