package com.example.buildup.ui.Products.layouts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.api.models.responsesAndData.products.productsEntities.Products
import com.example.api.models.responsesAndData.products.productsResponses.Filters
import com.example.api.models.responsesAndData.products.productsResponses.GetProductsBySearchQueryData
import com.example.api.models.responsesAndData.wishlist.IsWishlistedData
import com.example.buildup.AuthViewModel
import com.example.buildup.R
import com.example.buildup.databinding.ActivityProductsBinding
import com.example.buildup.ui.BottomNavigation.CartActivity
import com.example.buildup.ui.Products.adapters.ProductAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView

class ProductsActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityProductsBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var productAdapter: ProductAdapter
    private var productSubCategoryId:String?=null
    private var productSubCategoryName:String?=null
    private var searchQuery:String?=null
    private var layoutManager:GridLayoutManager=GridLayoutManager(this,2)
    private var page=0
    private var isLoading=false
    private var hasNext=true

    private var sort:String?=null


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
//            val fragment = BottomSortByFragment()
//            fragment.show(supportFragmentManager,fragment.tag)
            val dialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.fragment_bottom_sort_by, null)
            val highToLowBtn = view.findViewById<MaterialTextView>(R.id.highToLowBtn)
            val lowToHighBtn = view.findViewById<MaterialTextView>(R.id.lowToHighBtn)

            highToLowBtn.setOnClickListener {
                sortDesc()
                dialog.dismiss()
            }
            lowToHighBtn.setOnClickListener {
                sortAsec()
                dialog.dismiss()
            }

            dialog.setContentView(view)
            dialog.show()
        }

//        if(searchQuery.isNullOrBlank() && !productSubCategoryId.isNullOrBlank())
//            getProductsBySubCategoryId(productSubCategoryId,productSubCategoryName!!)
//        else if(!searchQuery.isNullOrBlank())
//            getProductsBySearchQuery(searchQuery!!)

        getProductsBySearchQuery()
    }

    private fun sortAsec() {
        sort="price_asc"
        getProductsBySearchQuery()
    }

    private fun sortDesc() {
        sort="price_desc"
        getProductsBySearchQuery()
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
    private fun getProductsBySearchQuery(){
        if(searchQuery.isNullOrBlank() && !productSubCategoryId.isNullOrBlank()){
            if(productSubCategoryName?.length!!>23)
                _binding.tvProductSubCategoryName.text= productSubCategoryName!!.take(23) + ".."
            else
                _binding.tvProductSubCategoryName.text=productSubCategoryName

        }
        else if(!searchQuery.isNullOrBlank()){
            if(searchQuery?.length!!>23)
                _binding.tvProductSubCategoryName.text=searchQuery!!.take(23) + ".."
            else
                _binding.tvProductSubCategoryName.text=searchQuery
        }

        authViewModel.getProductsBySearchQuery2(searchQuery,GetProductsBySearchQueryData(Filters(null,null,null,productSubCategoryId),sort))
        authViewModel.respProducts.observe({lifecycle}){
            if(it.success!!){
                if(it.products?.size==0)
                    Toast.makeText(this,"List is empty",Toast.LENGTH_SHORT).show()
                else {
                    Toast.makeText(this, "products fetching successful", Toast.LENGTH_SHORT).show()
                    val productList= mutableListOf<Products>()
                    for(i in it.products!!){
                        if(i!=null)
                            productList.add(i)
                    }
                    productAdapter.submitList(productList)
                }
            }
            else{
                Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
            }
        }
    }
//    private fun paginationHandler(){
//        _binding.apply {
//            productsRecyclerView.addOnScrollListener(object :RecyclerView.OnScrollListener(){
//
//                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                    if(hasNext){
//                        page++
//                        getProductsBySubCategoryId(productSubCategoryId,productSubCategoryName!!)
//                    }
//
////                    if(dy>0){
////                        val visibleItemCount=layoutManager.childCount
////                        val pastVisibleItem=layoutManager.findFirstCompletelyVisibleItemPosition()
////                        val total=productAdapter.itemCount
////
//////                        if(!isLoading){
////                            if((visibleItemCount+pastVisibleItem)>=total && hasNext){
////
////                            }
////                        }
////                    }
//                    super.onScrolled(recyclerView, dx, dy)
//                }
//            })
//        }
//    }
}