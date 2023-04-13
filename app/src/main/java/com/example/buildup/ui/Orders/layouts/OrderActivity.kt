package com.example.buildup.ui.Orders.layouts

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Intent
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.example.api.BuildUpClient
import com.example.buildup.AuthViewModel
import com.example.buildup.R
import com.example.buildup.TinyDB
import com.example.buildup.databinding.ActivityOrderBinding
import com.example.buildup.extensions.newLoadImage
import com.example.buildup.ui.ReturnOrder.ReturnActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.*
import java.io.*
import java.util.*
import kotlin.collections.ArrayList


class OrderActivity : AppCompatActivity(),MyCustomDialogOrder.OnInputListener,MyCustomDialogOrderNew.OnInputListenerNew {
    @SuppressLint("ConstantLocale")
    val isoDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())

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
    private lateinit var tinyDB : TinyDB



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_order)
        _binding = ActivityOrderBinding.inflate(layoutInflater)
        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        setContentView(_binding.root)
        tinyDB = TinyDB(this)

        drawLayout()
        _binding.btnRetry.setOnClickListener {
//            drawLayout()
            finish();
            startActivity(intent);
        }

//        array.add("Ordered")
//        array.add("Shipped")
//        array.add("Out For Delivery")
//        array.add("Delivered")

        orderId=intent.getStringExtra("orderId")
//        Toast.makeText(this,"order Id : "+orderId,Toast.LENGTH_SHORT).show()

        val savedName=tinyDB.getString("userName")
        val savedMobile="+91 " + tinyDB.getString("userMobile")
        val savedEmail=tinyDB.getString("userEmail")

        _binding.apply {
            star1.setOnClickListener {
                rating=1
                addProductRating()
                Star1()
            }

            star2.setOnClickListener {
                rating=2
                addProductRating()
                Star2()

            }

            star3.setOnClickListener {
                rating=3
                addProductRating()
                Star3()

            }

            star4.setOnClickListener {
                rating=4
                addProductRating()
                Star4()

            }

            star5.setOnClickListener {
                rating=5
                addProductRating()
                Star5()
            }

            backBtn.setOnClickListener {
                startActivity(Intent(this@OrderActivity, OrdersActivity::class.java))
            }

            invoiceLayout.setOnClickListener {
//                requestPermission()
                downloadInvoice()
            }
            iconHelp.setOnClickListener {
                helpAndSupport(orderId!!,savedName,savedEmail,savedMobile)
            }

        }

        getOrderById(orderId)
    }

    private fun helpAndSupport(orderId:String,username:String,email:String,mobile:String){
        val subject="Help - $orderId"
        val message="Type your query here...\n\n\n From\n$username\n$email\n$mobile"
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.fragment_help_and_support, null)
        val callLayout = view.findViewById<ConstraintLayout>(R.id.call_layout)
        val emailLayout = view.findViewById<ConstraintLayout>(R.id.email_layout)
        val cancelBtn = view.findViewById<ImageView>(R.id.btn_cancel)

        var helplineContactNumber=""
        var helplineContactEmail=""
        authViewModel.getContactDetails()
        authViewModel.respGetContactDetails.observe({lifecycle}){
            if(it?.success!!){
                helplineContactNumber=it.details!!.mobileNo
                helplineContactEmail=it.details!!.email
            }
            else{
                if(it.error!="Network Failure")
                    Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
            }
        }
        callLayout.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${helplineContactNumber}")
            startActivity(intent)
            dialog.dismiss()
        }
        emailLayout.setOnClickListener {
            val email = Intent(Intent.ACTION_SEND)
            email.putExtra(Intent.EXTRA_EMAIL, arrayOf<String>(helplineContactEmail))
            email.putExtra(Intent.EXTRA_SUBJECT, subject)
            email.putExtra(Intent.EXTRA_TEXT, message)
            email.type = "message/rfc822"
            startActivity(Intent.createChooser(email, "Choose an Email client :"))
            dialog.dismiss()
        }
        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setContentView(view)
        dialog.show()
    }
    private fun downloadInvoice(){
//        val url="http://192.168.0.103:5000/api/order/${orderId}/download-invoice"
        val url="https://api.buildup.org.in/api/order/${orderId}/download-invoice"

        val request=DownloadManager.Request(Uri.parse(url)).addRequestHeader("Authorization","Token " + BuildUpClient.authToken)
        request.setTitle(orderId)
        request.setMimeType("application/pdf")
        request.allowScanningByMediaScanner()
        request.setAllowedOverMetered(true)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,orderId)
        var dm:DownloadManager= getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        dm.enqueue(request)

        viewInvoice()
    }
    private fun viewInvoice(){
        var file=File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"/${orderId}")
        val uri=FileProvider.getUriForFile(this,"com.example.buildup"+".provider",file)
        val intent=Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri,"application/pdf")
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.flags= Intent.FLAG_GRANT_READ_URI_PERMISSION
        startActivity(intent)

    }
    private fun getOrderById(orderId:String?){
        if(orderId.isNullOrBlank())
            Toast.makeText(this,"OrderId is Null",Toast.LENGTH_SHORT).show()
        else{
            authViewModel.getOrderById(orderId)
            authViewModel.respGetOrderById.observe({lifecycle}){ it ->
                if(it?.success!!){
                    _binding.apply {
                        mainLayout.visibility=View.VISIBLE
                        idPBLoading.visibility=View.GONE

                        var finalStepList=listOf<String>()
                        val apiStatusArray=it.order!!.packageId.shipping.tracking.status

                        if(!apiStatusArray.isNullOrEmpty() && it.order!!.isCancelled==null){ //order goes as expected
                            paymentInfoLayout.visibility=View.VISIBLE
                            paymentLayout.visibility=View.VISIBLE

                            confirmationLayoutParent.visibility=View.GONE
                            timelineLayout.visibility=View.VISIBLE


                            val currentStatus:String=apiStatusArray.last().name
                            if(apiStatusArray[0].name=="ordered")
                                finalStepList=array
                            else if(apiStatusArray[0].name=="returned")
                                finalStepList=returnArray

                            if(currentStatus=="delivered"){
                                ratingLayout.visibility=View.VISIBLE
                                invoiceLayout.visibility=View.VISIBLE
                                cancelBtn.text="RETURN"
                                cancelBtn.setIconResource(R.drawable.ic_icon_return_button)
                                if(!isReturnWindowLeft(apiStatusArray.last().time))
                                    cancelBtn.visibility=View.GONE
                                cancelBtn.setOnClickListener {
                                    val intent=Intent(this@OrderActivity,ReturnActivity::class.java)
                                    intent.putExtra("orderId",orderId)
                                    startActivity(intent)
                                }
                            }


                            if(currentStatus=="outForDelivery" || currentStatus=="outForPickup" || currentStatus=="pickedUp" || currentStatus=="refund")
                                cancelBtn.visibility=View.GONE

                            if(currentStatus=="ordered" || currentStatus=="shipped" || currentStatus=="returned"){
                                _binding.apply {
                                    cancelBtn.visibility=View.VISIBLE
                                    cancelBtn.setOnClickListener {
                                        MyCustomDialogOrderNew().show(supportFragmentManager, "MyCustomFragment")

                                    }
                                }
                            }
                            if(it.order!!.isReturnAvailed!=null && it.order!!.isReturnAvailed!!)
                                cancelBtn.visibility= View.GONE

                            apiStatusArraySize=apiStatusArray.size


                            Log.d("apiStatusArraySize",apiStatusArraySize.toString())
                            Log.d("finalStepList",finalStepList.toString())
                            getUserProductRating()
                            drawStepView(finalStepList)
                        }

                        else if(apiStatusArray.isNullOrEmpty() &&  it.order!!.isCancelled==null){  //order under confirmation
                            confirmationLayoutParent.visibility=View.VISIBLE
                            timelineLayout.visibility=View.GONE
                            stepView.visibility=View.GONE
                            ivOrderCancelled.setImageResource(R.drawable.ic_bg_image_confirmation)
                            confirmationText.text="Your order is being processed"
                            confirmationIcon.setImageResource(R.drawable.ic_icon_confirmation_awaited_final)
                            confirmationLayout.setBackgroundColor(Color.parseColor("#F69421"))

                            cancelBtnAlternate.setOnClickListener {
                                MyCustomDialogOrderNew().show(supportFragmentManager, "MyCustomFragment")

                            }
                            paymentInfoLayout.visibility=View.GONE
                            paymentLayout.visibility=View.GONE

                        }
                        else if(it.order!!.isCancelled!!.value && !it.order!!.isCancelled!!.bySelf){ //order cancelled by admin
                            confirmationLayoutParent.visibility=View.VISIBLE
                            timelineLayout.visibility=View.GONE
                            cancelBtnAlternate.visibility=View.GONE

                            paymentInfoLayout.visibility=View.GONE
                            paymentLayout.visibility=View.GONE

                        }

                        else if(it.order!!.isCancelled!!.value && it.order!!.isCancelled!!.bySelf){ //order cancelled by user
                            Log.d("userCancelled","reached here")
                            confirmationLayoutParent.visibility=View.GONE
                            timelineLayout.visibility=View.VISIBLE
                            cancelBtn.visibility=View.GONE


                            if(apiStatusArray.isNullOrEmpty()){
                                //order cancelled by user before confirmation
                                finalStepList=cancelledArray1
                                apiStatusArraySize=2  // timeline = ordered..cancelled

                                paymentInfoLayout.visibility=View.GONE
                                paymentLayout.visibility=View.GONE
                            }
                            else if(apiStatusArray.isNotEmpty()){
                                //order maybe cancelled by user before/after cancellation

                                if(isOrderCancelledBeforeConfirmation(it.order!!.isCancelled!!.time, it.order!!.packageId.shipping.tracking.status?.get(0)!!.time)){
                                    //order cancelled before confirmation
                                    finalStepList=cancelledArray1
                                    apiStatusArraySize=2  // timeline = ordered..cancelled

                                    paymentInfoLayout.visibility=View.GONE
                                    paymentLayout.visibility=View.GONE
                                }
                                else{
                                    //order cancelled after confirmation
                                    paymentInfoLayout.visibility=View.VISIBLE
                                    paymentLayout.visibility=View.VISIBLE

                                    if(apiStatusArray.size==2) {
                                        finalStepList=cancelledArray2  // ordered..shipped..cancelled
                                        apiStatusArraySize=3
                                    }
                                    else if(apiStatusArray.size==1){
                                        apiStatusArraySize=2  // timeline = ordered..cancelled  ||  returned..cancelled
                                        if(apiStatusArray[0].name=="ordered")
                                            finalStepList=cancelledArray1   // ordered..cancelled
                                        else if(apiStatusArray[0].name=="returned")
                                            finalStepList=cancelledArray3   //returned..cancelled

                                    }

                                }
                            }
                            drawStepView(finalStepList)
                        }

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
                        tvClientName.text=it.order!!.packageId.shipping.customer
//                        tvShippingAddress.text=it.order!!.shipping.address
                        tvClientMobileNumber.text=it.order!!.packageId.shipping.mobileNo
                        tvShippingAddress.text=it.order!!.packageId.shipping.address + ", "  + it.order!!.packageId.shipping.city + ", " + it.order!!.packageId.shipping.state + " - " + it.order!!.packageId.shipping.pincode
                        tvOrderId.text="Order Id - " + it.order!!.id
                        tvOrderProductDetail.text= tvProductQuantity.toString() + " x " + brandName + " " + productName
                        tvTotalMrp.text="₹ " + totalMrp.toString()
                        tvDiscountedPrice.text="₹ " + discountedPrice.toString()
                        tvTotalDiscount.text="- ₹ " + (totalMrp-discountedPrice).toString()
                        tvDeliveryCharge.text=it.order!!.packageId.shipping.chargeForUser.toString()
                        if(it.order!!.packageId.shipping.chargeForUser!=null)
                            tvTotalCartValue.text="₹ " + (discountedPrice+it.order!!.packageId.shipping.chargeForUser!!.toInt()).toString()

                    }
                }

                else
                    if(it.error!="Network Failure")
                        Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()            }
        }
    }

    private fun isOrderCancelledBeforeConfirmation(orderCancelledTimeApi:String, statusObjectTimeApi:String):Boolean{

        val orderCancelledTime = Calendar.getInstance()
        orderCancelledTime.add(Calendar.HOUR,5)
        orderCancelledTime.add(Calendar.MINUTE,30)

        val statusObjectTime = Calendar.getInstance()
        statusObjectTime.add(Calendar.HOUR,5)
        statusObjectTime.add(Calendar.MINUTE,30)

        orderCancelledTime.time = isoDateFormat.parse(orderCancelledTimeApi)
        statusObjectTime.time = isoDateFormat.parse(statusObjectTimeApi)

        if(orderCancelledTime.before(statusObjectTime) || orderCancelledTime==statusObjectTime){
            return true
        }
        return false
    }


    private fun addProductRating(){
        isSuccess=false
        Log.d("isSuccessUnderFuncTop",isSuccess.toString())
        if(!productId.isNullOrEmpty()){
            authViewModel.addProductRating(productId!!, rating)
            authViewModel.respAddProductRating.observe({lifecycle}){
                if(it?.success!!){
                    isSuccess=true
                    Log.d("isSuccessUnderFuncBw",isSuccess.toString())

                }
                else{
                    isSuccess=false
                    if(it.error!="Network Failure")
                        Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
                }
            }
        }
        Log.d("isSuccessUnderFuncBottom",isSuccess.toString())
    }

    private fun getUserProductRating(){
        if(!productId.isNullOrEmpty()){
            authViewModel.getUserProductRating(productId!!)
            authViewModel.respGetUserProductrating.observe({lifecycle}){
                if(it?.success!!){
                    userRating=it.userRating!!
                    if(userRating>0)
                        setProductRating()
                }
                else
                    if(it.error!="Network Failure")
                        Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()            }
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
            Star1()
            star2.setImageResource(R.drawable.ic_rating_star_new_selected)

        }
    }
    private fun Star3(){
        _binding.apply {
            Star2()
            star3.setImageResource(R.drawable.ic_rating_star_new_selected)

        }
    }
    private fun Star4(){
        Log.d("star","star-4 is called")
        _binding.apply {
            Star3()
            star4.setImageResource(R.drawable.ic_rating_star_new_selected)
        }
    }
    private fun Star5(){
        _binding.apply {
            Star4()
            star5.setImageResource(R.drawable.ic_rating_star_new_selected)
        }
    }

    private fun drawStepView(finalStepList:List<String>){
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

    override fun sendInput(input: String?) {
        if(input=="yes")
            cancelAllOrders();
    }

    override fun sendInputNew(input: String?) {
        if(input=="yes")
            cancelOrder();
    }
    private fun cancelAllOrders(){
        authViewModel.cancelOrder(orderId!!,true)
        authViewModel.respCancelOrder.observe({lifecycle}){
            if(it?.success!!){
                Log.d("abcdif","reached here")
//                getOrderById(orderId)
                finish()
                startActivity(intent)
            }
            else{
                Log.d("abcdelse","reached here")
                if(it.error!="Network Failure" && it.error!="Package contains more Orders")
                    Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun cancelOrder(){
        authViewModel.cancelOrder(orderId!!,false)
        authViewModel.respCancelOrder.observe({lifecycle}){
            if(it?.error!=null && it.error=="Package contains more Orders"){
                MyCustomDialogOrder().show(supportFragmentManager, "MyCustomFragment")
            }
            else if(it?.success!=null && it.success!!){
                finish()
                startActivity(intent)
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

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, OrdersActivity::class.java))

    }

    private fun isNetworkAvailable(): Boolean {
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)

        return (capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))

    }
    private fun drawLayout() {
        if (isNetworkAvailable()) {
            Log.d("internet","internet")
//            _binding.mainLayout.visibility=View.VISIBLE
            _binding.noInternetLayout.visibility=View.GONE
        } else {
            Log.d("internet","no internet")
            _binding.mainLayout.visibility=View.GONE
            _binding.noInternetLayout.visibility=View.VISIBLE
            _binding.idPBLoading.visibility=View.GONE
        }
    }



}