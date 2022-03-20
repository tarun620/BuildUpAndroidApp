package com.example.buildup.ui.BottomNavigation

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.bumptech.glide.Glide
import com.example.api.BuildUpClient
import com.example.buildup.R
import com.example.buildup.TinyDB
import com.example.buildup.databinding.ActivityProfileNewBinding
import com.example.buildup.ui.Address.layouts.AddressesActivityProfile
import com.example.buildup.ui.HomeActivity
import com.example.buildup.ui.LoginSignup.LoginSignupSelectorActivity
import com.example.buildup.ui.LottieAnimation.WorkInProgressActivity
import com.example.buildup.ui.Orders.layouts.OrdersActivity
import kotlin.math.sign


class ProfileActivity : AppCompatActivity() {
    companion object {
        var PREFS_FILE_AUTH = "prefs_auth"
        var PREFS_KEY_TOKEN = "token"
    }
    private lateinit var _binding: ActivityProfileNewBinding
    private var doubleBackToExitPressedOnce = false
    private lateinit var tinyDB : TinyDB
    private lateinit var sharedPrefrences: SharedPreferences



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityProfileNewBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        drawLayout()
        _binding.btnRetry.setOnClickListener {
//            drawLayout()
            finish();
            startActivity(intent);
        }

        tinyDB = TinyDB(this)
        sharedPrefrences = getSharedPreferences(PREFS_FILE_AUTH, Context.MODE_PRIVATE)


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
            buildUpPlusZoneLayout.setOnClickListener {
                startActivity(Intent(this@ProfileActivity,WorkInProgressActivity::class.java))
            }

            myOrdersLayout.setOnClickListener {
                val intent=Intent(this@ProfileActivity,OrdersActivity::class.java)
                intent.putExtra("fromProfileActivity",true)
                startActivity(intent)
            }

            myWishlistLayout.setOnClickListener {
                startActivity(Intent(this@ProfileActivity,WishlistActivity::class.java))
            }

            myAddressesLayout.setOnClickListener {
                startActivity(Intent(this@ProfileActivity,AddressesActivityProfile::class.java))
            }

            btnLogout.setOnClickListener{
                MyCustomDialog().show(supportFragmentManager, "MyCustomFragment")
            }
//            btnLogout.setOnClickListener {
//                val builder = AlertDialog.Builder(this@ProfileActivity)
//                val alertDialog: AlertDialog = builder.create()
//                val inflater: LayoutInflater = layoutInflater
//                val dialogView: View = inflater.inflate(R.layout.layout_alert_dialog, null)
////                alertDialog.setContentView(dialogView);
//                builder.setView(dialogView)
//
//                var dialogIcon:ImageView=dialogView.findViewById(R.id.iv_icon)
//                var dialogTitle:TextView=dialogView.findViewById(R.id.tv_title)
//                var dialogDesc:TextView=dialogView.findViewById(R.id.tv_desc)
//                var btnYes:TextView=dialogView.findViewById(R.id.btn_yes)
//                var btnNo:TextView=dialogView.findViewById(R.id.btn_no)
//
//                //set title for alert dialog
////                builder.setTitle("Sign Out")
////                //set message for alert dialog
////                builder.setMessage("Are you sure you want to sign out?")
////                builder.setIcon(android.R.drawable.ic_dialog_alert)
//
//                dialogIcon.setImageResource(R.drawable.ic_icon_logout)
//                dialogTitle.text="Log Out"
//                dialogDesc.text="Do you want to Log Out from the application ?"
//
//                btnYes.setOnClickListener {
//                    signout()
//                }
//                btnNo.setOnClickListener {
//                    alertDialog.dismiss()
//                }
//
//                //performing positive action
////                builder.setPositiveButton("Yes"){dialogInterface, which ->
////                    signout()
////                }
////                //performing cancel action
////                builder.setNeutralButton("Cancel"){dialogInterface , which ->
////
////                }
////                //performing negative action
////                builder.setNegativeButton("No"){dialogInterface, which ->
////                }
//                // Create the AlertDialog
//
//                // Set other dialog properties
//                alertDialog.setCancelable(false)
//                alertDialog.setCanceledOnTouchOutside(false)
//                alertDialog.show()
//            }

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
            tvUserName.text=tinyDB.getString("userName")
            tvUserPhoneNumber.text="+91 " + tinyDB.getString("userMobile")
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

                    startActivity(Intent(this, WorkInProgressActivity::class.java))
                    overridePendingTransition(0,0)

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

    override fun onResume() {
        super.onResume()
        _binding.bottomNavigationView.menu.getItem(3).isEnabled = false
        _binding.bottomNavigationView.menu.getItem(3).isChecked = true
    }
    private fun signout(){
        BuildUpClient.authToken=null
        sharedPrefrences.edit {
            putString("token", null)
        }
//                sharedPrefrences.edit {
//                    remove("token")
//                }
        startActivity(Intent(this, LoginSignupSelectorActivity::class.java))
    }

    private fun isNetworkAvailable(): Boolean {
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)

        return (capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))

    }
    private fun drawLayout() {
        if (isNetworkAvailable()) {
            Log.d("internet","internet")
            _binding.mainLayout.visibility=View.VISIBLE
            _binding.noInternetLayout.visibility=View.GONE
        } else {
            Log.d("internet","no internet")
            _binding.mainLayout.visibility=View.GONE
            _binding.noInternetLayout.visibility=View.VISIBLE
        }
    }
}