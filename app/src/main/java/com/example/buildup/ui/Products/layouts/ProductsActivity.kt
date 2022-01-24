package com.example.buildup.ui.Products.layouts

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.api.models.responsesAndData.products.productsEntities.Products
import com.example.api.models.responsesAndData.products.productsResponses.Filters
import com.example.api.models.responsesAndData.products.productsResponses.GetProductsBySearchQueryData
import com.example.api.models.responsesAndData.products.productsResponses.PriceRange
import com.example.api.models.responsesAndData.wishlist.IsWishlistedData
import com.example.buildup.AuthViewModel
import com.example.buildup.MyApplication
import com.example.buildup.R
import com.example.buildup.databinding.ActivityProductsBinding
import com.example.buildup.ui.BottomNavigation.CartActivity
import com.example.buildup.ui.HomeActivity
import com.example.buildup.ui.Products.adapters.ProductAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textview.MaterialTextView

class ProductsActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityProductsBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var productAdapter: ProductAdapter
    private var productSubCategoryId:String?=null
    private var productSubCategoryName:String?=null
    private var productCategoryName:String?=null
    private var productCategoryId:String?=null
    private var searchQuery:String?=null
    private var singleBrandArray:ArrayList<String>?=null
    private var brandList:ArrayList<String>?=null
    private var brandName:String?=null
    private var fromRange:Int?=null
    private var toRange:Int?=null
    private var hasNext=true
    private var pageNum=0
    private var sort:String?=null
    private val productList= mutableListOf<Products>()
    private var fromProductActivity:Boolean=false




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_products)

        productSubCategoryId=intent.getStringExtra("productSubCategoryId")
        productSubCategoryName=intent.getStringExtra("productSubCategoryName")
        productCategoryName=intent.getStringExtra("productCategoryName")
        productCategoryId=intent.getStringExtra("productCategoryId")
        searchQuery=intent.getStringExtra("searchQuery")
        singleBrandArray=intent.getStringArrayListExtra("singleBrandArray")
        fromProductActivity=intent.getBooleanExtra("fromProductActivity",false)

        if((application as MyApplication).getFromRange()!=null)
            fromRange=(application as MyApplication).getFromRange()
        if((application as MyApplication).getToRange()!=null)
            toRange=(application as MyApplication).getToRange()
        (application as MyApplication).clearFromRange()
        (application as MyApplication).clearToRange()

        brandName=intent.getStringExtra("brandName")
        sort=intent.getStringExtra("sort")
        brandList=(application as MyApplication).getList()
        (application as MyApplication).clearList()
        Log.d("brandList",brandList.toString())

        _binding=ActivityProductsBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)
        productAdapter= ProductAdapter({onProductClicked(it)},{onWishlistClicked(it)})

        _binding.productsRecyclerView.layoutManager= GridLayoutManager(this,2)
        _binding.productsRecyclerView.adapter=productAdapter

        setContentView(_binding.root)

        _binding.backBtn.setOnClickListener {
            if(fromProductActivity)
                startActivity(Intent(this,HomeActivity::class.java))
            else
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


        _binding.btnFilter.setOnClickListener {
            startActivity(Intent(this,FilterActivity::class.java))
        }
//        if(searchQuery.isNullOrBlank() && !productSubCategoryId.isNullOrBlank())
//            getProductsBySubCategoryId(productSubCategoryId,productSubCategoryName!!)
//        else if(!searchQuery.isNullOrBlank())
//            getProductsBySearchQuery(searchQuery!!)

//        getProductsBySearchQuery(pageNum,hasNext,true)

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
                        getProductsBySearchQuery(pageNum,hasNext);
                    }
                }

            })
        }
    }



    private fun sortAsec() {
//        sort="price_asc"
//        getProductsBySearchQuery(0,true)
        val intent = intent
        intent.putExtra("sort","price_asc")
        finish()
        startActivity(intent)
    }

    private fun sortDesc() {
//        sort="price_desc"
//        getProductsBySearchQuery(0,true)
        val intent = intent
        intent.putExtra("sort","price_desc")
        finish()
        startActivity(intent)
    }

    private fun onProductClicked(productId:String?){
        val intent=Intent(this, ProductActivity::class.java)
        intent.putExtra("productId",productId)
        Log.d("productId",productId.toString())
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
    private fun getProductsBySearchQuery(page:Int,hasNextBool:Boolean){

        var nullCount=0
        setName()

        if (!hasNextBool) {
            // checking if the page number is greater than limit.
            // displaying toast message in this case when page>limit.
            Toast.makeText(this, "That's all the data..", Toast.LENGTH_SHORT).show();

            // hiding our progress bar.
            _binding.idPBLoading.visibility=View.GONE
            return;
        }
//        if(isNewListRequired)
//            productList.clear()

        if(!brandList.isNullOrEmpty()){
            Log.d("inside","inside if(!brandList.isNullOrEmpty())")
            authViewModel.getProductsBySearchQuery2(searchQuery,page,GetProductsBySearchQueryData(Filters(brandList, PriceRange(fromRange,toRange),productCategoryId,productSubCategoryId),sort))
        }
        else{
            Log.d("inside","inside else(!brandList.isNullOrEmpty())")
            authViewModel.getProductsBySearchQuery2(searchQuery,page,GetProductsBySearchQueryData(Filters(singleBrandArray,PriceRange(fromRange,toRange),productCategoryId,productSubCategoryId),sort))
        }


        authViewModel.respProducts.observe({lifecycle}){
            if(it.success!!){
                _binding.idPBLoading.visibility=View.GONE
                hasNext=it?.hasNext!!
                it.products!!.forEach {
                    if(it==null)
                        nullCount++
                }
                if(it.products?.size==0 || nullCount==it.products!!.size){
                    _binding.productsRecyclerView.visibility=View.GONE
                    _binding.noProductsLayout.visibility=View.VISIBLE
                }
                else {
                    _binding.productsRecyclerView.visibility=View.VISIBLE
                    _binding.noProductsLayout.visibility=View.GONE
                    Toast.makeText(this, "products fetching successful", Toast.LENGTH_SHORT).show()

//                    for(i in it.products!!){
//                        if(i!=null)
//                            producIdList.add(i.id)
//                    }
                    for(i in it.products!!){
                        if(i!=null && !productList.contains(i))
                            productList.add(i)
                    }
                    productAdapter.submitList(productList)
                    productAdapter.notifyDataSetChanged()
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

    private fun setName(){
        if(searchQuery.isNullOrBlank()){
            Log.d("inside","inside if(searchQuery.isNullOrBlank())")

            if(!productSubCategoryId.isNullOrBlank()){
                Log.d("inside","inside if(!productSubCategoryId.isNullOrBlank())")
                if(productSubCategoryName?.length!!>23)
                    _binding.tvProductSubCategoryName.text= productSubCategoryName!!.take(23) + ".."
                else
                    _binding.tvProductSubCategoryName.text=productSubCategoryName
            }
            else if(!productCategoryId.isNullOrBlank()){
                Log.d("inside","inside else if(!productCategoryId.isNullOrBlank())")

                if(productCategoryName?.length!!>23)
                    _binding.tvProductSubCategoryName.text= productCategoryName!!.take(23) + ".."
                else
                    _binding.tvProductSubCategoryName.text=productCategoryName
            }
            else if(!brandName.isNullOrEmpty())
                _binding.tvProductSubCategoryName.text=brandName


        }
        else if(!searchQuery.isNullOrBlank()){
            Log.d("inside","else if(!searchQuery.isNullOrBlank())")
            if(searchQuery?.length!!>23)
                _binding.tvProductSubCategoryName.text=searchQuery!!.take(23) + ".."
            else
                _binding.tvProductSubCategoryName.text=searchQuery
        }
    }
    override fun onResume() {
        super.onResume()
        brandList=(application as MyApplication).getList()
        if((application as MyApplication).getFromRange()!=-1)
            fromRange=(application as MyApplication).getFromRange()
        if((application as MyApplication).getToRange()!=-1)
            toRange=(application as MyApplication).getToRange()

        getProductsBySearchQuery(0,true)
        productAdapter= ProductAdapter({onProductClicked(it)},{onWishlistClicked(it)})

        _binding.productsRecyclerView.layoutManager= GridLayoutManager(this,2)
        _binding.productsRecyclerView.adapter=productAdapter

    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(fromProductActivity)
            startActivity(Intent(this,HomeActivity::class.java))
        else
            finish()
    }
}