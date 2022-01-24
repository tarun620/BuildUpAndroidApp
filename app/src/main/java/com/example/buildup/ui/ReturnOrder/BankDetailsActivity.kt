package com.example.buildup.ui.ReturnOrder

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.InputFilter
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.api.models.responsesAndData.returnOrder.BankDetails
import com.example.api.models.responsesAndData.returnOrder.PlaceOrderReturnRequestData
import com.example.buildup.AuthViewModel
import com.example.buildup.databinding.ActivityBankDetailsBinding
import com.example.buildup.databinding.ActivitySuccessMessageBinding
import com.example.buildup.ui.Orders.layouts.OrdersActivity

class BankDetailsActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityBankDetailsBinding
    private lateinit var _bindingDialog : ActivitySuccessMessageBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var dialog: Dialog
    private var orderId:String?=null
    private var orderReturnReason:String?=null
    private var orderReturnAdditionalReason:String?=null
    private var itemQuantity:Int?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_bank_details)
        _binding= ActivityBankDetailsBinding.inflate(layoutInflater)
        _bindingDialog= ActivitySuccessMessageBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)
        setContentView(_binding.root)

        val bundle = intent.extras

        orderId=bundle!!.getString("orderId")
        orderReturnReason=bundle.getString("orderReturnReason")
        orderReturnAdditionalReason=bundle.getString("orderReturnAdditionalReason")
        itemQuantity=bundle.getInt("itemQuantity")

        _binding.backBtn.setOnClickListener {
            finish()
        }
//        <your_edit_text>.filters += InputFilter.AllCaps()
        _binding.apply {
            ifscEditText.filters+=InputFilter.AllCaps()
            btnContinue.setOnClickListener {
                if(bankDetailsValidator()){
                    authViewModel.placeOrderReturnRequest(
                        orderId!!,
                        PlaceOrderReturnRequestData(
                            orderReturnAdditionalReason,
                            BankDetails(accountNumber = accountEditText.text.toString().toLong(),
                                        fullName = nameEditText.text.toString(),
                                        ifscCode = ifscEditText.text.toString()),
                            itemQuantity!!,
                            orderReturnReason!!
                        )
                    )

                    authViewModel.respPlaceOrderReturnRequest.observe({lifecycle}){
                        if(it?.success!!){
                            showDialog()
                        }
                        else{
                            Toast.makeText(this@BankDetailsActivity,it.error,Toast.LENGTH_SHORT).show()
                        }
                    }
                }
//            startActivity(Intent(this,SuccessMessageActivity::class.java))
            }
        }


    }

    private fun bankDetailsValidator():Boolean{
        val pattern = Regex("^[A-Z]{4}0[A-Z0-9]{6}$")
        _binding.apply {
            nameTextInputLayout.error=null
            accountTextInputLayout.error=null
            ifscTextInputLayout.error=null

            nameTextInputLayout.isErrorEnabled= false
            accountTextInputLayout.isErrorEnabled=false
            ifscTextInputLayout.isErrorEnabled=false

            if(nameEditText.text.isNullOrEmpty() || nameEditText.text.isNullOrBlank()){
                nameTextInputLayout.error="Please Fill Full Name"
                nameTextInputLayout.requestFocus()
                return false
            } else if(accountEditText.text.isNullOrBlank() || accountEditText.text.isNullOrEmpty() || accountEditText.text.toString().length<8){
                accountTextInputLayout.error="Please Fill Correct Account Number"
                accountTextInputLayout.requestFocus()
                return false
            } else if(ifscEditText.text.isNullOrBlank() || ifscEditText.text.isNullOrEmpty() || !pattern.matches(ifscEditText.text.toString())){
                ifscTextInputLayout.error="Please Fill Correct IFSC Code"
                ifscTextInputLayout.requestFocus()
                return false
            }
            else
                return true
        }
    }

    private fun showDialog() {
        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
        dialog.setContentView(_bindingDialog.root)
        dialog.setCancelable(false)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.show()


        Handler().postDelayed({
            dialog.dismiss()
            val intent=Intent(this, OrdersActivity::class.java)
            startActivity(intent)
        }, 4000)

    }
}