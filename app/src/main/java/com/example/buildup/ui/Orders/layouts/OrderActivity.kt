package com.example.buildup.ui.Orders.layouts

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.example.buildup.AuthViewModel
import com.example.buildup.R
import com.example.buildup.databinding.ActivityOrderBinding
import com.example.buildup.extensions.newLoadImage
import com.example.buildup.ui.Orders.adapters.isoDateFormat
import com.example.buildup.ui.Return.ReturnActivity
import java.util.*
import kotlin.collections.ArrayList

class OrderActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityOrderBinding
    private lateinit var authViewModel: AuthViewModel
    private var orderId :String?=null
//    val map = mapOf(1 to "ordered", 2 to "shipped" , 3 to "outForDelivery" , 4 to "delivered" , 5 to "cancelled" , 6 to "returned", 7 to "outForPickup" , 8 to "pickedUp" , 9 to "refund")

//    val array = listOf<String>("Ordered", "Shipped", "Out For Delivery", "Delivered", "Cancelled", "Returned", "Out For Pickup", "Pickup Successful", "Refund")
    private val array = listOf<String>("Ordered", "Shipped", "Out For Delivery", "Delivered")
    private val returnArray=listOf<String>("Returned", "Out For Pickup", "Pickup Done", "Refund")
    private val cancelledArray1= listOf<String>("Ordered","Cancelled")
    private val cancelledArray2=listOf<String>("Ordered","Shipped","Cancelled")
    private val cancelledArray3= listOf<String>("Returned","Cancelled")
    var apiStatusArraySize:Int=-1
    val statusList=ArrayList<String>()
    private var rating:Int=0
    private var userRating:Int=0
    private var productId:String?=null
    private var isSuccess=false
    private var isDelivered=false

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

        _binding.apply {
            star1.setOnClickListener {
                rating=1
                if(addProductRating()){
                    Star1()
                    Log.d("rating","here")
                }

            }

            star2.setOnClickListener {
                rating=2
                if(addProductRating())
                    Star2()

            }

            star3.setOnClickListener {
                rating=3
                if(addProductRating())
                    Star3()

            }

            star4.setOnClickListener {
                rating=4
                if(addProductRating())
                    Star4()

            }

            star5.setOnClickListener {
                rating=5
                if(addProductRating())
                    Star5()
            }
            cancelBtn.setOnClickListener {
                if(isDelivered){
                    val intent=Intent(this@OrderActivity,ReturnActivity::class.java)
                    intent.putExtra("orderId",orderId)
                    startActivity(intent)
                }
                else{
                    //TODO : implement cancel order -- update timeline if ordered is cancelled
                }
            }

        }

        getOrderById(orderId)
    }

    private fun getOrderById(orderId:String?){
        if(orderId.isNullOrBlank())
            Toast.makeText(this,"OrderId is Null",Toast.LENGTH_SHORT).show()
        else{
            authViewModel.getOrderById(orderId)
            authViewModel.respGetOrderById.observe({lifecycle}){ it ->
                if(it?.success!!){
                    _binding.apply {
                        var finalStepList=listOf<String>()
                        val apiStatusArray=it.order!!.shipping.tracking.status
                        val currentStatus:String=apiStatusArray.last().name

                        if(currentStatus=="cancelled"){
                            if(apiStatusArray.size==3) { // ordered..shipped..cancelled
                                finalStepList=cancelledArray2

                            }
                            else if(apiStatusArray.size==2){
                                if(apiStatusArray[0].name=="ordered") // ordered..cancelled
                                    finalStepList=cancelledArray1
                                else if(apiStatusArray[0].name=="returned") //returned..cancelled
                                    finalStepList=cancelledArray3
                            }
                        }
                        else if(apiStatusArray[0].name=="ordered")
                            finalStepList=array
                        else if(apiStatusArray[0].name=="returned")
                            finalStepList=returnArray

                        if(currentStatus=="delivered"){
                            cancelBtn.text="RETURN"
                            if(!isReturnWindowLeft(apiStatusArray.last().time))
                                cancelBtn.visibility=View.GONE
                            //TODO : change button icon
                        }


                        if(currentStatus=="cancelled")
                            cancelBtn.visibility=View.GONE

                        if(currentStatus=="ordered" || currentStatus=="shipped" || currentStatus=="returned"){
//                            it.order!!.shipping.tracking.status.forEach {
//                                statusList.add(it.name)
//                            }
                            _binding.apply {
                                cancelBtn.visibility=View.VISIBLE
                                cancelBtn.setOnClickListener {
                                    cancelOrder()
                                }
                            }
                        }
                        if(it.order!!.isReturnAvailed!=null && it.order!!.isReturnAvailed!!)
                            cancelBtn.visibility= View.GONE

                        apiStatusArraySize=apiStatusArray.size
                        productId=it.order!!.product.id.id
                        ivProductImage.newLoadImage(it.order?.product?.id?.image?.get(0)!!)
                        val brandName=it.order!!.product.id.brand.name
                        val productName=it.order!!.product.id.name
                        val tvProductQuantity=it.order!!.product.quantity
                        val totalMrp=tvProductQuantity * it.order!!.product.unitMrp
                        val discountedPrice=tvProductQuantity * it.order!!.product.unitCost
//                        current_state=it.order!!.shipping.tracking.status.toInt()
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

                        getUserProductRating()
                        drawStepView(it.order!!.isReturn,finalStepList)

                    }
                }
                else
                    Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
            }
        }
    }
//    private fun stepViewHandling(){
//        _binding.stepView.apply {
//            labels = array
//            completedPosition=2
//            drawView()
//            progressColorIndicator=R.color.stepView_green
//        }
////        _binding.apply {
////            stepView.barColorIndicator= R.color.selected_green_timeline
////            stepView.labelColorIndicator=R.color.selected_green_timeline
////            stepView.progressColorIndicator=R.color.stepView_green
//////            stepView.color
////        }
//    }

    private fun addProductRating():Boolean{
        if(!productId.isNullOrEmpty()){
            authViewModel.addProductRating(productId!!, rating)
            authViewModel.respAddProductRating.observe({lifecycle}){
                if(it?.success!!){
                    isSuccess=true
                    Log.d("isSuccess1",isSuccess.toString())
                    Toast.makeText(this,"Product Rated Successfully.",Toast.LENGTH_SHORT).show()
                }
                else{
                    isSuccess=false
                    Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
                }
            }
        }
        Log.d("isSuccess2",isSuccess.toString())
        return isSuccess
    }

    private fun getUserProductRating(){
        if(!productId.isNullOrEmpty()){
            authViewModel.getUserProductRating(productId!!)
            authViewModel.respGetUserProductrating.observe({lifecycle}){
                if(it?.success!!){
                    Toast.makeText(this,"Product Rating fetched Successfully.",Toast.LENGTH_SHORT).show()
                    userRating=it.userRating!!
                    if(userRating>0)
                        setProductRating()
                }
                else
                    Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setProductRating(){
        when(userRating){
            1->Star1()
            2->Star2()
            3->Star3()
            4->Star4()
            5->Star5()
        }
    }

    private fun Star1(){
        _binding.apply {
            star1.setImageResource(R.drawable.ic_rating_star_new_selected)
            star2.setImageResource(R.drawable.ic_rating_star_new)
            star3.setImageResource(R.drawable.ic_rating_star_new)
            star4.setImageResource(R.drawable.ic_rating_star_new)
            star5.setImageResource(R.drawable.ic_rating_star_new)
        }
    }
    private fun Star2(){
        _binding.apply {
            star1.setImageResource(R.drawable.ic_rating_star_new_selected)
            star2.setImageResource(R.drawable.ic_rating_star_new_selected)
            star3.setImageResource(R.drawable.ic_rating_star_new)
            star4.setImageResource(R.drawable.ic_rating_star_new)
            star5.setImageResource(R.drawable.ic_rating_star_new)
        }
    }
    private fun Star3(){
        _binding.apply {
            star1.setImageResource(R.drawable.ic_rating_star_new_selected)
            star2.setImageResource(R.drawable.ic_rating_star_new_selected)
            star3.setImageResource(R.drawable.ic_rating_star_new_selected)
            star4.setImageResource(R.drawable.ic_rating_star_new)
            star5.setImageResource(R.drawable.ic_rating_star_new)
        }
    }
    private fun Star4(){
        Log.d("star","star-4 is called")
        _binding.apply {
            star1.setImageResource(R.drawable.ic_rating_star_new_selected)
            star2.setImageResource(R.drawable.ic_rating_star_new_selected)
            star3.setImageResource(R.drawable.ic_rating_star_new_selected)
            star4.setImageResource(R.drawable.ic_rating_star_new_selected)
            star5.setImageResource(R.drawable.ic_rating_star_new)
        }
    }
    private fun Star5(){
        _binding.apply {
            star1.setImageResource(R.drawable.ic_rating_star_new_selected)
            star2.setImageResource(R.drawable.ic_rating_star_new_selected)
            star3.setImageResource(R.drawable.ic_rating_star_new_selected)
            star4.setImageResource(R.drawable.ic_rating_star_new_selected)
            star5.setImageResource(R.drawable.ic_rating_star_new_selected)
        }
    }

    private fun drawStepView(isReturn:Boolean?,finalStepList:List<String>){
        _binding.apply {
            stepView.state
                .steps(finalStepList)
                .animationDuration(resources.getInteger(android.R.integer.config_shortAnimTime))
                .typeface(ResourcesCompat.getFont(this@OrderActivity, R.font.overpass))
                // other state methods are equal to the corresponding xml attributes
                .commit()

            stepView.go(apiStatusArraySize-1,false)
        }
    }

    private fun isReturnWindowLeft(orderStatusDate:String):Boolean{
        val orderReturnLastDate = Calendar.getInstance()
        val presentDate = Calendar.getInstance()

        orderReturnLastDate.time = isoDateFormat.parse(orderStatusDate)
        orderReturnLastDate.add(Calendar.DAY_OF_MONTH, 7) //date of delivery + return window
        orderReturnLastDate.set(Calendar.HOUR_OF_DAY,24)
        orderReturnLastDate.set(Calendar.MINUTE,59)
        orderReturnLastDate.set(Calendar.SECOND,59)

        if(presentDate.time.after(orderReturnLastDate.time))
            return false
        return true

    }

    private fun cancelOrder(){
        _binding.apply {
            val builder = AlertDialog.Builder(this@OrderActivity)
            //set title for alert dialog
            builder.setTitle("Cancel Order")
            //set message for alert dialog
            builder.setMessage("Are you sure you want to Cancel the Order ?")
            builder.setIcon(android.R.drawable.ic_dialog_alert)

            //performing positive action
            builder.setPositiveButton("Yes"){dialogInterface, which ->
//                    (application as MyApplication).clearQueue()
                cancelOrderAPI()
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
    private fun cancelOrderAPI(){
        authViewModel.cancelOrder(orderId!!)
        authViewModel.respCancelOrder.observe({lifecycle}){
            if(it?.success!!){
                Toast.makeText(this,"Order Cancelled Successfully.",Toast.LENGTH_SHORT).show()
//                updateStepView()
                getOrderById(orderId)
            }
            else{
                Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
            }
        }
    }

//    private fun updateStepView(){
//        _binding.apply {
//            if(statusList.size==2){     //ordered..shipped..cancelled
//                stepView.state
//                    .steps(cancelledArray2)
//                    .animationDuration(resources.getInteger(android.R.integer.config_shortAnimTime))
//                    .typeface(ResourcesCompat.getFont(this@OrderActivity, R.font.overpass))
//                    // other state methods are equal to the corresponding xml attributes
//                    .commit()
//
//                stepView.go(apiStatusArraySize-1,false)
//            }
//            else if(statusList.size==1){
//                if(statusList[0]=="ordered"){ // ordered..cancelled
//                    stepView.state
//                        .steps(cancelledArray1)
//                        .animationDuration(resources.getInteger(android.R.integer.config_shortAnimTime))
//                        .typeface(ResourcesCompat.getFont(this@OrderActivity, R.font.overpass))
//                        // other state methods are equal to the corresponding xml attributes
//                        .commit()
//                }
//                else{
//                    stepView.state
//                        .steps(cancelledArray3)
//                        .animationDuration(resources.getInteger(android.R.integer.config_shortAnimTime))
//                        .typeface(ResourcesCompat.getFont(this@OrderActivity, R.font.overpass))
//                        // other state methods are equal to the corresponding xml attributes
//                        .commit()
//                }
//            }
//        }
//
//    }

//    private fun renameStatus(stepsList:ArrayList<String>){
//        stepsList.forEach {
//            val currentState=it
//            var counter=0
//            var transformed = ""
//            currentState.forEach{t->
//                if (counter == 0) {
//                    transformed += t.toUpperCase();
//                }
//                //everything else
//                if (counter != 0 && t == t.toUpperCase()) {
//                    transformed+= " ";
//                    transformed+=t;
//                }
//                else if(counter != 0 && t == t.toLowerCase()){
//                    transformed+=t;
//                }
//                //increment counter
//                counter++;
//            }
//            renamedStepsList.add(transformed)
//        }
//    }



}