package com.example.buildup.ui.Products.layouts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
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
    private var shippingCost:Int?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityCodPaymentBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)
        setContentView(_binding.root)

        propertyId=intent.getStringExtra("propertyId")
        shippingCost=intent.getIntExtra("shippingCost",-1)

        getProductsFromCart()

        _binding.submitButton.setOnClickListener {
            placeOrder(propertyId)
        }
        _binding.backBtn.setOnClickListener {
            finish()
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
                    val intent= Intent(this, OrdersActivity::class.java)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this,it.error, Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun getProductsFromCart(){
        authViewModel.getProductsFromCart()

        authViewModel.respGetProductsFromCart.observe({lifecycle}){
            if(it?.success!!){
                _binding.paymentLayout.visibility= View.VISIBLE
                _binding.idPBLoading.visibility= View.GONE
                var totalMrp=0
                var discountedPrice=0
                var productQuantity=0

                for(i in it.items!!){
                    totalMrp+=i.product.mrp
                    discountedPrice+=i.product.amount
                    productQuantity+=i.quantity

                }
                _binding.apply {
                    tvOrderProductDetail.text= productQuantity.toString() + " x " + "Total Items"
                    tvTotalMrp.text="₹ " + totalMrp.toString()
                    tvDiscountedPrice.text="₹ " + discountedPrice.toString()
                    tvTotalDiscount.text="- ₹ " + (totalMrp-discountedPrice).toString()
                    tvDeliveryCharge.text="₹ " + shippingCost.toString()
                    tvTotalCartValue.text="₹ " + (discountedPrice+shippingCost!!).toString()
                }

            }
            else{
                Toast.makeText(this,it.error, Toast.LENGTH_SHORT).show()
            }
        }
    }
}