package com.example.buildup.ui.BottomNavigation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.buildup.R
import com.example.buildup.TinyDB
import com.example.buildup.databinding.ActivityProfileBinding
import com.example.buildup.ui.HomeActivity
import com.example.buildup.ui.LottieAnimation.WorkInProgressActivity
import com.example.buildup.ui.Orders.layouts.OrdersActivity
import com.example.buildup.ui.Property.layouts.PropertiesActivity



class ProfileActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityProfileBinding
    private var doubleBackToExitPressedOnce = false
    private lateinit var tinyDB : TinyDB


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        tinyDB = TinyDB(this)


        _binding.bottomNavigationView.background = null

        _binding.bottomNavigationView.menu.getItem(3).isEnabled = false
        _binding.bottomNavigationView.menu.getItem(3).isChecked = true

        setProfileData()
        setButtonsFunctionalities()
        setupBottomNavigationBar()

        _binding.backBtn.setOnClickListener {
            finish()
//            val intent=Intent(this,PropertiesActivity::class.java)
//            startActivity(intent)
        }

    }

    private fun setButtonsFunctionalities() {
        _binding.apply {
            myPropertiesBtn.setOnClickListener {
                startActivity(Intent(this@ProfileActivity,WorkInProgressActivity::class.java))
            }

            myOrdersBtn.setOnClickListener {
                startActivity(Intent(this@ProfileActivity,OrdersActivity::class.java))
            }

            myWishlistBtn.setOnClickListener {
                startActivity(Intent(this@ProfileActivity,WishlistActivity::class.java))
            }
        }
    }

    private fun setProfileData() {
        _binding.apply {
            if(!tinyDB.getString("userProfileImage").isNullOrBlank())
                Glide.with(this@ProfileActivity)
                    .load(tinyDB.getString("userProfileImage"))
                    .circleCrop()
                    .placeholder(R.drawable.ic_profile)
                    .into(ivProfileImage)
            userName.text=tinyDB.getString("userName")
            tvPhoneNo.text="+91 " + tinyDB.getString("userMobile")
        }
    }

    private fun setupBottomNavigationBar() {

        _binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {

                R.id.nav_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))


                }
                R.id.nav_cart -> {

                    startActivity(Intent(this, CartActivity::class.java))

                }

                R.id.nav_property -> {

                    startActivity(Intent(this, WorkInProgressActivity::class.java))

                }

                R.id.nav_profile -> {


                }
            }
            true
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


}