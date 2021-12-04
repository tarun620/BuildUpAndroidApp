package com.example.buildup.ui.Products.layouts

import android.app.AlertDialog
import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.api.models.responsesAndData.products.productsEntities.RecentySearchedQueryData
import com.example.buildup.AuthViewModel
import com.example.buildup.databinding.ActivitySearchBinding
import com.example.buildup.ui.MyApplication
import com.example.buildup.ui.Products.adapters.RecentSearchedAdapter
import com.example.buildup.ui.Products.adapters.RecentViewedProductsAdapter
import java.util.*


class SearchActivity : AppCompatActivity() {
    private lateinit var _binding: ActivitySearchBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var recentViewedProductsAdapter: RecentViewedProductsAdapter
    private lateinit var recentSearchedAdapter : RecentSearchedAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivitySearchBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)

        recentViewedProductsAdapter=RecentViewedProductsAdapter { onProductClicked(it) }
        recentSearchedAdapter=RecentSearchedAdapter{onItemClicked(it)}

        val intent=intent
        if(Intent.ACTION_SEARCH == intent.action){
            var query=intent.getStringExtra(SearchManager.QUERY)
            Toast.makeText(this,query,Toast.LENGTH_SHORT).show()
        }
        _binding.recentlyViewedRecyclerView.layoutManager=LinearLayoutManager(this,RecyclerView.HORIZONTAL,false)
        _binding.recentlyViewedRecyclerView.adapter=recentViewedProductsAdapter

        _binding.recentlySearchRecyclerView.layoutManager=LinearLayoutManager(this)
        _binding.recentlySearchRecyclerView.adapter=recentSearchedAdapter
//        setContentView(R.layout.activity_search)
        setContentView(_binding.root)


        _binding.apply {
            backBtn.setOnClickListener {
                finish()
            }

            btnSearch.setOnClickListener {
                if(etSearch.text.isNullOrBlank())
                    Toast.makeText(this@SearchActivity,"please enter keyword to search",Toast.LENGTH_SHORT).show()
                else{
                    (application as MyApplication).addElement(etSearch.text.toString())
//                    getRecentSearchedQuery()

                    val intent= Intent(this@SearchActivity,ProductsActivity::class.java)
                    intent.putExtra("searchQuery",etSearch.text.toString())
                    startActivity(intent)
                }
            }

            removeBtn.setOnClickListener {
                val builder = AlertDialog.Builder(this@SearchActivity)
                //set title for alert dialog
                builder.setTitle("Delete History")
                //set message for alert dialog
                builder.setMessage("Do you want to delete your Entire Search History?")
                builder.setIcon(android.R.drawable.ic_dialog_alert)

                //performing positive action
                builder.setPositiveButton("Yes"){dialogInterface, which ->
                    (application as MyApplication).clearQueue()
                    getRecentSearchedQuery()
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

        getAutoCompleteQueries()
        getRecentlyViewedProducts()
//        getRecentSearchedQuery()
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.search_menu,menu)
//        var searchManager=getSystemService(SEARCH_SERVICE) as SearchManager
//        var searchView:SearchView= menu?.findItem(R.id.search)?.actionView as SearchView
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
//        searchView.isSubmitButtonEnabled=true
//        return true
//
//    }

    private fun onProductClicked(productId:String?){
        val intent=Intent(this, ProductActivity::class.java)
        intent.putExtra("productId",productId)
        startActivity(intent)
    }
    private fun onItemClicked(searchQuery:String?){

    }
    private fun getRecentlyViewedProducts(){
        authViewModel.getRecentlyViewedProducts()
        authViewModel.respGetRecentlyViewedProducts.observe({lifecycle}){
            if(it?.success!!){
                recentViewedProductsAdapter.submitList(it.products)
            }
            else{
                Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun getRecentSearchedQuery(){
        var searchQueryList=mutableListOf<RecentySearchedQueryData>()
        (application as MyApplication).getQueue().let {
            for(i in it){
                searchQueryList.add(RecentySearchedQueryData(i))
            }
        }
        searchQueryList.reverse()
        recentSearchedAdapter.submitList(searchQueryList)
    }

    private fun getAutoCompleteQueries(){
        _binding.apply {
            etSearch.addTextChangedListener(object :TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if(!etSearch.text.toString().isNullOrBlank()){
                        authViewModel.getAutocompleteQueries(etSearch.text.toString())
                        authViewModel.respGetAutocompleteQueries.observe({lifecycle}){
                            if(it?.success!! && it.queries!=null){
                                val adapter = ArrayAdapter(this@SearchActivity,
                                    android.R.layout.simple_list_item_1, it.queries!!.toMutableList())
                                etSearch.threshold=0
                                etSearch.setAdapter(adapter)
                            }
                            else if(!it.success!!){
                                Toast.makeText(this@SearchActivity,it.error,Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                }

                override fun afterTextChanged(p0: Editable?) {
                }

            })
        }
    }

    override fun onResume() {
        super.onResume()
        Toast.makeText(this,"onResume",Toast.LENGTH_SHORT).show()
        _binding.recentlySearchRecyclerView.adapter=recentSearchedAdapter
        getRecentSearchedQuery()
    }
}