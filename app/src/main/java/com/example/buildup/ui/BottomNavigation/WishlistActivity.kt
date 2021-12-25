package com.example.buildup.ui.BottomNavigation

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.api.models.responsesAndData.wishlist.WishlistData
import com.example.buildup.AuthViewModel
import com.example.buildup.R
import com.example.buildup.databinding.ActivityWishlistBinding
import com.example.buildup.ui.HomeActivity
import com.example.buildup.ui.Products.layouts.ProductActivity
import com.example.buildup.ui.Wishlist.adapters.WishlistAdapter


class  WishlistActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityWishlistBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var wishlistAdapter: WishlistAdapter
    private var propertyId:String=""
    private var inCart:Boolean=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityWishlistBinding.inflate(layoutInflater)

        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)

        wishlistAdapter= WishlistAdapter({openProductActivity(it)},{removeProductFromWishlist(it)},{addProductToCartFromWishlist(it)})
        _binding.wishListRecyclerView.layoutManager= LinearLayoutManager(this)
        _binding.wishListRecyclerView.adapter=wishlistAdapter
        setContentView(_binding.root)

//        _binding.bottomNavigationView.background = null
//        setupBottomNavigationBar()

//        _binding.bottomNavigationView.menu.getItem(2).isEnabled = false
//        _binding.bottomNavigationView.menu.getItem(2).isChecked = true


        _binding.backBtn.setOnClickListener {
            finish()
//            startActivity(Intent(this,WishlistActivity::class.java))
        }
        _binding.cartBtn.setOnClickListener {
            val intent=Intent(this,CartActivity::class.java)
            startActivity(intent)
        }

        _binding.apply {
            removeBtn.setOnClickListener {
                val builder = AlertDialog.Builder(this@WishlistActivity)
                //set title for alert dialog
                builder.setTitle("Delete Wishlist")
                //set message for alert dialog
                builder.setMessage("Do you want to delete your Entire Wishlist?")
                builder.setIcon(android.R.drawable.ic_dialog_alert)

                //performing positive action
                builder.setPositiveButton("Yes"){dialogInterface, which ->
//                    (application as MyApplication).clearQueue()
                    deleteWishlist()
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

        getWishlist()

    }

    private fun getWishlist(){
        authViewModel.getWishlist()
        authViewModel.respGetWishlist.observe({lifecycle}){
            if(it?.success!!){
                Toast.makeText(this,"wishlist items fetched successfully.", Toast.LENGTH_SHORT).show()
                if(it.products?.size!! == 1)
                    _binding.itemCount.text=it.products?.size.toString() + " " + "item"
                else
                    _binding.itemCount.text=it.products?.size.toString() + " " + "items"
                wishlistAdapter.submitList(it.products)

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

    private fun removeProductFromWishlist(productId: String?){
        authViewModel.removeProductFromWishlist(productId!!)
        authViewModel.respRemoveProductFromWishlist.observe({lifecycle}){
            if(it?.success!!)
                getWishlist()
            else
                Toast.makeText(this,it.error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun addProductToCartFromWishlist(wishlistData: WishlistData){
        if(wishlistData.inCart){
            startActivity(Intent(this,CartActivity::class.java))
        }
        else{
            authViewModel.addProductToCart(wishlistData.productId,true)
            authViewModel.respAddProductToCart.observe({lifecycle}){
                if(it?.success!!){
                    Toast.makeText(this,"RecentProduct added to cart successsfully.",Toast.LENGTH_SHORT).show()
                    getWishlist()
                }
                else
                    Toast.makeText(this,it.error, Toast.LENGTH_SHORT).show()

            }
        }

    }
//    private fun setupBottomNavigationBar() {
//
//        _binding.bottomNavigationView.setOnNavigationItemSelectedListener {
//            when (it.itemId) {
//
//                R.id.nav_home -> {
//                    startActivity(Intent(this, HomeActivity::class.java))
//
//
//                }
//                R.id.nav_cart -> {
//                    startActivity(Intent(this, CartActivity::class.java))
//
//                }
//
//                R.id.nav_property -> {
//
//
//                }
//
//                R.id.nav_profile -> {
//                    startActivity(Intent(this, ProfileActivity::class.java))
//
//                }
//            }
//            true
//        }
//    }

    private fun deleteWishlist(){
        authViewModel.deleteWishlist()
        authViewModel.respDeleteWishlist.observe({lifecycle}){
            if(it?.success!!) {
                _binding.itemCount.text="0 items"
                wishlistAdapter.submitList(null)
            }
            else
                Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
        }
    }

}