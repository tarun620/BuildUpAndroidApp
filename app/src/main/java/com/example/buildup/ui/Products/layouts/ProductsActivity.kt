package com.example.buildup.ui.Products.layouts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.api.models.responsesAndData.products.productsResponses.Filters
import com.example.api.models.responsesAndData.products.productsResponses.GetProductsBySearchQueryData
import com.example.api.models.responsesAndData.wishlist.IsWishlistedData
import com.example.buildup.AuthViewModel
import com.example.buildup.R
import com.example.buildup.databinding.ActivityProductsBinding
import com.example.buildup.ui.BottomNavigation.CartActivity
import com.example.buildup.ui.Products.adapters.ProductAdapter

class ProductsActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityProductsBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var productAdapter: ProductAdapter
    private var productSubCategoryId:String?=null
    private var productSubCategoryName:String?=""
    private var searchQuery:String?=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_products)

        productSubCategoryId=intent.getStringExtra("productSubCategoryId")
        productSubCategoryName=intent.getStringExtra("productSubCategoryName")
        searchQuery=intent.getStringExtra("searchQuery")

        _binding=ActivityProductsBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)
        productAdapter= ProductAdapter({onProductClicked(it)},{onWishlistClicked(it)})

        _binding.productsRecyclerView.layoutManager= GridLayoutManager(this,2)
        _binding.productsRecyclerView.adapter=productAdapter



        setContentView(_binding.root)


        if (getSupportActionBar() != null) {
            getSupportActionBar()?.hide();
        }


        _binding.backBtn.setOnClickListener {
            finish()
        }
        _binding.searchBtn.setOnClickListener {
            val intent=Intent(this,SearchActivity::class.java)
            startActivity(intent)
        }
        _binding.cartBtn.setOnClickListener {
            val intent=Intent(this,CartActivity::class.java)
            startActivity(intent)
        }

        _binding.btnSort.setOnClickListener {
            val fragment = BottomSortByFragment()
            fragment.show(supportFragmentManager,fragment.tag)

        }

        if(searchQuery.isNullOrBlank() && !productSubCategoryId.isNullOrBlank())
            getProductsBySubCategoryId(productSubCategoryId,productSubCategoryName!!)
        else if(!searchQuery.isNullOrBlank())
            getProductsBySearchQuery(searchQuery!!)
    }

    fun onProductClicked(productId:String?){
        val intent=Intent(this, ProductActivity::class.java)
        intent.putExtra("productId",productId)
        startActivity(intent)
    }

    fun onWishlistClicked(isWishlistedData : IsWishlistedData){
        if(isWishlistedData.isWishlisted){
            authViewModel.removeProductFromWishlist(isWishlistedData.productId)
            authViewModel.respRemoveProductFromWishlist.observe({lifecycle}){
                if(it?.success!!)
                    Toast.makeText(this,"RecentProduct removed from wishlist",Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
            }
        }
        else{
            authViewModel.addProductToWishlist(isWishlistedData.productId)
            authViewModel.respAddProductToWishlist.observe({lifecycle}){
                if(it?.success!!)
                    Toast.makeText(this,"RecentProduct added to wishlist",Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun getProductsBySubCategoryId(productSubCategoryId:String?,productSubCategoryName:String){
        _binding.tvProductSubCategoryName.text=productSubCategoryName

        authViewModel.getProductsBySearchQuery(GetProductsBySearchQueryData(Filters(null,null,null,productSubCategoryId),null))
        authViewModel.respProducts.observe({lifecycle}){
            if(it.success!!){
                Toast.makeText(this,"products fetching successful",Toast.LENGTH_SHORT).show()
                productAdapter.submitList(it.products)
            }
            else{
                Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
            }
        }

    }
    private fun getProductsBySearchQuery(searchQueary:String){
        _binding.tvProductSubCategoryName.text=searchQueary

        authViewModel.getProductsBySearchQuery2(searchQuery!!,GetProductsBySearchQueryData(Filters(null,null,null,null),null))
        authViewModel.respProducts.observe({lifecycle}){
            if(it.success!!){
                Toast.makeText(this,"products fetching successful",Toast.LENGTH_SHORT).show()
                productAdapter.submitList(it.products)
            }
            else{
                Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
            }
        }

    }
}