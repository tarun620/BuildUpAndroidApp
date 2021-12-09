package com.example.buildup.ui.Products.layouts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.buildup.AuthViewModel
import com.example.buildup.databinding.ActivityCodPaymentBinding
import com.example.buildup.databinding.ActivityProductBinding
import com.example.buildup.ui.Orders.layouts.OrdersActivity

class CodPaymentActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityCodPaymentBinding
    private lateinit var authViewModel: AuthViewModel
    private var propertyId: String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityCodPaymentBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)
        setContentView(_binding.root)

        propertyId=intent.getStringExtra("propertyId")

        _binding.submitButton.setOnClickListener {
            placeOrder(propertyId)
        }

    }

    private fun placeOrder(propertyId:String?){
        if(propertyId==null){
            Toast.makeText(this,"property Id is null", Toast.LENGTH_SHORT).show()

        }
        else{
            authViewModel.createOrder(propertyId,"cod","null",true)
            authViewModel.respCreateOrder.observe({lifecycle}){
                if(it?.success!!){
                    Toast.makeText(this,"Order Placed Successfully", Toast.LENGTH_SHORT).show()
                    val intent= Intent(this, OrdersActivity::class.java)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this,it.error, Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}