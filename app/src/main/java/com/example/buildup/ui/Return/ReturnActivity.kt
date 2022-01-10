package com.example.buildup.ui.Return

import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buildup.AuthViewModel
import com.example.buildup.R
import com.example.buildup.databinding.ActivityReturnBinding
import com.example.buildup.extensions.getReturnValidyDate
import com.example.buildup.extensions.loadBrandImage
import com.example.buildup.extensions.newLoadImage
import com.example.buildup.ui.Orders.adapters.OrderAdapter

class ReturnActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityReturnBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var orderReturnReasonAdapter: OrderReturnReasonAdapter
    private var orderId:String?=null
    private var orderReturnReason:String?=null
    private var orderReturnAdditionalReason:String?=null
    private var itemQuantity:Int?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_return)
        _binding= ActivityReturnBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)

        orderReturnReasonAdapter= OrderReturnReasonAdapter { onReasonClicked(it) }
        _binding.orderReturnReasonsRecyclerview.layoutManager= LinearLayoutManager(this)
        _binding.orderReturnReasonsRecyclerview.adapter=orderReturnReasonAdapter

        setContentView(_binding.root)

        orderId=intent.getStringExtra("orderId")

        getOrderDetails()

        _binding.apply {
//            btnContinue.isEnabled = !(orderReturnReason.isNullOrEmpty() || itemQuantity==null || !checkbox.isChecked)
            checkbox.setOnClickListener{
                btnContinue.isEnabled = checkbox.isChecked
            }

            btnContinue.setOnClickListener {
                if(!orderReturnReason.isNullOrEmpty()){
                    if(itemQuantity!=null){
                        if(checkbox.isEnabled){
                            val bundle = Bundle()
                            bundle.putString("orderId",orderId)
                            bundle.putString("orderReturnReason",orderReturnReason)
                            bundle.putString("orderReturnAdditionalReason",orderReturnAdditionalReason)
                            bundle.putInt("itemQuantity",itemQuantity!!)
                            val intent=Intent(this@ReturnActivity,BankDetailsActivity::class.java)
                            intent.putExtras(bundle)
                            startActivity(intent)
                        }
                    }
                    else{
                        Toast.makeText(this@ReturnActivity,"Please Select Quantity of the Product..",Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    Toast.makeText(this@ReturnActivity,"Please Select a Reason to Return the Products..",Toast.LENGTH_SHORT).show()
                }
            }
        }


    }

    private fun getOrderDetails(){
        if(!orderId.isNullOrEmpty()){
            authViewModel.getOrderReturnDetails(orderId!!)
            authViewModel.respGetOrderReturnDetails.observe({lifecycle}){
                if(it?.success!!){
                    _binding.apply {

                        var quantityArray= ArrayList<Int>()
                        ivProductImage.newLoadImage(it.order!!.product.id.image[0])
                        tvBrandName.text=it.order!!.product.id.brand.name
                        tvProductName.text=it.order!!.product.id.name
                        tvProductPrice.text="₹ " + it.order!!.product.unitCost.toString()
                        tvProductMrp.text="₹ " + it.order!!.product.unitMrp.toString()
                        tvProductMrp.paintFlags = tvProductMrp.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                        tvReturnValidity.getReturnValidyDate=it.order!!.shipping.tracking.status[0].time
                        orderReturnReasonAdapter.submitList(it.reasons)
                        orderReturnAdditionalReason=reasonEditText.text.toString()

                        for(i in 1..it.order!!.product.quantity)
                            quantityArray.add(i)

                        var quantityAdapter = ArrayAdapter(this@ReturnActivity, android.R.layout.simple_list_item_1, quantityArray)
                        quantityEditText.setAdapter(quantityAdapter)

                        quantityEditText.addTextChangedListener(object :TextWatcher{
                            override fun beforeTextChanged(
                                p0: CharSequence?,
                                p1: Int,
                                p2: Int,
                                p3: Int
                            ) {

                            }

                            override fun onTextChanged(
                                p0: CharSequence?,
                                p1: Int,
                                p2: Int,
                                p3: Int
                            ) {

                            }

                            override fun afterTextChanged(p0: Editable?) {
                                if(quantityEditText.text.toString()!="Select Quantity"){
                                    itemQuantity=quantityEditText.text.toString().toInt()
                                }
                            }

                        })

                        tvAddress.text=it.order!!.shipping.address + ", "  + it.order!!.shipping.city + ", " + it.order!!.shipping.state + " - " + it.order!!.shipping.pincode

                    }
                }
                else
                    Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onReasonClicked(reasonItem:String?) {
        orderReturnReason=reasonItem
    }

}