package com.example.buildup.ui.Orders.layouts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.api.models.responsesAndData.order.Order
import com.example.api.models.responsesAndData.products.productsEntities.Products
import com.example.buildup.AuthViewModel
import com.example.buildup.databinding.ActivityOrdersBinding
import com.example.buildup.ui.HomeActivity
import com.example.buildup.ui.LoginSignup.LoginSignupSelectorActivity
import com.example.buildup.ui.Orders.adapters.OrderAdapter
import com.example.buildup.ui.Products.layouts.ProductCategoryActivity
import com.example.buildup.ui.ReturnOrder.ReturnActivity

class OrdersActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityOrdersBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var orderAdapter: OrderAdapter
    private var hasNext=true
    private var pageNum=0
    private val ordersList= mutableListOf<Order>()
    private var fromProfileActivity:Boolean=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityOrdersBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)
        orderAdapter= OrderAdapter({onOrderClicked(it)},{onReturnOrderClicked(it)})
        _binding.ordersRecyclerView.layoutManager=LinearLayoutManager(this)
        _binding.ordersRecyclerView.adapter=orderAdapter

        setContentView(_binding.root)


        fromProfileActivity=intent.getBooleanExtra("fromProfileActivity",false)
//        getOrders(0,true)

        _binding.backBtn.setOnClickListener {
            if(fromProfileActivity)
                finish()
            else
                startActivity(Intent(this, HomeActivity::class.java))
        }

        _binding.btnAddProducts.setOnClickListener {
            startActivity(Intent(this, ProductCategoryActivity::class.java))
        }

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
                        getOrders(pageNum,hasNext);
                    }
                }

            })
        }

    }

    private fun getOrders(page:Int,hasNextBool:Boolean){
        if (!hasNextBool) {
            // checking if the page number is greater than limit.
            // displaying toast message in this case when page>limit.

            // hiding our progress bar.
            _binding.idPBLoading.visibility=View.GONE
            return;
        }
        authViewModel.getAllOrders(page)
//        authViewModel.respGetAllOrders.removeObservers { lifecycle }
        authViewModel.respGetAllOrders.observe({lifecycle}){
            if(it?.success!!){
                if(it.orders!!.isEmpty()){
                    _binding.ordersRecyclerView.visibility= View.GONE
                    _binding.idPBLoading.visibility= View.GONE
                    _binding.noOrdersLayout.visibility=View.VISIBLE
                }
                else{
                    _binding.ordersRecyclerView.visibility= View.VISIBLE
                    _binding.idPBLoading.visibility= View.GONE
                    _binding.noOrdersLayout.visibility=View.GONE
                }
                hasNext=it.hasNext!!

                for(i in it.orders!!){
                    if(!ordersList.contains(i))
                        ordersList.add(i)
                }
//                if(isFirstLoading) {
//                    orderAdapter.submitList(it.orders)
//                }
//                else
                orderAdapter.submitList(ordersList)
                orderAdapter.notifyDataSetChanged()
            }
            else{
                Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onOrderClicked(orderId:String?){
        val intent=Intent(this,OrderActivity::class.java)
        intent.putExtra("orderId",orderId)
        startActivity(intent)
    }
    private fun onReturnOrderClicked(orderId: String?){
        val intent=Intent(this,ReturnActivity::class.java)
        intent.putExtra("orderId",orderId)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        getOrders(0,true)
    }
    override fun onBackPressed() {
        super.onBackPressed()
        if(fromProfileActivity)
            finish()
        else
            startActivity(Intent(this, HomeActivity::class.java))
    }
}