package com.example.buildup.ui.Orders.layouts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.buildup.AuthViewModel
import com.example.buildup.R
import com.example.buildup.databinding.ActivityOrderBinding
import com.example.buildup.databinding.ActivityOrdersBinding
import com.example.buildup.extensions.newLoadImage

class OrderActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityOrderBinding
    private lateinit var authViewModel: AuthViewModel
    private var orderId :String?=null
    val map = mapOf(1 to "Ordered", 2 to "Shipped" , 3 to "Out For Delivery" , 4 to "Delivered")
//    val array=Array<String>(4)
    val array = arrayOf("Ordered", "Shipped", "Out For Delivery", "Delivered")
    var current_state=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_order)
        _binding = ActivityOrderBinding.inflate(layoutInflater)
        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        setContentView(_binding.root)


//        array.add("Ordered")
//        array.add("Shipped")
//        array.add("Out For Delivery")
//        array.add("Delivered")

        orderId=intent.getStringExtra("orderId")
//        Toast.makeText(this,"order Id : "+orderId,Toast.LENGTH_SHORT).show()

        getOrderById(orderId)
        stepViewHandling()

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
                        current_state=it.order!!.shipping.tracking.status.toInt()
                        tvBrandName.text=brandName
                        tvProductName.text=productName
                        tvClientName.text=it.order!!.shipping.customer
//                        tvShippingAddress.text=it.order!!.shipping.address
                        tvShippingAddress.text=it.order!!.shipping.address + ", "  + it.order!!.shipping.city + ", " + it.order!!.shipping.state + " - " + it.order!!.shipping.pincode
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
    private fun stepViewHandling(){
        _binding.stepView.apply {
            labels = array
            completedPosition=2
            drawView()
            progressColorIndicator=R.color.stepView_green
        }
//        _binding.apply {
//            stepView.barColorIndicator= R.color.selected_green_timeline
//            stepView.labelColorIndicator=R.color.selected_green_timeline
//            stepView.progressColorIndicator=R.color.stepView_green
////            stepView.color
//        }
    }

}