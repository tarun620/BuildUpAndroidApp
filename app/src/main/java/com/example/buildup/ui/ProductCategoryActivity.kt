package com.example.buildup.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.buildup.AuthViewModel
import com.example.buildup.R
import com.example.buildup.databinding.ActivityAddPropertyBinding
import com.example.buildup.databinding.ActivityProductCategoryBinding
import com.example.buildup.databinding.NestedRecyclerViewBinding

class ProductCategoryActivity : AppCompatActivity() {
//    private lateinit var _binding:ActivityProductCategoryBinding
    private lateinit var _binding:ActivityProductCategoryBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var productCategoryAdapter: ProductCategoryAdapter
//    private lateinit var productSubCategoryAdapter: ProductSubCategoryAdapter
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var propertyCategoryId:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_product_category)

        _binding= ActivityProductCategoryBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)
        productCategoryAdapter= ProductCategoryAdapter{openProductCategory(it)}
//        productSubCategoryAdapter= ProductSubCategoryAdapter{onProductSubCategoryClicked(it)}

        _binding.productsCategoryRecyclerView.layoutManager=LinearLayoutManager(this)
        _binding.productsCategoryRecyclerView.adapter=productCategoryAdapter

//        _binding.productsSubCategoryRecyclerView.layoutManager= GridLayoutManager(this,3)
//        _binding.productsSubCategoryRecyclerView.adapter=productSubCategoryAdapter

        setContentView(_binding?.root)

        getProductCategories()
//        val catId=getProductCategories()
//        getProductSubCategories(catId)

        swipeRefreshLayout=findViewById(R.id.swipeRefreshLayout)
//
        swipeRefreshLayout.setOnRefreshListener {
            Toast.makeText(this,"refreshed.", Toast.LENGTH_SHORT).show()
            getProductCategories()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    fun getProductCategories(){
        authViewModel.getProductCategories()
        authViewModel.respProductCategoryArray.observe({lifecycle}){
            if(it?.success!!){
                Toast.makeText(this,"product categories fetching successful", Toast.LENGTH_SHORT).show()
                productCategoryAdapter.submitList(it.productCategories)
                productCategoryAdapter.notifyDataSetChanged()
//                propertyCategoryId=it.productCategories[0].id
                Log.d("propertyid",propertyCategoryId.toString())
            }
            else{
                Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
                Log.d("errorProductCategory",it.error.toString())
            }
        }
//        return propertyCategoryId!!
    }

//    fun getProductSubCategories(productCategoryId:String){
//        authViewModel.getProductSubCategories(productCategoryId)
//        authViewModel.respProductSubCategoryArray.observe({lifecycle}){
//            if(it.success){
//                Toast.makeText(this,"product sub categories fetching successful", Toast.LENGTH_SHORT).show()
//                productSubCategoryAdapter.submitList(it.subCategories)
//                productSubCategoryAdapter.notifyDataSetChanged()
//            }
//            else
//                Toast.makeText(this,"product sub categories fetching failed.", Toast.LENGTH_SHORT).show()
//        }
//    }
    fun openProductCategory(productCategoryId:String?){
        val intent= Intent(this,ProductSubCategoryActivity::class.java)
        intent.putExtra("productCategoryId",productCategoryId)
        startActivity(intent)

//        getProductSubCategories(productCategoryId!!)
    }

//    fun onProductSubCategoryClicked(productSubCategoryId:String?){
//        val intent= Intent(this,ProductsActivity::class.java)
//        intent.putExtra("productSubCategoryId",productSubCategoryId)
//        startActivity(intent)
//    }
}