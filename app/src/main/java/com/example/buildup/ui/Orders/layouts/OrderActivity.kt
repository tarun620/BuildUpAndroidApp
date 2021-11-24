package com.example.buildup.ui.Orders.layouts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.buildup.AuthViewModel
import com.example.buildup.databinding.ActivityOrderBinding
import com.example.buildup.databinding.ActivityOrdersBinding
import com.example.buildup.extensions.newLoadImage

class OrderActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityOrderBinding
    private lateinit var authViewModel: AuthViewModel
    private var orderId :String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_order)
        _binding = ActivityOrderBinding.inflate(layoutInflater)
        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        setContentView(_binding.root)

        if (getSupportActionBar() != null) {
            getSupportActionBar()?.hide();
        }
        orderId=intent.getStringExtra("orderId")
//        Toast.makeText(this,"order Id : "+orderId,Toast.LENGTH_SHORT).show()

        getOrderById(orderId)

    }

    private fun getOrderById(orderId:String?){
        if(orderId.isNullOrBlank())
            Toast.makeText(this,"OrderId is Null",Toast.LENGTH_SHORT).show()
        else{
            authViewModel.getOrderById(orderId)
            authViewModel.respGetOrderById.observe({lifecycle}){
                if(it?.success!!){
                    _binding.apply {

                        ivProductImage.newLoadImage(it.order?.product?.id?.image?.get(0)!!)
                        val brandName=it.order!!.product.id.brand.name
                        val productName=it.order!!.product.id.name
                        val tvProductQuantity=it.order!!.product.quantity
                        val totalMrp=tvProductQuantity * it.order!!.product.unitMrp
                        val discountedPrice=tvProductQuantity * it.order!!.product.unitCost
                        tvBrandName.text=brandName
                        tvProductName.text=productName
                        tvClientName.text=it.order!!.shipping.customer
                        tvShippingAddress.text=it.order!!.shipping.address
                        tvOrderId.text="Order Id - " + it.order!!.id
                        tvOrderProductDetail.text= tvProductQuantity.toString() + " x " + brandName + " " + productName
                        tvTotalMrp.text="₹ " + totalMrp.toString()
                        tvDiscountedPrice.text="₹ " + discountedPrice.toString()
                        tvTotalDiscount.text="₹ " + (totalMrp-discountedPrice).toString()
                        // TODO : Shipping charges are hard coded yet
                        tvDeliveryCharge.text="₹ 100"
                        tvTotalCartValue.text="₹ " + (discountedPrice+100).toString()
                    }
                }
                else
                    Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
            }
        }
    }
}