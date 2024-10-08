package com.example.buildup.ui.Property.layouts

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.edit
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.example.api.BuildUpClient
import com.example.buildup.AuthViewModel
import com.example.buildup.R
import com.example.buildup.TinyDB
import com.example.buildup.databinding.ActivityPropertiesBinding
import com.example.buildup.ui.Address.layouts.AddressesActivity
import com.example.buildup.ui.BottomNavigation.CartActivity
import com.example.buildup.ui.BottomNavigation.ProfileActivity
import com.example.buildup.ui.Property.adapters.PropertyAdapter
import com.example.buildup.ui.BottomNavigation.WishlistActivity
import com.example.buildup.ui.HomeActivity
import com.example.buildup.ui.LoginSignup.LoginSignupSelectorActivity
import com.example.buildup.ui.Orders.layouts.OrdersActivity
import com.example.buildup.ui.Products.layouts.ProductCategoryActivity
import com.example.buildup.ui.LottieAnimation.WorkInProgressActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.navigation.NavigationView

class PropertiesActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener  {
    companion object {
        var PREFS_FILE_AUTH = "prefs_auth"
        var PREFS_KEY_TOKEN = "token"
    }
    //    private lateinit var _binding:ActivityPropertiesBinding
    private lateinit var _binding: ActivityPropertiesBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var propertyAdapter: PropertyAdapter
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var doubleBackToExitPressedOnce = false
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var tinyDB : TinyDB
    private lateinit var sharedPrefrences: SharedPreferences




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_logged_in)

        _binding = ActivityPropertiesBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        tinyDB = TinyDB(this)
        sharedPrefrences = getSharedPreferences(PREFS_FILE_AUTH, Context.MODE_PRIVATE)


        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        propertyAdapter = PropertyAdapter { openProperty(it) }
        setupNavigationDrawer()
        _binding.bottomNavigationView.menu.getItem(2).isEnabled = false
        _binding.bottomNavigationView.menu.getItem(2).isChecked = true


        _binding.propertyRecyclerView.layoutManager = LinearLayoutManager(this)
        _binding.propertyRecyclerView.adapter = propertyAdapter
        propertyAdapter.notifyDataSetChanged()

        _binding.buttonDashboard.setOnClickListener {
            this._binding.drawer.openDrawer(Gravity.LEFT)
        }


        _binding.bottomNavigationView.background = null

//        swipeRefreshLayout=findViewById(R.id.swipeRefreshLayout)


        _binding.addPropertyBtn.setOnClickListener {
            val intent = Intent(this, AddPropertyActivity::class.java)
            startActivity(intent)
        }

//        _binding.buttonProfile.setOnClickListener {
//            val intent=Intent(this,ProfileActivity::class.java)
//            startActivity(intent)
//        }

        getProperties()
        setupBottomNavigationBar()

//        swipeRefreshLayout.setOnRefreshListener {
//            Toast.makeText(this,"refreshed.",Toast.LENGTH_SHORT).show()
//            getProperties()
//            swipeRefreshLayout.isRefreshing = false
//        }


    }

    fun getProperties() {
        authViewModel.getProperties()
        authViewModel.respPropertyArray.observe({ lifecycle }) {
            if (it?.success!!) {
//                Log.d("completed", it.properties?.get(0)?.completed.toString())
                Toast.makeText(this, "property fetching successful", Toast.LENGTH_SHORT).show()
                Log.d("propertiesActivity", it.properties?.size.toString())
                propertyAdapter.submitList(it.properties)
            } else {
                Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                Log.d("errorLoggedIn", it.error.toString())
            }

        }
    }

    fun openProperty(propertyId: String?) {
        val intent = Intent(this, PropertyActivity::class.java)
        intent.putExtra("propertyId", propertyId)
        startActivity(intent)
//        Toast.makeText(this,"propertyId:${propertyId}",Toast.LENGTH_SHORT).show()
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


                }

                R.id.nav_profile -> {

                    startActivity(Intent(this, ProfileActivity::class.java))
                    finish()

                }
            }
            true
        }
    }

    private fun setupNavigationDrawer() {
        val drawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            _binding.drawer,
            null,
            R.string.open,
            R.string.close
        ) {

        }

        drawerToggle.isDrawerIndicatorEnabled = true
        _binding.drawer.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        supportActionBar?.setHomeButtonEnabled(true);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_dashboard)
        _binding.drawer.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

            }

        })

        _binding.apply {
            var headerView=navView.getHeaderView(0)
            val profileImage=headerView.findViewById<ImageView>(R.id.iv_profileImage)
            val userName=headerView.findViewById<TextView>(R.id.tv_fullname)
            val userMobile=headerView.findViewById<TextView>(R.id.tv_phoneNo)

            if(!tinyDB.getString("userProfileImage").isNullOrBlank())
                Glide.with(this@PropertiesActivity)
                    .load(tinyDB.getString("userProfileImage"))
                    .circleCrop()
                    .placeholder(R.drawable.ic_profile)
                    .into(profileImage)
            userName.text=tinyDB.getString("userName")
            userMobile.text="+91 " + tinyDB.getString("userMobile")
        }





        _binding.navView.setNavigationItemSelectedListener(this)


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.nav_build_up_plus -> {
                startActivity(Intent(this@PropertiesActivity, WorkInProgressActivity::class.java))
            }

            R.id.nav_all_categories -> {
                startActivity(Intent(this@PropertiesActivity,ProductCategoryActivity::class.java))
            }

            R.id.nav_my_order -> {
                startActivity(Intent(this@PropertiesActivity,OrdersActivity::class.java))
            }

            R.id.nav_my_cart -> {
                startActivity(Intent(this@PropertiesActivity,CartActivity::class.java))
            }

            R.id.nav_my_wishlist -> {
                startActivity(Intent(this@PropertiesActivity,WishlistActivity::class.java))
            }

            R.id.nav_my_account -> {
                startActivity(Intent(this@PropertiesActivity,ProfileActivity::class.java))
            }
            R.id.nav_about_buildUp -> {
//                startActivity(Intent(this@PropertiesActivity,AddressesActivity::class.java))
                val i = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.buildup.org.in/about")
                )
                startActivity(i)
            }

            R.id.nav_sign_out -> {
//                startActivity(Intent(this@PropertiesActivity, WorkInProgressActivity::class.java))
                BuildUpClient.authToken=null
                sharedPrefrences.edit {
                    putString("token", null)
                }
//                sharedPrefrences.edit {
//                    remove("token")
//                }
                startActivity(Intent(this, LoginSignupSelectorActivity::class.java))
            }

        }
        _binding.drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onResume() {
        super.onResume()
        Toast.makeText(this,"On resume Called",Toast.LENGTH_SHORT).show()

        getProperties()

    }


}