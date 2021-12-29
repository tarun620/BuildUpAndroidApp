package com.example.buildup.ui.Orders.layouts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buildup.AuthViewModel
import com.example.buildup.databinding.ActivityOrdersBinding
import com.example.buildup.databinding.ActivityProductBinding
import com.example.buildup.ui.Orders.adapters.OrderAdapter
import com.example.buildup.ui.Products.adapters.ProductAdapter
import com.example.buildup.ui.Return.ReturnActivity

class OrdersActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityOrdersBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var orderAdapter: OrderAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityOrdersBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)
        orderAdapter= OrderAdapter({onOrderClicked(it)},{onReturnOrderClicked(it)})
        _binding.ordersRecyclerView.layoutManager=LinearLayoutManager(this)
        _binding.ordersRecyclerView.adapter=orderAdapter

        setContentView(_binding.root)


        getOrders()

    }

    private fun getOrders(){
        authViewModel.getAllOrders()
        authViewModel.respGetAllOrders.observe({lifecycle}){
            if(it?.success!!){
                Toast.makeText(this,"Orders Fetched Successfully.",Toast.LENGTH_SHORT).show()
                orderAdapter.submitList(it.orders)
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
}