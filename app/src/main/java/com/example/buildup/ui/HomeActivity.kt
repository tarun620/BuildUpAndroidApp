package com.example.buildup.ui

import android.content.Intent
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
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.api.models.responsesAndData.products.productsEntities.ProductCategoryIdData
import com.example.buildup.AuthViewModel
import com.example.buildup.R
import com.example.buildup.TinyDB
import com.example.buildup.databinding.ActivityCartBinding
import com.example.buildup.databinding.ActivityHomeBinding
import com.example.buildup.ui.Address.layouts.AddressesActivity
import com.example.buildup.ui.BottomNavigation.CartActivity
import com.example.buildup.ui.BottomNavigation.ProfileActivity
import com.example.buildup.ui.BottomNavigation.WishlistActivity
import com.example.buildup.ui.Cart.adapters.CartAdapter
import com.example.buildup.ui.Orders.layouts.OrdersActivity
import com.example.buildup.ui.Products.adapters.ProductCategoryAdapter
import com.example.buildup.ui.Products.adapters.ProductCategoryAdapterNew
import com.example.buildup.ui.Products.adapters.ProductSubCategoryAdapter
import com.example.buildup.ui.Products.adapters.RecentViewedProductsAdapter
import com.example.buildup.ui.Products.layouts.ProductActivity
import com.example.buildup.ui.Products.layouts.ProductCategoryActivity
import com.example.buildup.ui.Products.layouts.ProductsActivity
import com.example.buildup.ui.Property.adapters.PropertyAdapter
import com.google.android.material.navigation.NavigationView

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var _binding: ActivityHomeBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var brandAdapter: BrandAdapter
    private lateinit var productCategoryAdapter: ProductCategoryAdapterNew
    private lateinit var recentViewedProductsAdapter: RecentViewedProductsAdapter
    private var doubleBackToExitPressedOnce = false
    private lateinit var tinyDB : TinyDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityHomeBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)
        setContentView(_binding.root)

        brandAdapter = BrandAdapter { openBrand(it) }
        _binding.brandsRecyclerView.layoutManager = GridLayoutManager(this,3)
        _binding.brandsRecyclerView.adapter = brandAdapter

        productCategoryAdapter = ProductCategoryAdapterNew { openProductCategory(it) }
        _binding.productsCategoriesRecyclerView.layoutManager = GridLayoutManager(this,2)
        _binding.productsCategoriesRecyclerView.adapter = productCategoryAdapter

        recentViewedProductsAdapter=RecentViewedProductsAdapter { onProductClicked(it) }
        _binding.recentlyViewedRecyclerView.layoutManager=LinearLayoutManager(this,
            RecyclerView.HORIZONTAL,false)
        _binding.recentlyViewedRecyclerView.adapter=recentViewedProductsAdapter

        tinyDB = TinyDB(this)


        _binding.buttonDashboard.setOnClickListener {
            this._binding.drawer.openDrawer(Gravity.LEFT)
        }
        _binding.bottomNavigationView.background = null

        setupNavigationDrawer()
        setupBottomNavigationBar()
        getBrands()
        getRecentlyViewedProducts()
        getProductCategories()




    }

    private fun getBrands(){
        authViewModel.getBrands(true,6)
        authViewModel.respGetBrands.observe({lifecycle}){
            if(it?.success!!){
                Toast.makeText(this,"Brands fetched Successfully",Toast.LENGTH_SHORT).show()
                brandAdapter.submitList(it.brands)
            }
            else
                Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()

        }
    }
    private fun openBrand(brandName:String?){
//        val brandArray=ArrayList<String?>()
//        brandArray.add(brandName)
//        val intent=Intent(this,ProductsActivity::class.java)
//        intent.putExtra("brandArray",brandArray)
//        startActivity(intent)
    }

    fun getProductCategories() {
        authViewModel.getProductCategories(true,6)
        authViewModel.respProductCategoryArray.observe({ lifecycle }) {
            if (it?.success!!) {
                Toast.makeText(this, "product categories fetching successful", Toast.LENGTH_SHORT)
                    .show()
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

                R.id.nav_wishlist -> {

                    startActivity(Intent(this, WishlistActivity::class.java))


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
                startActivity(Intent(this,WorkInProgressActivity::class.java))
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
                startActivity(Intent(this,AddressesActivity::class.java))
            }
            R.id.nav_help_center -> {
                startActivity(Intent(this,WorkInProgressActivity::class.java))
            }

            R.id.nav_privacy_policy -> {
                startActivity(Intent(this,WorkInProgressActivity::class.java))
            }

            R.id.nav_legal -> {
                startActivity(Intent(this,WorkInProgressActivity::class.java))
            }

            R.id.nav_sign_out -> {
                startActivity(Intent(this,WorkInProgressActivity::class.java))
            }

        }
        _binding.drawer.closeDrawer(GravityCompat.START)
        return true
    }


}