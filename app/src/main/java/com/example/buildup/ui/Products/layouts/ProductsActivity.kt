package com.example.buildup.ui.Products.layouts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.buildup.AuthViewModel
import com.example.buildup.R
import com.example.buildup.databinding.ActivityProductsBinding
import com.example.buildup.ui.Products.adapters.ProductAdapter

class ProductsActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityProductsBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var productAdapter: ProductAdapter
    private var productSubCategoryId:String?=null
    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_products)

        productSubCategoryId=intent.getStringExtra("productSubCategoryId")

        Toast.makeText(this,"this is products activity:${productSubCategoryId}",Toast.LENGTH_SHORT).show()
        _binding=ActivityProductsBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)
        productAdapter= ProductAdapter{onProductClicked(it)}

        _binding.productsRecyclerView.layoutManager= GridLayoutManager(this,2)
        _binding.productsRecyclerView.adapter=productAdapter


        setContentView(_binding?.root)


        if (getSupportActionBar() != null) {
            getSupportActionBar()?.hide();
        }

        getProducts(productSubCategoryId)

        swipeRefreshLayout=findViewById(R.id.swipeRefreshLayout)

        swipeRefreshLayout.setOnRefreshListener {
            Toast.makeText(this,"refreshed.", Toast.LENGTH_SHORT).show()
            getProducts(productSubCategoryId)
            swipeRefreshLayout.isRefreshing = false
        }

    }

    fun getProducts(productSubCategoryId:String?){
        authViewModel.getProducts(productSubCategoryId!!)

        authViewModel.respProducts.observe({lifecycle}){
            if(it.success!!){
                Log.d("product",it.products?.size.toString())
//                Log.d("product",it.products[0].name)
//                Log.d("product",it.products[1].name)
//                Log.d("product",it.products[2].name)

                Toast.makeText(this,"products fetching successful",Toast.LENGTH_SHORT).show()
                productAdapter.submitList(it.products)
            }
            else{
                Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
                Log.d("errorProducts",it.error.toString())
            }
        }
    }

    fun onProductClicked(productId:String?){
        val intent=Intent(this, ProductActivity::class.java)
        intent.putExtra("productId",productId)
        startActivity(intent)
    }
}