package com.example.buildup.ui.Products.layouts

import android.app.AlertDialog
import android.app.Dialog
import android.app.SearchManager
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.View.*
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.api.models.responsesAndData.products.productsEntities.RecentySearchedQueryData
import com.example.buildup.AuthViewModel
import com.example.buildup.MyApplication
import com.example.buildup.TinyDB
import com.example.buildup.databinding.ActivitySearchBinding
import com.example.buildup.ui.BottomNavigation.MyCustomDialogCart
import com.example.buildup.ui.Products.adapters.AutoCompleteQueryAdapter
import com.example.buildup.ui.Products.adapters.AutoCompleteQueryAdapterJava
import com.example.buildup.ui.Products.adapters.RecentSearchedAdapter
import com.example.buildup.ui.Products.adapters.RecentViewedProductsAdapter
import java.util.*
import kotlin.collections.ArrayList


class SearchActivity : AppCompatActivity(),MyCustomDialogSearch.OnInputListener{
    private lateinit var _binding: ActivitySearchBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var recentViewedProductsAdapter: RecentViewedProductsAdapter
    private lateinit var recentSearchedAdapter : RecentSearchedAdapter
    private lateinit var autoCompleteQueryAdapter : AutoCompleteQueryAdapter
    private lateinit var autoCompleteQueryAdapterJava: AutoCompleteQueryAdapterJava
    private var searchQueryList=ArrayList<String>()
    private lateinit var tinyDB :TinyDB
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivitySearchBinding.inflate(layoutInflater)

        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)

        tinyDB = TinyDB(this)
        recentViewedProductsAdapter=RecentViewedProductsAdapter { onProductClicked(it) }
        recentSearchedAdapter=RecentSearchedAdapter{onItemClicked(it)}

        val intent=intent
        if(Intent.ACTION_SEARCH == intent.action){
            var query=intent.getStringExtra(SearchManager.QUERY)
        }
        _binding.recentlyViewedRecyclerView.layoutManager=LinearLayoutManager(this,RecyclerView.HORIZONTAL,false)
        _binding.recentlyViewedRecyclerView.adapter=recentViewedProductsAdapter

        _binding.recentlySearchRecyclerView.layoutManager=LinearLayoutManager(this)
        _binding.recentlySearchRecyclerView.adapter=recentSearchedAdapter
//        setContentView(R.layout.activity_search)
        setContentView(_binding.root)

        drawLayout()
        _binding.btnRetry.setOnClickListener {
//            drawLayout()
            finish();
            startActivity(intent);
        }


        _binding.apply {
            textInputLayout.setEndIconOnClickListener {
                performSearch()
            }

            etSearch.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch()
                    return@OnEditorActionListener true
                }
                false
            })

            textInputLayout.setStartIconOnClickListener {
                finish()
            }
            removeBtn.setOnClickListener {
//                val builder = AlertDialog.Builder(this@SearchActivity)
//                //set title for alert dialog
//                builder.setTitle("Delete History")
//                //set message for alert dialog
//                builder.setMessage("Do you want to delete your Entire Search History?")
//                builder.setIcon(android.R.drawable.ic_dialog_alert)
//
//                //performing positive action
//                builder.setPositiveButton("Yes"){dialogInterface, which ->
////                    (application as MyApplication).clearQueue()
//                    searchQueryList.clear()
//                    tinyDB.putListString("searchQueryList",searchQueryList)
//                    getRecentSearchedQuery()
//                }
//                //performing cancel action
//                builder.setNeutralButton("Cancel"){dialogInterface , which ->
//
//                }
//                //performing negative action
//                builder.setNegativeButton("No"){dialogInterface, which ->
//                }
//                // Create the AlertDialog
//                val alertDialog: AlertDialog = builder.create()
//                // Set other dialog properties
//                alertDialog.setCancelable(false)
//                alertDialog.show()

                MyCustomDialogSearch().show(supportFragmentManager, "MyCustomFragment")

            }
        }

        getAutoCompleteQueries()
        getRecentlyViewedProducts()
//        getRecentSearchedQuery()
    }
    override fun sendInput(input: String?) {
        if(input=="yes"){
            Log.d("productId","i am here")
            deleteList()
        }
    }

    private fun deleteList(){
//        (application as MyApplication).clearQueue()
        searchQueryList.clear()
        tinyDB.putListString("searchQueryList",searchQueryList)
        getRecentSearchedQuery()
    }


    private fun performSearch() {
        _binding.apply {
            if(etSearch.text.isNullOrBlank())
                Toast.makeText(this@SearchActivity,"please enter keyword to search",Toast.LENGTH_SHORT).show()
            else{
//                    (application as MyApplication).addElement(etSearch.text.toString())
                if(searchQueryList.contains(etSearch.text.toString()))
                    searchQueryList.remove(etSearch.text.toString())

                if(searchQueryList.size>=5)
                    searchQueryList.removeAt(searchQueryList.size-1)

                searchQueryList.add(0,etSearch.text.toString())
                Log.d("array",searchQueryList.toString())
                tinyDB.putListString("searchQueryList",searchQueryList)

//                    getRecentSearchedQuery()

                val intent= Intent(this@SearchActivity,ProductsActivity::class.java)
                intent.putExtra("searchQuery",etSearch.text.toString())
                startActivity(intent)
            }
        }
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
        intent.putExtra("productClickedFromSearchIntent",true)
        startActivity(intent)
    }
    private fun onItemClicked(searchQuery:String?){
        val intent= Intent(this@SearchActivity,ProductsActivity::class.java)
        intent.putExtra("searchQueryRecentlySearched",searchQuery)
        intent.putExtra("intentFromRecentlySearched",true)
        startActivity(intent)
    }
    private fun getRecentlyViewedProducts(){
        authViewModel.getRecentlyViewedProducts()
        authViewModel.respGetRecentlyViewedProducts.observe({lifecycle}){
            if(it?.success!!){
                _binding.idPBLoading.visibility= GONE

                if(it.products!!.isEmpty()){
                    _binding.recentlyViewedLayout.visibility= GONE
                    _binding.recentlyViewedText.visibility= GONE
                }
                else{
                    _binding.recentlyViewedLayout.visibility= VISIBLE
                    _binding.recentlyViewedText.visibility= VISIBLE
                }

                recentViewedProductsAdapter.submitList(it.products)
            }
            else{
                if(it.error!="Network Failure")
                    Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun getRecentSearchedQuery(){ //called in OnResume()
        var searchQueryListMutable=mutableListOf<RecentySearchedQueryData>()
        tinyDB.getListString("searchQueryList").let {
            for(i in it){
                searchQueryListMutable.add(RecentySearchedQueryData(i))
            }
        }
    //        (application as MyApplication).getQueue().let {
//            for(i in it){
//                searchQueryList.add(RecentySearchedQueryData(i))
//            }
//        }
        if(searchQueryListMutable.isEmpty())
            _binding.recentlySearchedLayout.visibility=GONE
        else
            _binding.recentlySearchedLayout.visibility= VISIBLE

//        searchQueryListMutable.reverse()
        recentSearchedAdapter.submitList(searchQueryListMutable)
    }

    private fun getAutoCompleteQueries(){
        _binding.apply {
            etSearch.addTextChangedListener(object :TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {
                    if(etSearch.text.toString().isNotBlank()){
                        authViewModel.getAutocompleteQueries(etSearch.text.toString())
                        authViewModel.respGetAutocompleteQueries.observe({lifecycle}){ it ->
                            if(it?.success!! && it.queries!=null){
                                val suggestionList= ArrayList<String>()
                                for(i in it.queries!!){
                                    if(i!=null)
                                        suggestionList.add(i)
                                }
//                                Log.d("arraySize",suggestionList.size.toString())
//                                autoCompleteQueryAdapter=AutoCompleteQueryAdapter(this@SearchActivity,R.layout.item_recent_searched_products,R.id.title,suggestionList)
                                autoCompleteQueryAdapterJava= AutoCompleteQueryAdapterJava(this@SearchActivity,suggestionList)


//                                val adapter = ArrayAdapter(this@SearchActivity,
//                                    android.R.layout.simple_list_item_1,it.queries!!)

//                                var searchQueryListMutable=mutableListOf<RecentySearchedQueryData>()
//                               it.queries!!.let {
//                                    for(i in it){
//                                        searchQueryListMutable.add(RecentySearchedQueryData(i!!))
//                                    }
//                                }
//                                recentSearchedAdapter.submitList(searchQueryListMutable)
                                etSearch.threshold=0
//                                etSearch.adapter=CustomArrayAdapter(context, android.R.layout.simple_spinner_item, values = listOfValues)
                                etSearch.setAdapter(autoCompleteQueryAdapterJava)
                            }
                            else if(!it.success!!){
                                if(it.error!="Network Failure")
                                    Toast.makeText(this@SearchActivity,it.error,Toast.LENGTH_SHORT).show()                            }
                        }
                    }
                }

            })
        }
    }

//    private fun showDialog() {
//        dialog = Dialog(this)
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
//        dialog.setContentView(_bindingDialog.root)
//        dialog.setCancelable(false)
//
//        val lp = WindowManager.LayoutParams()
//        lp.copyFrom(dialog.window!!.attributes)
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT
//        lp.height = WindowManager.LayoutParams.MATCH_PARENT
//
//
//        if(_binding.etSearch.isFocused)
//            dialog.show()
//        else
//            dialog.hide()
////        Handler().postDelayed({
////            dialog.dismiss()
////            val intent=Intent(this, OrdersActivity::class.java)
////            startActivity(intent)
////        }, 4000)
//
//    }

    override fun onResume() {
        super.onResume()
        searchQueryList=tinyDB.getListString("searchQueryList")
        _binding.recentlySearchRecyclerView.adapter=recentSearchedAdapter
        getRecentSearchedQuery()
    }

    private fun isNetworkAvailable(): Boolean {
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)

        return (capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))

    }
    private fun drawLayout() {
        if (isNetworkAvailable()) {
            Log.d("internet","internet")
            _binding.mainLayout.visibility= View.VISIBLE
            _binding.noInternetLayout.visibility= View.GONE
        } else {
            Log.d("internet","no internet")
            _binding.mainLayout.visibility= View.GONE
            _binding.noInternetLayout.visibility= View.VISIBLE
        }
    }
}