package com.example.buildup.ui.Products.layouts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.api.models.responsesAndData.products.productsEntities.ProductCategoryIdData
import com.example.api.models.responsesAndData.products.productsEntities.ProductSubCategoryIdData
import com.example.buildup.AuthViewModel
import com.example.buildup.R
import com.example.buildup.databinding.ActivityProductCategoryBinding
import com.example.buildup.ui.BottomNavigation.CartActivity
import com.example.buildup.ui.Products.adapters.ProductCategoryAdapter
import com.example.buildup.ui.Products.adapters.ProductSubCategoryAdapter

class ProductCategoryActivity : AppCompatActivity() {
    //    private lateinit var _binding:ActivityProductCategoryBinding
    private lateinit var _binding: ActivityProductCategoryBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var productCategoryAdapter: ProductCategoryAdapter
    private lateinit var productSubCategoryAdapter: ProductSubCategoryAdapter
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
//    private var productCategoryId: String? = null
//    private var productCategoryName:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_product_category)

        _binding = ActivityProductCategoryBinding.inflate(layoutInflater)
        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        productCategoryAdapter = ProductCategoryAdapter { openProductCategory(it) }

        productSubCategoryAdapter = ProductSubCategoryAdapter { onProductSubCategoryClicked(it) }

        _binding.productsCategoryRecyclerView.layoutManager = LinearLayoutManager(this)
        _binding.productsCategoryRecyclerView.adapter = productCategoryAdapter

        _binding.productsSubCategoryRecyclerView.layoutManager = GridLayoutManager(this, 3)
        _binding.productsSubCategoryRecyclerView.adapter = productSubCategoryAdapter

        setContentView(_binding?.root)

//        productCategoryId=intent.getStringExtra("productCategoryId")
//        productCategoryName=intent.getStringExtra("productCategoryName")

        _binding.backBtn.setOnClickListener {
            finish()
        }
        _binding.searchBtn.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
        _binding.cartBtn.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
        getProductCategories()
//        val catId=getProductCategories()
//        getProductSubCategories(catId)

//        swipeRefreshLayout=findViewById(R.id.swipeRefreshLayout)
////
//        swipeRefreshLayout.setOnRefreshListener {
//            Toast.makeText(this,"refreshed.", Toast.LENGTH_SHORT).show()
//            getProductCategories()
//            swipeRefreshLayout.isRefreshing = false
//        }
    }

    fun getProductCategories() {
        authViewModel.getProductCategories(true,null)
        authViewModel.respProductCategoryArray.observe({ lifecycle }) {
            if (it?.success!!) {
                _binding.mainLayout.visibility= View.VISIBLE
                _binding.idPBLoading.visibility= View.GONE
                Toast.makeText(this, "product categories fetching successful", Toast.LENGTH_SHORT)
                    .show()
                productCategoryAdapter.submitList(it.productCategories)
                productCategoryAdapter.notifyDataSetChanged()
                _binding.productCategoryText.text=it.productCategories?.get(0)?.name
                getProductSubCategories(it.productCategories?.get(0)?.id!!)
//                else{
//                    openProductCategory(ProductCategoryIdData(productCategoryId!!,productCategoryName!!))
//                }

            } else {
                Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                Log.d("errorProductCategory", it.error.toString())
            }
        }
//        return propertyCategoryId!!
    }

    //    fun getProductSubCategories(productCategoryId:String){
//        authViewModel.getProductSubCategories(productCategoryId)
//        authViewModel.respProductSubCategoryArray.observe({lifecycle}){
//            if(it.success){
//                Toast.makeText(this,"product sub categories fetching successful", Toast.LENGTH_SHORT).show()
//                productSubCategoryAdapter.submitList(it.productSubCategories)
//                productSubCategoryAdapter.notifyDataSetChanged()
//            }
//            else
//                Toast.makeText(this,"product sub categories fetching failed.", Toast.LENGTH_SHORT).show()
//        }
//    }
    fun openProductCategory(productCategoryIdData: ProductCategoryIdData) {
//        Log.d("productCategoryId",productCategoryId.toString())

        _binding.productsCategoryRecyclerView.setBackgroundColor(getColor(R.color.grey_bg_poducts))
        _binding.productCategoryText.text=productCategoryIdData.productCategoryName
        getProductSubCategories(productCategoryIdData.productCategoryId)

//        getProductSubCategories(productCategoryId!!)
    }

    fun getProductSubCategories(productCategoryId: String) {
        authViewModel.getProductSubCategories(productCategoryId)
        authViewModel.respProductSubCategoryArray.observe({ lifecycle }) {
            if (it.success!!) {
//                Log.d("testLog",it.productSubCategories)
                Toast.makeText(
                    this,
                    "product sub categories fetching successful",
                    Toast.LENGTH_SHORT
                ).show()
                productSubCategoryAdapter.submitList(it.productSubCategories)
            } else {
                Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                Log.d("errorProductSubCategory", it.error.toString())
            }
        }
    }

    fun onProductSubCategoryClicked(productSubCategoryIdData: ProductSubCategoryIdData) {
        val intent = Intent(this, ProductsActivity::class.java)
        intent.putExtra("productSubCategoryId", productSubCategoryIdData.productSubCategoryId)
        intent.putExtra("productSubCategoryName", productSubCategoryIdData.productSubCategoryName)
        startActivity(intent)
//        Toast.makeText(this, "productSubCategoryId:${productSubCategoryId}", Toast.LENGTH_SHORT)
//            .show()
    }

//    fun onProductSubCategoryClicked(productSubCategoryId:String?){
//        val intent= Intent(this,ProductsActivity::class.java)
//        intent.putExtra("productSubCategoryId",productSubCategoryId)
//        startActivity(intent)
//    }
}