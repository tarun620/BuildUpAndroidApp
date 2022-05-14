package com.example.buildup.ui.Products.layouts

import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
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
import com.example.buildup.extensions.loadImage
import com.example.buildup.ui.BottomNavigation.CartActivity
import com.example.buildup.ui.Products.adapters.ProductCategoryAdapter
import com.example.buildup.ui.Products.adapters.ProductSubCategoryAdapter

class ProductCategoryActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityProductCategoryBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var productCategoryAdapter: ProductCategoryAdapter
    private lateinit var productSubCategoryAdapter: ProductSubCategoryAdapter

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

        drawLayout()
        _binding.btnRetry.setOnClickListener {
//            drawLayout()
            finish();
            startActivity(intent);
        }

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
                productCategoryAdapter.submitList(it.productCategories)
                productCategoryAdapter.notifyDataSetChanged()
                _binding.productCategoryText.text=it.productCategories?.get(0)?.name
                _binding.imageView6.loadImage(it.productCategories!![0].image!!)
                getProductSubCategories(it.productCategories!![0].id)
//                else{
//                    openProductCategory(ProductCategoryIdData(productCategoryId!!,productCategoryName!!))
//                }

            } else {
                if(it.error!="Network Failure")
                    Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
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
    private fun openProductCategory(productCategoryIdData: ProductCategoryIdData) {
//        Log.d("productCategoryId",productCategoryId.toString())

        _binding.productsCategoryRecyclerView.setBackgroundColor(getColor(R.color.grey_bg_poducts))
        _binding.productCategoryText.text=productCategoryIdData.productCategoryName
        _binding.imageView6.loadImage(productCategoryIdData.productCategoryImage)
        getProductSubCategories(productCategoryIdData.productCategoryId)

//        getProductSubCategories(productCategoryId!!)
    }

    private fun getProductSubCategories(productCategoryId: String) {
        authViewModel.getProductSubCategories(productCategoryId)
        authViewModel.respProductSubCategoryArray.observe({ lifecycle }) {
            if (it.success!!) {
//                Log.d("testLog",it.productSubCategories)

                productSubCategoryAdapter.submitList(it.productSubCategories)
            } else {
                if(it.error!="Network Failure")
                    Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onProductSubCategoryClicked(productSubCategoryIdData: ProductSubCategoryIdData) {
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

    private fun isNetworkAvailable(): Boolean {
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)

        return (capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))

    }
    private fun drawLayout() {
        if (isNetworkAvailable()) {
            Log.d("internet","internet")
//            _binding.mainLayout.visibility=View.VISIBLE
            _binding.noInternetLayout.visibility=View.GONE
        } else {
            Log.d("internet","no internet")
            _binding.mainLayout.visibility=View.GONE
            _binding.noInternetLayout.visibility=View.VISIBLE
            _binding.idPBLoading.visibility=View.GONE
        }
    }
}