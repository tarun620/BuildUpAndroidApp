package com.example.buildup.ui.Products.layouts

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.buildup.AuthViewModel
import com.example.buildup.R
import com.example.buildup.databinding.ActivityProductBinding
import com.example.buildup.ui.BottomNavigation.CartActivity
import com.example.buildup.ui.BottomNavigation.WishlistActivity
import com.example.buildup.ui.Products.adapters.ProductViewPagerAdapter
import com.taufiqrahman.reviewratings.BarLabels
import java.util.*

class ProductActivity : AppCompatActivity() {
    private lateinit var _binding:ActivityProductBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var adapter: ProductViewPagerAdapter
//    private lateinit var productImageList: ArrayList<String>
    private lateinit var productId:String
    private var inCart=false
    private var isWishlisted=false
    private val colors = intArrayOf(
        Color.parseColor("#43D5A2"),
        Color.parseColor("#43D5A2"),
        Color.parseColor("#43D5A2"),
        Color.parseColor("#FFD56A"),
        Color.parseColor("#FF0000")
    )

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

        authViewModel.getProduct(productId,isBrand = true,inCart = true,isWishlisted = true)
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
        getRating()

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
//                    Toast.makeText(this@ProductActivity,"button clicked",Toast.LENGTH_SHORT).show()
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
        authViewModel.getProduct(productId,isBrand = true,inCart = true,isWishlisted = true)
        authViewModel.respProduct.observe({lifecycle}){
            _binding.apply {
                //TODO :  Brand name is not included yet
                //TODO : Description is not included yet
                tvProductName.text=it.product?.brand?.name + " " + it.product?.name
//                tvDescription.text=it.product?.description
                tvProductPrice.text="Rs. " + it.product?.amount.toString()
                tvProductMRP.text="Rs. " + it.product?.mrp.toString()
                setViewPagerAdapter(it.product?.image!!)
                if(it.product?.inCart!!){
                    Log.d("inCartFunc","true")
                    inCart=true
                    _binding.addToCartButton.text="Go To Cart"
                }
                if(!it.product?.inCart!!){
                    inCart=false
                    _binding.addToCartButton.text="Add To Cart"
                }
                if(it.product?.isWishlisted!!)
                    isWishlisted=true
            }
        }
    }

    private fun getRating(){
        authViewModel.getProductRating(productId)
        authViewModel.respGetProductRating.observe({lifecycle }){
            if(it?.success!!){
                _binding.apply {
                    ratingReviewsGraph.createRatingBars(it.ratings?.total!!, BarLabels.STYPE1, colors, it.ratings!!.count.reversed().toIntArray())

                    tvRating.text=it.ratings?.avg.toString()
                    val totalReviews=it.ratings?.total
                    if(totalReviews==1){
                        tvTotalReviews.text=totalReviews.toString() + " rating"
                        tvTotalReviewsBottom.text=totalReviews.toString() + " rating"
                    }
                    else{
                        tvTotalReviews.text=totalReviews.toString() + " ratings"
                        tvTotalReviewsBottom.text=totalReviews.toString() + " ratings"
                    }

                    tvRatingsReviews.text=it.ratings?.avg.toString()

                }
            }
            else
                Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
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

    override fun onResume() {
        super.onResume()
        setPageContent()
        if(inCart) {
//            Log.d("inCart","true")
            _binding.addToCartButton.text="Go To Cart"
        }
        else{
//            Log.d("inCart","false")
            _binding.addToCartButton.text="Add to Cart"

        }
    }
}