package com.example.buildup.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.buildup.AuthViewModel
import com.example.buildup.R
import com.example.buildup.databinding.ActivityProductCategoryBinding
import com.example.buildup.databinding.ActivityProductSubCategoryBinding

class ProductSubCategoryActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityProductSubCategoryBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var productSubCategoryAdapter: ProductSubCategoryAdapter
    private var productCategoryId:String?=null
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_product_sub_category)

        productCategoryId=intent.getStringExtra("productCategoryId")

        _binding= ActivityProductSubCategoryBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)
        productSubCategoryAdapter= ProductSubCategoryAdapter{onProductSubCategoryClicked(it)}

        _binding.productsSubCategoryRecyclerView.layoutManager= LinearLayoutManager(this)
        _binding.productsSubCategoryRecyclerView.adapter=productSubCategoryAdapter

        setContentView(_binding?.root)

        getProductSubCategories(productCategoryId!!)

        swipeRefreshLayout=findViewById(R.id.swipeRefreshLayout)

        swipeRefreshLayout.setOnRefreshListener {
            Toast.makeText(this,"refreshed.", Toast.LENGTH_SHORT).show()
            getProductSubCategories(productCategoryId!!)
            swipeRefreshLayout.isRefreshing = false
        }

    }
    fun getProductSubCategories(productCategoryId:String){
        authViewModel.getProductSubCategories(productCategoryId)
        authViewModel.respProductSubCategoryArray.observe({lifecycle}){
            if(it.success){
                Toast.makeText(this,"product sub categories fetching successful", Toast.LENGTH_SHORT).show()
                productSubCategoryAdapter.submitList(it.subCategories)
            }
            else
                Toast.makeText(this,"product sub categories fetching failed.", Toast.LENGTH_SHORT).show()
        }
    }
    fun onProductSubCategoryClicked(productSubCategoryId:String?){
        val intent= Intent(this,ProductsActivity::class.java)
        intent.putExtra("productSubCategoryId",productSubCategoryId)
        startActivity(intent)
        Toast.makeText(this,"productSubCategoryId:${productSubCategoryId}",Toast.LENGTH_SHORT).show()
    }
}