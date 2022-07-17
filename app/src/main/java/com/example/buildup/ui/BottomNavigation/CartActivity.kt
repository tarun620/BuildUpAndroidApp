package com.example.buildup.ui.BottomNavigation

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buildup.AuthViewModel
import com.example.buildup.MyApplication
import com.example.buildup.R
import com.example.buildup.TinyDB
import com.example.buildup.databinding.ActivityCartBinding
import com.example.buildup.ui.Address.layouts.AddressesActivity
import com.example.buildup.ui.Cart.adapters.CartAdapter
import com.example.buildup.ui.HomeActivity
import com.example.buildup.extensions.timeStamp
import com.example.buildup.ui.LoginSignup.LoginSignupSelectorActivity
import com.example.buildup.ui.LottieAnimation.WorkInProgressActivity
import com.example.buildup.ui.Products.layouts.CodPaymentActivity
import com.example.buildup.ui.Products.layouts.ProductActivity
import com.example.buildup.ui.Products.layouts.ProductCategoryActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CartActivity : AppCompatActivity(),MyCustomDialogCart.OnInputListener,MyCustomDialogDelivery.OnInputListenerDelivery, MyCustomDialogPlaceOrder.OnInputListenerPlaceOrder {
    companion object {
        var PREFS_FILE_AUTH = "prefs_property"
        var PREFS_KEY_TOKEN = "propertyId"
    }

    private lateinit var _binding: ActivityCartBinding
    private lateinit var cartAdapter: CartAdapter
    private lateinit var authViewModel: AuthViewModel
    private var propertyId: String? = null
    private lateinit var sharedPrefrences: SharedPreferences
    private var cartValue: Int = 0
    private var isEmpty = true
    private lateinit var tinyDB: TinyDB
    private var shippingCost:Int=-1
    private var intentFromWishlist:Boolean?=false
    private var productIdDelete:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCartBinding.inflate(layoutInflater)
        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        sharedPrefrences = getSharedPreferences(PREFS_FILE_AUTH, Context.MODE_PRIVATE)
        tinyDB= TinyDB(this)

        cartAdapter = CartAdapter({ openProductActivity(it) },
            { increaseProductQuantity(it) },
            { decreaseProductQuantity(it) },
            { removeProductFromCart(it) })
        _binding.cartItemRecyclerView.layoutManager = LinearLayoutManager(this)
        _binding.cartItemRecyclerView.adapter = cartAdapter

        setContentView(_binding.root)

        drawLayout()
        _binding.btnRetry.setOnClickListener {
//            drawLayout()
            finish();
            startActivity(intent);
        }

//        sharedPrefrences.edit {
//            remove("propertyIdForCart")
//        }

//        (application as MyApplication).clearPropertyId()
//        tinyDB.remove("propertyIdForCart")
//        propertyId= sharedPrefrences.getString("propertyIdForCart",null)
//        propertyId=(application as MyApplication).getPropertyId()


        if(tinyDB.getString("propertyIdForCart")==null || tinyDB.getString("propertyIdForCart")=="")
            propertyId=null
        else
        propertyId=tinyDB.getString("propertyIdForCart")

        intentFromWishlist=intent.getBooleanExtra("intentFromWishlist",false)
        _binding.apply {
            bottomNavigationView.background = null
            setupBottomNavigationBar()
//        _binding.bottomNavigationView.selectedItemId(R.id.nav_cart)
            bottomNavigationView.menu.getItem(1).isEnabled = false
            bottomNavigationView.menu.getItem(1).isChecked = true
//
//
            backBtn.setOnClickListener {
                if(intentFromWishlist!!){
                    val intent=Intent(this@CartActivity,WishlistActivity::class.java)
                    intent.putExtra("fromCartActivity",true)
                    startActivity(intent)

                }
                else
                    finish()
            }

            deliveryLayout.setOnClickListener {
                val intent=Intent(this@CartActivity, AddressesActivity::class.java)
                startActivity(intent)
            }

            btnCheckout.setOnClickListener {
                if(isEmpty){
                    Toast.makeText(this@CartActivity,"Cart is Empty.",Toast.LENGTH_SHORT).show()

                }
                else
                    MyCustomDialogPlaceOrder().show(supportFragmentManager, "MyCustomFragment")

            }

            btnAddProducts.setOnClickListener {
                startActivity(Intent(this@CartActivity,ProductCategoryActivity::class.java))
            }
//            getProductsFromCart()
//        changeDeliveryAddress(propertyId)
//        setTotalCartValue()
        }
    }

    private fun getProductsFromCart(){
        authViewModel.getProductsFromCart()

        authViewModel.respGetProductsFromCart.observe({lifecycle}){
            if(it?.success!!){
                if(it.items!!.isEmpty())
                {
                    _binding.mainLayout.visibility= View.GONE
                    _binding.idPBLoading.visibility= View.GONE
                    _binding.emptyCartLayout.visibility=View.VISIBLE
                    _binding.btnCheckout.visibility=View.GONE
                }
                else{
                    _binding.mainLayout.visibility= View.VISIBLE
                    _binding.idPBLoading.visibility= View.GONE
                    _binding.emptyCartLayout.visibility=View.GONE
                    _binding.btnCheckout.visibility=View.VISIBLE
                }
                isEmpty =it.items!!.isEmpty()
                cartAdapter.submitList(it.items)

                var totalMrp=0
                var discountedPrice=0
                var productQuantity=0

                for(i in it.items!!){
                    totalMrp+=i.product.mrp * i.quantity
                    discountedPrice+=i.product.amount * i.quantity
                    productQuantity+=i.quantity

                }
                _binding.apply {
                    tvOrderProductDetail.text= productQuantity.toString() + " x " + "Total Items"
                    tvTotalMrp.text="₹ " + totalMrp.toString()
                    tvDiscountedPrice.text="₹ " + discountedPrice.toString()
                    tvTotalDiscount.text="- ₹ " + (totalMrp-discountedPrice).toString()
                    Log.d("shippingCostAfter",shippingCost.toString())
                    if(shippingCost==-1){
                        tvTotalCartValue.text="₹ " + (discountedPrice).toString()
//                        tvTotalCartValue.text="shipping cost = -1"
                    }
                    else
                        tvTotalCartValue.text="₹ " + (discountedPrice+shippingCost!!).toString()
                }
            }
            else{
                if(it.error=="jwt expired"){
                    startActivity(Intent(this, LoginSignupSelectorActivity::class.java))
                }
                else if(it.error!="Network Failure" && it.error!="Cart is Empty.")
                    Toast.makeText(this,it.error, Toast.LENGTH_SHORT).show()
            }
        }
    }
//
    private fun openProductActivity(productId:String?){
        val intent= Intent(this, ProductActivity::class.java)
        intent.putExtra("productId",productId)
        intent.putExtra("productClickedFromCartIntent",true)
        startActivity(intent)

    }
    fun increaseProductQuantity(productId: String?){
        var currQuantity=1
        authViewModel.respGetProductsFromCart.observe({lifecycle}){
            for(i in it?.items!!){
                if(i.product.id==productId)
                currQuantity=i.quantity
            }
        }
        authViewModel.updateProductQuantityCart(productId!!,currQuantity+1)
        authViewModel.respUpdateProductQuantityCart.observe({lifecycle}){
            if(it?.success!!) {
                getProductsFromCart()
                getCostDeliveryDetails()
            }
            else{
                if(it.error=="jwt expired"){
                    startActivity(Intent(this, LoginSignupSelectorActivity::class.java))
                }
                else if(it.error!="Network Failure")
                    Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun decreaseProductQuantity(productId: String?) {
        var currQuantity = 1
        authViewModel.respGetProductsFromCart.observe({ lifecycle }) {
            for (i in it?.items!!) {
                if (i.product.id == productId)
                    currQuantity = i.quantity
            }
        }
        if (currQuantity == 1) {
            Toast.makeText(
                this,
                "Minimum Quantity cannot be less than 1, Delete the product from delete icon",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            authViewModel.updateProductQuantityCart(productId!!, currQuantity - 1)
            authViewModel.respUpdateProductQuantityCart.observe({ lifecycle }) {
                if (it?.success!!) {
                    getProductsFromCart()
                    getCostDeliveryDetails()
                }
                else {
                    if(it.error=="jwt expired"){
                        startActivity(Intent(this, LoginSignupSelectorActivity::class.java))
                    }
                    else if(it.error!="Network Failure")
                        Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun removeProductFromCart(productId:String?){
        productIdDelete=productId
//        isEmpty=true
//        authViewModel.removeProductFromCart(productId!!)
//        authViewModel.respRemoveProductFromCart.observe({lifecycle}){
//            if(it?.success!!)
//                getProductsFromCart()
//
//            else
//                Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
//        }
        MyCustomDialogCart().show(supportFragmentManager, "MyCustomFragment")


    }
    override fun sendInput(input: String?) {
        if(input=="yes"){
            deleteItem(productIdDelete)
        }
    }
    private fun deleteItem(productId: String?){
        Log.d("productId",productId.toString())
        isEmpty=true
        authViewModel.removeProductFromCart(productId!!)
        authViewModel.respRemoveProductFromCart.observe({lifecycle}){
            if(it?.success!!) {
                getProductsFromCart()
                getCostDeliveryDetails()
            }

            else{
                if(it.error=="jwt expired"){
                    startActivity(Intent(this, LoginSignupSelectorActivity::class.java))
                }
                else if(it.error!="Network Failure")
                    Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun sendInputDelivery(input: String?) {
        if(input=="yes"){
            deliveryNotAvailable()
        }
    }

    private fun deliveryNotAvailable(){
        tinyDB.remove("propertyIdForCart") //setting property id as null
        (application as MyApplication).clearPropertyId() //setting property id as null
        finish();
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        overridePendingTransition(0, 0);
    }

    override fun sendInputPlaceOrder(input: String?) {
        if(input=="yes"){
            placeOrder(propertyId)
        }
    }
    private fun setupBottomNavigationBar() {

        _binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {

                R.id.nav_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    overridePendingTransition(0,0)


                }
                R.id.nav_cart -> {


                }

                R.id.nav_property -> {

                    startActivity(Intent(this, WorkInProgressActivity::class.java))
                    overridePendingTransition(0,0)

                }

                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    overridePendingTransition(0,0)


                }
            }
            true
        }
    }


    private fun placeOrder(propertyId:String?){
        if(propertyId==null){
            Toast.makeText(this@CartActivity,"Please Select Delivery Address",Toast.LENGTH_SHORT).show()
        }
        else{
            val intent=Intent(this,CodPaymentActivity::class.java)
            intent.putExtra("propertyId",propertyId)
            intent.putExtra("shippingCost",shippingCost)
            startActivity(intent)
        }

    }

    private fun changeDeliveryAddress(propertyIdFunc: String?){

        if(propertyIdFunc==null){
            _binding.tvAddress.text="Select Delivery Address"
//            Toast.makeText(this@CartActivity,"Please Select Delivery Address",Toast.LENGTH_SHORT).show()
        }
        else{
            authViewModel.getAddressById(propertyIdFunc)
            authViewModel.respGetAddressById.observe({lifecycle}){
                if(it?.success!!){
                    if(it.property==null){
                        propertyId=null
                        _binding.tvAddress.text="Select Delivery Address"
                    }
                    else
                        _binding.tvAddress.text=it.property?.address?.houseNo + ", " + it.property?.address?.colony + ", " + it.property?.address?.city + ", " + it.property?.address?.state + " - " + it.property?.address?.pincode
                }
                else{
                    if(it.error=="jwt expired"){
                        startActivity(Intent(this, LoginSignupSelectorActivity::class.java))
                    }
                    else if(it.error!="Network Failure")
                        Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()                }
            }
        }
    }

    private fun getCostDeliveryDetails(){
        authViewModel.getCostDeliveryDetails(propertyId)
        authViewModel.respGetCostDeliveryDetails.observe({lifecycle}){
            if(it?.success!!){
                _binding.apply {
                    if(it.isDeliverable!=null && !it.isDeliverable!!){
//                        MyCustomDialogDelivery.isCanceleble (false);
                        MyCustomDialogDelivery().show(supportFragmentManager, "MyCustomFragment")

                    }
                    if(it.estimatedDelivery!=null && it.cost!!.shipping!=null){
                        deliveryDateLayout.visibility=View.VISIBLE
                        tvDeliveryCharge.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F);
                        tvEstimatedDeliveryDate.timeStamp=it.estimatedDelivery!!
                        tvDeliveryCharge.text="₹ " + it.cost!!.shipping!!.toInt()
                        shippingCost=it.cost!!.shipping!!.toInt()
                        if(shippingCost==-1){
                            tvTotalCartValue.text="₹ " + it.cost!!.total.toString()
//                        tvTotalCartValue.text="shipping cost = -1"
                        }
                        else
                            tvTotalCartValue.text="₹ " + it.cost!!.total.toString()


//                        getProductsFromCart()
                        Log.d("shippingCost",shippingCost.toString())
                    }
                    else{
                        deliveryDateLayout.visibility=View.GONE
                        tvDeliveryCharge.text="Select Delivery Address"
                        tvDeliveryCharge.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12F);


                    }

                }

            }
            else{
                if(it.error=="jwt expired"){
                    startActivity(Intent(this, LoginSignupSelectorActivity::class.java))
                }
                else if(it.error!="Network Failure" && it.error!="Cart is Empty.")
                    Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        _binding.bottomNavigationView.menu.getItem(1).isEnabled = false
        _binding.bottomNavigationView.menu.getItem(1).isChecked = true

        if(tinyDB.getString("propertyIdForCart")==null || tinyDB.getString("propertyIdForCart")=="")
            propertyId=null
        else
            propertyId=tinyDB.getString("propertyIdForCart")

        _binding.btnCheckout.isEnabled = !propertyId.isNullOrEmpty()

        getCostDeliveryDetails()
        changeDeliveryAddress(propertyId)
        getProductsFromCart()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(intentFromWishlist!!){
            val intent=Intent(this@CartActivity,WishlistActivity::class.java)
            intent.putExtra("fromCartActivity",true)
            startActivity(intent)

        }
        else
            finish()
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
//            _binding.btnCheckout.visibility=View.VISIBLE
        } else {
            Log.d("internet","no internet")
            _binding.mainLayout.visibility=View.GONE
            _binding.noInternetLayout.visibility=View.VISIBLE
            _binding.idPBLoading.visibility=View.GONE
            _binding.emptyCartLayout.visibility=View.GONE
//            _binding.btnCheckout.visibility=View.GONE
        }
    }
}

