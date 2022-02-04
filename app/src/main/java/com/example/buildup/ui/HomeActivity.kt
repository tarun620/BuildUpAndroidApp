package com.example.buildup.ui

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.api.BuildUpClient
import com.example.api.models.responsesAndData.brand.BrandNameId
import com.example.api.models.responsesAndData.products.productsEntities.ProductCategoryIdData
import com.example.buildup.AuthViewModel
import com.example.buildup.R
import com.example.buildup.TinyDB
import com.example.buildup.databinding.ActivityHomeBinding
import com.example.buildup.ui.BottomNavigation.CartActivity
import com.example.buildup.ui.BottomNavigation.ProfileActivity
import com.example.buildup.ui.BottomNavigation.WishlistActivity
import com.example.buildup.ui.Brand.BrandAdapter
import com.example.buildup.ui.LoginSignup.LoginSignupSelectorActivity
import com.example.buildup.ui.LottieAnimation.WorkInProgressActivity
import com.example.buildup.ui.Orders.layouts.OrdersActivity
import com.example.buildup.ui.Products.adapters.ProductCategoryAdapterNew
import com.example.buildup.ui.Products.adapters.RecentViewedProductsAdapter
import com.example.buildup.ui.Products.layouts.*
import com.google.android.material.navigation.NavigationView

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    companion object {
        var PREFS_FILE_AUTH = "prefs_auth"
        var PREFS_KEY_TOKEN = "token"
    }
    private lateinit var _binding: ActivityHomeBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var brandAdapter: BrandAdapter
    private lateinit var productCategoryAdapter: ProductCategoryAdapterNew
    private lateinit var recentViewedProductsAdapter: RecentViewedProductsAdapter
    private var doubleBackToExitPressedOnce = false
    private lateinit var tinyDB : TinyDB
    private lateinit var sharedPrefrences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityHomeBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)
        setContentView(_binding.root)

        sharedPrefrences = getSharedPreferences(PREFS_FILE_AUTH, Context.MODE_PRIVATE)


        brandAdapter = BrandAdapter { openBrand(it) }
        _binding.brandsRecyclerView.layoutManager = GridLayoutManager(this,3)
        _binding.brandsRecyclerView.adapter = brandAdapter
        _binding.brandsRecyclerView.isNestedScrollingEnabled=false

        productCategoryAdapter = ProductCategoryAdapterNew { openProductCategory(it) }
        _binding.productsCategoriesRecyclerView.layoutManager = GridLayoutManager(this,2)
        _binding.productsCategoriesRecyclerView.adapter = productCategoryAdapter
        _binding.productsCategoriesRecyclerView.isNestedScrollingEnabled=false

        recentViewedProductsAdapter=RecentViewedProductsAdapter { onProductClicked(it) }
        _binding.recentlyViewedRecyclerView.layoutManager=LinearLayoutManager(this, RecyclerView.HORIZONTAL,false)
        _binding.recentlyViewedRecyclerView.adapter=recentViewedProductsAdapter
        _binding.recentlyViewedRecyclerView.isNestedScrollingEnabled=false

        tinyDB = TinyDB(this)


        _binding.buttonDashboard.setOnClickListener {
            this._binding.drawer.openDrawer(Gravity.LEFT)
        }
        _binding.bottomNavigationView.background = null

        _binding.bottomNavigationView.menu.getItem(0).isEnabled = false
        _binding.bottomNavigationView.menu.getItem(0).isChecked = true

        _binding.viewAllCategories.setOnClickListener {
            startActivity(Intent(this,ProductCategoryActivity::class.java))
        }

        _binding.searchBtn.setOnClickListener {
            startActivity(Intent(this,SearchActivity::class.java))
        }
        _binding.btnAddProducts.setOnClickListener {
            startActivity(Intent(this,ProductCategoryActivity::class.java))
        }

        setupNavigationDrawer()
        setupBottomNavigationBar()
        getAppData()
        getBrands()
        getRecentlyViewedProducts()
        getProductCategories()




    }

    private fun getAppData(){
        authViewModel.getAppData("home")
        authViewModel.respGetAppData.observe({lifecycle}){
            if(it?.success!!){
                _binding.apply {
                    heading1.text=it.homeData!!.heading1
                    heading2.text=it.homeData!!.heading2
                    subHeading.text=it.homeData!!.subHeading
                    text1.text=it.homeData!!.text1
                    text2.text=it.homeData!!.text2
                }
            }
            else
                Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
        }
    }
    private fun getBrands(){
        authViewModel.getBrands(true,6)
        authViewModel.respGetBrands.observe({lifecycle}){
            if(it?.success!!){
                _binding.mainLayout.visibility=View.VISIBLE
                _binding.idPBLoading.visibility=View.GONE
                brandAdapter.submitList(it.brands)
            }
            else
                Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()

        }
    }
    private fun openBrand(brandNameId: BrandNameId?){
        val singleBrandArray=ArrayList<String?>()
        val brandName=brandNameId?.name
        singleBrandArray.add(brandNameId?.id)
        val intent=Intent(this,ProductsActivity::class.java)
        intent.putExtra("singleBrandArray",singleBrandArray)
        intent.putExtra("brandName",brandName)
        startActivity(intent)
    }

    fun getProductCategories() {
        authViewModel.getProductCategories(true,6)
        authViewModel.respProductCategoryArray.observe({ lifecycle }) {
            if (it?.success!!) {
                productCategoryAdapter.submitList(it.productCategories)
                productCategoryAdapter.notifyDataSetChanged()

            } else {
                Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
            }
        }
//        return propertyCategoryId!!
    }

    fun openProductCategory(productCategoryIdData: ProductCategoryIdData) {
        val intent=Intent(this,ProductsActivity::class.java)
        intent.putExtra("productCategoryName",productCategoryIdData.productCategoryName)
        intent.putExtra("productCategoryId",productCategoryIdData.productCategoryId)
        startActivity(intent)

    }

    private fun getRecentlyViewedProducts(){
        authViewModel.getRecentlyViewedProducts()
        authViewModel.respGetRecentlyViewedProducts.observe({lifecycle}){
            if(it?.success!!){
                if(it.products!!.isEmpty()){
                    _binding.recentlyViewedLayout.visibility= View.GONE
                    _binding.recentlyViewedText.visibility= View.GONE
                }
                else{
                    _binding.recentlyViewedLayout.visibility= View.VISIBLE
                    _binding.recentlyViewedText.visibility= View.VISIBLE
                }
                recentViewedProductsAdapter.submitList(it.products)
            }
            else{
                Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onProductClicked(productId:String?){
        val intent= Intent(this, ProductActivity::class.java)
        intent.putExtra("productId",productId)
        intent.putExtra("productClickedFromHomeIntent",true)
        startActivity(intent)
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


                }
                R.id.nav_cart -> {

                    startActivity(Intent(this, CartActivity::class.java))

                }

                R.id.nav_property -> {

                    startActivity(Intent(this, WorkInProgressActivity::class.java))


                }

                R.id.nav_profile -> {

                    startActivity(Intent(this, ProfileActivity::class.java))

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
                Glide.with(this@HomeActivity)
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
                startActivity(Intent(this, WorkInProgressActivity::class.java))
            }

            R.id.nav_all_categories -> {
                startActivity(Intent(this,ProductCategoryActivity::class.java))
            }

            R.id.nav_my_order -> {
                startActivity(Intent(this,OrdersActivity::class.java))
            }

            R.id.nav_my_cart -> {
                startActivity(Intent(this,CartActivity::class.java))
            }

            R.id.nav_my_wishlist -> {
                startActivity(Intent(this,WishlistActivity::class.java))
            }

            R.id.nav_my_account -> {
                startActivity(Intent(this,ProfileActivity::class.java))
            }
            R.id.nav_about_buildUp -> {
//                startActivity(Intent(this, WorkInProgressActivity::class.java))
                val i = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.buildup.org.in/about")
                )
                startActivity(i)
            }


            R.id.nav_sign_out -> {
                val builder = AlertDialog.Builder(this)
                //set title for alert dialog
                builder.setTitle("Sign Out")
                //set message for alert dialog
                builder.setMessage("Are you sure you want to sign out?")
                builder.setIcon(android.R.drawable.ic_dialog_alert)

                //performing positive action
                builder.setPositiveButton("Yes"){dialogInterface, which ->
                    signout()
                }
                //performing cancel action
                builder.setNeutralButton("Cancel"){dialogInterface , which ->

                }
                //performing negative action
                builder.setNegativeButton("No"){dialogInterface, which ->
                }
                // Create the AlertDialog
                val alertDialog: AlertDialog = builder.create()
                // Set other dialog properties
                alertDialog.setCancelable(false)
                alertDialog.show()
            }

        }
        _binding.drawer.closeDrawer(GravityCompat.START)
        return true
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
    override fun onResume() {
        super.onResume()
        _binding.bottomNavigationView.menu.getItem(0).isEnabled = false
        _binding.bottomNavigationView.menu.getItem(0).isChecked = true
    }
}