package com.example.buildup.ui.Products.layouts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.buildup.AuthViewModel
import com.example.buildup.R
import com.example.buildup.databinding.ActivityProductBinding
import com.example.buildup.ui.BottomNavigation.CartActivity
import com.example.buildup.ui.BottomNavigation.WishlistActivity
import com.example.buildup.ui.Products.adapters.ProductViewPagerAdapter
import java.util.*

class ProductActivity : AppCompatActivity() {
    private lateinit var _binding:ActivityProductBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var adapter: ProductViewPagerAdapter
//    private lateinit var productImageList: ArrayList<String>
    private lateinit var productId:String
    private var inCart=false
    private var isWishlisted=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityProductBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)
//        productImageList = ArrayList()
//
//        productImageList.add("")
//        productImageList.add("")
//        productImageList.add("")

//        setContentView(R.layout.activity_product)
        setContentView(_binding.root)

        if (getSupportActionBar() != null) {
            getSupportActionBar()?.hide();
        }

        productId= intent.getStringExtra("productId")!!

        _binding.backBtn.setOnClickListener {
            finish()
        }
        _binding.btnCart.setOnClickListener {
            val intent=Intent(this,CartActivity::class.java)
            startActivity(intent)
        }

//        adapter = ProductViewPagerAdapter(productImageList,this)
//        _binding.productVewPager.adapter = adapter
//        _binding.productVewPager.orientation =ViewPager2.ORIENTATION_HORIZONTAL
//        _binding.circleIndicator.setViewPager(_binding.productVewPager)

        authViewModel.getProduct(productId)
        authViewModel.respProduct.observe({lifecycle}){
            if(it.product?.inCart!!) {
                _binding.addToCartButton.text="Go To Cart"
                inCart = true
            }
            if(it?.product?.isWishlisted!!) {
                Log.d("iswishlisted", "true")
                _binding.wishlistBtn.setImageResource(R.drawable.ic_frame_28)
            }
        }

        setPageContent()

        _binding.apply {
            addToCartButton.setOnClickListener {
                if(!inCart){
                    authViewModel.addProductToCart(productId,false)
                    authViewModel.respAddProductToCart.observe({lifecycle}){
                        if(it?.success!!){
                            _binding.addToCartButton.text="Go To Cart"
                            inCart=true
                        }
                        else{
                            Toast.makeText(this@ProductActivity,it.error,Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else{
                    val intent=Intent(this@ProductActivity,CartActivity::class.java)
                    startActivity(intent)
                }
            }

            wishlistBtn.setOnClickListener {
                if(!isWishlisted){
                    Log.d("wishlist1","reached here")
                    authViewModel.addProductToWishlist(productId)
                    authViewModel.respAddProductToWishlist.observe({lifecycle}){
                        Log.d("wishlist2","reached here")
                        if(it?.success!!){
                            Log.d("wishlist3","reached here")
                            _binding.wishlistBtn.setImageResource(R.drawable.ic_frame_28)
                            isWishlisted=true
                        }
                    }
                }
                else{
                    val intent=Intent(this@ProductActivity,WishlistActivity::class.java)
                    startActivity(intent)
                }
            }
        }

    }
    private fun setPageContent(){
        authViewModel.getProduct(productId)
        authViewModel.respProduct.observe({lifecycle}){
            _binding.apply {
                //TODO :  Brand name is not included yet
                //TODO : Description is not included yet
                tvProductName.text=it.product?.brand?.name + " " + it.product?.name
//                tvDescription.text=it.product?.description
                tvProductPrice.text="Rs. " + it.product?.amount.toString()
                tvProductMRP.text="Rs. " + it.product?.mrp.toString()
                setViewPagerAdapter(it.product?.image!!)
                if(it.product?.inCart!!)
                    inCart=true
                if(it.product?.isWishlisted!!)
                    isWishlisted=true
            }
        }

    }
    private fun setViewPagerAdapter(images:List<String>){
        adapter = ProductViewPagerAdapter(images as ArrayList<String>,this@ProductActivity)
        _binding.apply {
            productVewPager.adapter = adapter
            productVewPager.orientation =ViewPager2.ORIENTATION_HORIZONTAL
            circleIndicator.setViewPager(_binding.productVewPager)
        }
    }
}