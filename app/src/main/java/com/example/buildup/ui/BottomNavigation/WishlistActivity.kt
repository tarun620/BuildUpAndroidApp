package com.example.buildup.ui.BottomNavigation

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.api.models.responsesAndData.wishlist.Product
import com.example.api.models.responsesAndData.wishlist.WishlistData
import com.example.api.models.responsesAndData.wishlist.WishlistPositionData
import com.example.buildup.AuthViewModel
import com.example.buildup.databinding.ActivityWishlistBinding
import com.example.buildup.ui.Products.layouts.ProductActivity
import com.example.buildup.ui.Products.layouts.ProductCategoryActivity
import com.example.buildup.ui.Wishlist.adapters.WishlistAdapter


class  WishlistActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityWishlistBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var wishlistAdapter: WishlistAdapter
    private var propertyId:String=""
    private var inCart:Boolean=false
    private var hasNext=true
    private var pageNum=0
    private var productsList= mutableListOf<Product>()
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
//
//        _binding.bottomNavigationView.menu.getItem(2).isEnabled = false
//        _binding.bottomNavigationView.menu.getItem(2).isChecked = true


        _binding.backBtn.setOnClickListener {
            finish()
//            startActivity(Intent(this,WishlistActivity::class.java))
        }
        _binding.cartBtn.setOnClickListener {
            val intent=Intent(this,CartActivity::class.java)
            intent.putExtra("intentFromWishlist",true)
            startActivity(intent)
        }

        _binding.btnAddProducts.setOnClickListener {
            startActivity(Intent(this, ProductCategoryActivity::class.java))
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


        _binding.apply {
            Log.d("scroll","scroll")
            idNestedSV.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener {
                override fun onScrollChange(
                    v: NestedScrollView?,
                    scrollX: Int,
                    scrollY: Int,
                    oldScrollX: Int,
                    oldScrollY: Int
                ) {
                    if (scrollY == v!!.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                        // in this method we are incrementing page number,
                        // making progress bar visible and calling get data method.
                        pageNum++;
                        _binding.idPBLoading.visibility = View.VISIBLE;
                        getWishlistOnScrolled(pageNum,hasNext);
                    }
                }

            })
        }

//        getWishlistOnScrolled(pageNum,true)

    }

    private fun getWishlistOnScrolled(page:Int,hasNextBool:Boolean){
        Log.d("wishlist","getWishlistOnScrolled()")

        if (!hasNextBool) {
            // checking if the page number is greater than limit.
            // displaying toast message in this case when page>limit.
            Toast.makeText(this, "That's all the data..", Toast.LENGTH_SHORT).show();

            // hiding our progress bar.
            _binding.idPBLoading.visibility=View.GONE
            return;
        }
//        if(isFirstLoading){
//            productsList.clear()
//            Log.d("first",productsList.size.toString())
//        }
        authViewModel.getWishlist(page)
        authViewModel.respGetWishlist.observe({lifecycle}){
            if(it?.success!!){
                if(it.products!!.isEmpty())
                {
                    _binding.wishListRecyclerView.visibility= View.GONE
                    _binding.idPBLoading.visibility= View.GONE
                    _binding.emptyWishlistLayout.visibility=View.VISIBLE
                }
                else{
                    _binding.wishListRecyclerView.visibility= View.VISIBLE
                    _binding.idPBLoading.visibility= View.GONE
                    _binding.emptyWishlistLayout.visibility=View.GONE
                }
                hasNext=it.hasNext!!
                Toast.makeText(this,"wishlist items fetched successfully.", Toast.LENGTH_SHORT).show()

                Log.d("wishlistProductListbefore",productsList.size.toString())

                for(i in it.products!!){
                    if(!productsList.contains(i)){
                        productsList.add(i)
                    }
                }

                wishlistAdapter.submitList(productsList)
                wishlistAdapter.notifyDataSetChanged()
                Log.d("wishlistProductListafter",productsList.size.toString())

                if(it.products?.size!! == 1)
                    _binding.itemCount.text=productsList.size.toString() + " " + "item"
                else
                    _binding.itemCount.text=productsList.size.toString() + " " + "items"


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

    private fun removeProductFromWishlist(wishlistPositionData: WishlistPositionData?){
        authViewModel.removeProductFromWishlist(wishlistPositionData!!.productId)
        authViewModel.respRemoveProductFromWishlist.observe({lifecycle}){
            if(it?.success!!){
                Toast.makeText(this,"Product removed from wishlist",Toast.LENGTH_SHORT).show()
                var product: Product? =null
                productsList.forEach {
                    if(it.id==wishlistPositionData.productId)
                        product=it
                }
                productsList.remove(product)

                if(productsList.size == 1)
                    _binding.itemCount.text=productsList.size.toString() + " " + "item"
                else
                    _binding.itemCount.text=productsList.size.toString() + " " + "items"

                wishlistAdapter .submitList(productsList)
                wishlistAdapter.notifyDataSetChanged()
            }
            else
                Toast.makeText(this,it.error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun addProductToCartFromWishlist(wishlistData: WishlistData){
        if(wishlistData.inCart){
            var intent=Intent(this,CartActivity::class.java)
            intent.putExtra("intentFromWishlist",true)
            startActivity(intent)
        }
        else{
            authViewModel.addProductToCart(wishlistData.productId,true)
            authViewModel.respAddProductToCart.observe({lifecycle}){
                if(it?.success!!){
                    Toast.makeText(this,"RecentProduct added to cart successsfully.",Toast.LENGTH_SHORT).show()
                    var product: Product? =null
                    productsList.forEach {
                        if(it.id==wishlistData.productId)
                            product=it
                    }
                    productsList.remove(product)

                    if(productsList.size == 1)
                        _binding.itemCount.text=productsList.size.toString() + " " + "item"
                    else
                        _binding.itemCount.text=productsList.size.toString() + " " + "items"

                    wishlistAdapter .submitList(productsList)
                    wishlistAdapter.notifyDataSetChanged()
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
                productsList.clear()
                wishlistAdapter.submitList(productsList)
                wishlistAdapter.notifyDataSetChanged()
            }
            else
                Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        getWishlistOnScrolled(0,true)
    }
}