package com.example.buildup.ui.Products.layouts

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
import kotlin.math.floor


class ProductActivity : AppCompatActivity() {
    private lateinit var _binding:ActivityProductBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var adapter: ProductViewPagerAdapter
//    private lateinit var productImageList: ArrayList<String>
    private lateinit var productId:String
    private var productClickedFromCartIntent:Boolean=false
    private var productClickedFromHomeIntent:Boolean=false
    private var productClickedFromSearchIntent:Boolean=false
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

        val window: Window = window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        setContentView(_binding.root)

        productId= intent.getStringExtra("productId")!!
        productClickedFromCartIntent=intent.getBooleanExtra("productClickedFromCartIntent",false)
        productClickedFromHomeIntent=intent.getBooleanExtra("productClickedFromHomeIntent",false)
        productClickedFromSearchIntent=intent.getBooleanExtra("productClickedFromSearchIntent",false)

        Log.d("productId",productId)
        _binding.backBtn.setOnClickListener {
            if(productClickedFromCartIntent || productClickedFromHomeIntent || productClickedFromSearchIntent)
                finish()
            else{
                finish()

//                val intent=Intent(this,ProductsActivity::class.java)
//                intent.putExtra("fromProductActivity",true)
//                startActivity(intent)
            }

        }
        _binding.btnCart.setOnClickListener {
            val intent=Intent(this,CartActivity::class.java)
            startActivity(intent)
        }

        _binding.btnOpenWishlist.setOnClickListener {
            val intent=Intent(this,WishlistActivity::class.java)
            intent.putExtra("fromProductActivity",true)
            startActivity(intent)
        }

//        adapter = ProductViewPagerAdapter(productImageList,this)
//        _binding.productVewPager.adapter = adapter
//        _binding.productVewPager.orientation =ViewPager2.ORIENTATION_HORIZONTAL
//        _binding.circleIndicator.setViewPager(_binding.productVewPager)

//        authViewModel.getProduct(productId,isBrand = true,inCart = true,isWishlisted = true)
//        authViewModel.respProduct.observe({lifecycle}){
//            if(it?.success!!){
//                _binding.mainLayout.visibility=View.VISIBLE
//                _binding.idPBLoading.visibility= View.GONE
//                if(it.product?.inCart!!) {
//                    _binding.addToCartButton.text="Go To Cart"
//                    inCart = true
//                }
//                if(it.product?.isWishlisted!!) {
//                    Log.d("iswishlisted", "true")
//                    _binding.btnWishlist.icon=resources.getDrawable(R.drawable.ic_icon_wishlisted_new6)
//                }
//            }
//            else
//                Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
//
//        }

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

            btnWishlist.setOnClickListener {
//                onWishlistClicked()
                if(isWishlisted){
                    Log.d("wishlist1","reached here")

                    authViewModel.removeProductFromWishlist(productId)
                    authViewModel.respRemoveProductFromWishlist.observe({lifecycle}){
                        if(it?.success!!){
                            _binding.btnWishlist.icon=resources.getDrawable(R.drawable.ic_icon_wishlist_new)
                            isWishlisted=false
                        }
                        else
                            Toast.makeText(this@ProductActivity,it.error,Toast.LENGTH_SHORT).show()
                    }
                }
                else{
//                    val intent=Intent(this@ProductActivity,WishlistActivity::class.java)
//                    startActivity(intent)

                    authViewModel.addProductToWishlist(productId)
                    authViewModel.respAddProductToWishlist.observe({lifecycle}){
                        if(it?.success!!){
                            _binding.btnWishlist.icon=resources.getDrawable(R.drawable.ic_icon_wishlisted_new6)
                            isWishlisted=true
                        }
                        else
                            Toast.makeText(this@ProductActivity,it.error,Toast.LENGTH_SHORT).show()

                    }
                }
            }
        }

    }

    private fun setPageContent(){
        authViewModel.getProduct(productId,isBrand = true,inCart = true,isWishlisted = true)
        authViewModel.respProduct.observe({lifecycle}){
            if(it.success!!){
                _binding.apply {
                    _binding.mainLayout.visibility=View.VISIBLE
                    _binding.idPBLoading.visibility= View.GONE

                    tvProductName.text=it.product?.brand?.name + " " + it.product?.name
                    tvDescription.text=it.product?.description
                    var productPrice:Int=it.product?.amount!!
                    var productMRP:Int=it.product?.mrp!!
                    tvProductPrice.text="₹ " + productPrice.toString()
                    tvProductMRP.text="₹ " + productMRP.toString()
                    tvProductMRP.paintFlags = tvProductMRP.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    var discount:Double = ((productMRP-productPrice).toDouble()/productMRP)*100
                    discount= floor(discount)
                    tvDiscount.text=discount.toInt().toString() + "% OFF"

                    setViewPagerAdapter(it.product?.image!!)
                    if(it.product?.isWishlisted!!){
                        isWishlisted=true
                        _binding.btnWishlist.icon=resources.getDrawable(R.drawable.ic_icon_wishlisted_new6)
                    }
                    else{
                        isWishlisted=false
                        _binding.btnWishlist.icon=resources.getDrawable(R.drawable.ic_icon_wishlist_new)
                    }

//                    Log.d("inCart",it.product?.inCart.toString())
                    if(it.product?.inCart!!){
                        Log.d("inCartFunc","true")
                        inCart=true
                        _binding.addToCartButton.text="Go To Cart"
                    }
                    else{
                        inCart=false
                        _binding.addToCartButton.text="Add To Cart"
                    }
                }
            }
            else
                Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()

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
//        recreate()
        setPageContent()
        if(inCart) {
//            Log.d("inCart","true")
            _binding.addToCartButton.text="Go To Cart"
        }
        else{
//            Log.d("inCart","false")
            _binding.addToCartButton.text="Add To Cart"

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(productClickedFromCartIntent || productClickedFromHomeIntent || productClickedFromSearchIntent)
            finish()
        else{

            finish()
//            val intent=Intent(this,ProductsActivity::class.java)
//            intent.putExtra("fromProductActivity",true)
//            startActivity(intent)
        }

    }
}