package com.example.buildup.ui.BottomNavigation

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buildup.AuthViewModel
import com.example.buildup.R
import com.example.buildup.databinding.ActivityCartBinding
import com.example.buildup.ui.Address.layouts.AddressesActivity
import com.example.buildup.ui.BottomNavigation.CartActivity.PropertyActivity.Companion.PREFS_FILE_AUTH
import com.example.buildup.ui.Cart.adapters.CartAdapter
import com.example.buildup.ui.Orders.layouts.OrdersActivity
import com.example.buildup.ui.Products.layouts.ProductActivity
import com.example.buildup.ui.Property.layouts.PropertiesActivity
import com.example.buildup.ui.Property.layouts.PropertyActivity

class CartActivity : AppCompatActivity() {
    class PropertyActivity : AppCompatActivity() {
        companion object {
            var PREFS_FILE_AUTH = "prefs_property"
            var PREFS_KEY_TOKEN = "propertyId"
        }
    }
    private lateinit var _binding: ActivityCartBinding
    private lateinit var cartAdapter: CartAdapter
    private lateinit var authViewModel: AuthViewModel
    private var propertyId:String?=null
    private lateinit var sharedPrefrences: SharedPreferences
    private var cartValue:Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCartBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)
        sharedPrefrences = getSharedPreferences(PREFS_FILE_AUTH, Context.MODE_PRIVATE)

        cartAdapter= CartAdapter({openProductActivity(it)},{increaseProductQuantity(it)},{decreaseProductQuantity(it)},{removeProductFromCart(it)})
        _binding.cartItemRecyclerView.layoutManager= LinearLayoutManager(this)
        _binding.cartItemRecyclerView.adapter=cartAdapter

        setContentView(_binding.root)

        if (getSupportActionBar() != null) {
            getSupportActionBar()?.hide();
        }

        propertyId= sharedPrefrences.getString("propertyId",null)
        Log.d("propertyId ",propertyId!!)
//        if(propertyId!=null){
//            val intent=Intent(this,PropertiesActivity::class.java)
//            startActivity(intent)
//        }

        _binding.apply {
            bottomNavigationView.background = null
            setupBottomNavigationBar()
//        _binding.bottomNavigationView.selectedItemId(R.id.nav_cart)
            bottomNavigationView.menu.getItem(1).isEnabled = false
            bottomNavigationView.menu.getItem(1).isChecked = true


            backBtn.setOnClickListener {
//            startActivity(Intent(this,PropertiesActivity::class.java))
                finish()
            }

            btnChangeAddress.setOnClickListener {
                val intent=Intent(this@CartActivity, AddressesActivity::class.java)
                startActivity(intent)
            }

            btnCheckout.setOnClickListener {
                if(propertyId==null){
                    Toast.makeText(this@CartActivity,"property Id is null",Toast.LENGTH_SHORT).show()

                }
                else{
                    authViewModel.createOrder(propertyId!!,"cod","null",true)
                    authViewModel.respCreateOrder.observe({lifecycle}){
                        if(it?.success!!){
                            Toast.makeText(this@CartActivity,"Order Placed Successfully",Toast.LENGTH_SHORT).show()
                            val intent=Intent(this@CartActivity, OrdersActivity::class.java)
                            startActivity(intent)
                        }
                        else{
                            Toast.makeText(this@CartActivity,it.error,Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }


        getProductsFromCart()
//        setTotalCartValue()

    }

    private fun getProductsFromCart(){
        authViewModel.getProductsFromCart()

        authViewModel.respGetProductsFromCart.observe({lifecycle}){
            if(it?.success!!){
                Toast.makeText(this,"cart items fetched successfully.", Toast.LENGTH_SHORT).show()
                cartAdapter.submitList(it.items)
                cartValue=0
                for(i in it.items!!){
                    cartValue+=(i.product.amount) * i.quantity
                    Log.d("cartValue",cartValue.toString())
                }
                _binding.apply {
                    tvSubtotal.text=cartValue.toString()
                    tvShipping.text="50" //hardcoded
                    tvTotal.text=(cartValue+50).toString()
                }
            }
            else{
                Toast.makeText(this,it.error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openProductActivity(productId:String?){
        val intent= Intent(this, ProductActivity::class.java)
        intent.putExtra("productId",productId)
        startActivity(intent)

    }
    private fun increaseProductQuantity(productId: String?){
        var currQuantity=1
        authViewModel.respGetProductsFromCart.observe({lifecycle}){
            for(i in it?.items!!){
                if(i.product.id==productId)
                currQuantity=i.quantity
            }
        }
        authViewModel.updateProductQuantityCart(productId!!,currQuantity+1)
        authViewModel.respUpdateProductQuantityCart.observe({lifecycle}){
            if(it?.success!!)
                getProductsFromCart()
            else
                Toast.makeText(this,it.error, Toast.LENGTH_SHORT).show()
        }

    }

    private fun decreaseProductQuantity(productId: String?) {
        var currQuantity = 1
        authViewModel.respGetProductsFromCart.observe({ lifecycle }) {
            for (i in it?.items!!) {
                if (i.product.id == productId)
                    currQuantity = i.quantity
            }
        }
        if (currQuantity == 1) {
            Toast.makeText(
                this,
                "Minimum Quantity cannot be less than 1, Delete the product from delete icon",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            authViewModel.updateProductQuantityCart(productId!!, currQuantity - 1)
            authViewModel.respUpdateProductQuantityCart.observe({ lifecycle }) {
                if (it?.success!!)
                    getProductsFromCart()
                else
                    Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun removeProductFromCart(productId:String?){
        authViewModel.removeProductFromCart(productId!!)
        authViewModel.respRemoveProductFromCart.observe({lifecycle}){
            if(it?.success!!)
                getProductsFromCart()
            else
                Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
        }
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