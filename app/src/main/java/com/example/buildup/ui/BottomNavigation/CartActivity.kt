package com.example.buildup.ui.BottomNavigation

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buildup.AuthViewModel
import com.example.buildup.R
import com.example.buildup.TinyDB
import com.example.buildup.databinding.ActivityCartBinding
import com.example.buildup.ui.Address.layouts.AddressesActivity
import com.example.buildup.ui.Cart.adapters.CartAdapter
import com.example.buildup.ui.HomeActivity
import com.example.buildup.MyApplication
import com.example.buildup.extensions.timeStamp
import com.example.buildup.ui.LottieAnimation.WorkInProgressActivity
import com.example.buildup.ui.Products.layouts.CodPaymentActivity
import com.example.buildup.ui.Products.layouts.ProductActivity
import com.example.buildup.ui.Products.layouts.ProductCategoryActivity
import com.example.buildup.ui.Property.layouts.PropertiesActivity

class CartActivity : AppCompatActivity() {
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

//        sharedPrefrences.edit {
//            remove("propertyIdForCart")
//        }

        (application as MyApplication).clearPropertyId()
//        tinyDB.remove("propertyIdForCart")
//        propertyId= sharedPrefrences.getString("propertyIdForCart",null)
        propertyId=(application as MyApplication).getPropertyId()
//        if(tinyDB.getString("propertyIdForCart")==null)
//            propertyId=null
//        else
//            propertyId=tinyDB.getString("propertyIdForCart")

//        if(propertyId!=null){
////            val intent=Intent(this,PropertiesActivity::class.java)
////            startActivity(intent)
//        }

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
//            startActivity(Intent(this,PropertiesActivity::class.java))
                if(intentFromWishlist!!)
                    startActivity(Intent(this@CartActivity,WishlistActivity::class.java))
                else
                    finish()
            }

            btnChangeAddress.setOnClickListener {
                val intent=Intent(this@CartActivity, AddressesActivity::class.java)
                startActivity(intent)
            }

            btnCheckout.setOnClickListener {
                if(isEmpty)
                    Toast.makeText(this@CartActivity,"Cart is Empty.",Toast.LENGTH_SHORT).show()
                else
                    placeOrder(propertyId)
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
                }
                else{
                    _binding.mainLayout.visibility= View.VISIBLE
                    _binding.idPBLoading.visibility= View.GONE
                    _binding.emptyCartLayout.visibility=View.GONE
                }
                Toast.makeText(this,"cart items fetched successfully.", Toast.LENGTH_SHORT).show()
                isEmpty =it.items!!.isEmpty()
                cartAdapter.submitList(it.items)
//                cartValue=0
//                for(i in it.items!!){
//                    cartValue+=(i.product.amount) * i.quantity
//                    Log.d("cartValue",cartValue.toString())
//                }
//                _binding.apply {
//                    tvSubtotal.text="₹ " + cartValue.toString()
//                    tvShipping.text="₹ " + "50" //hardcoded
//                    tvTotal.text="₹ " + (cartValue+50).toString()
//                }
            }
            else{
                Toast.makeText(this,it.error, Toast.LENGTH_SHORT).show()
            }
        }
    }
//
    private fun openProductActivity(productId:String?){
        val intent= Intent(this, ProductActivity::class.java)
        intent.putExtra("productId",productId)
        startActivity(intent)

    }
    private fun increaseProductQuantity(productId: String?){
        var currQuantity=1
        authViewModel.respGetProductsFromCart.observe({lifecycle}){
            for(i in it?.items!!){
                if(i.product.id==productId)
                currQuantity=i.quantity
            }
        }
        authViewModel.updateProductQuantityCart(productId!!,currQuantity+1)
        authViewModel.respUpdateProductQuantityCart.observe({lifecycle}){
            if(it?.success!!)
                getProductsFromCart()
            else
                Toast.makeText(this,it.error, Toast.LENGTH_SHORT).show()
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
                if (it?.success!!)
                    getProductsFromCart()
                else
                    Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun removeProductFromCart(productId:String?){
        isEmpty=true
        authViewModel.removeProductFromCart(productId!!)
        authViewModel.respRemoveProductFromCart.observe({lifecycle}){
            if(it?.success!!)
                getProductsFromCart()

            else
                Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
        }
    }
    private fun setupBottomNavigationBar() {

        _binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {

                R.id.nav_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))


                }
                R.id.nav_cart -> {


                }

                R.id.nav_property -> {

                    startActivity(Intent(this, WorkInProgressActivity::class.java))

                }

                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))

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
            var intent=Intent(this,CodPaymentActivity::class.java)
            intent.putExtra("propertyId",propertyId)
            intent.putExtra("shippingCost",shippingCost)
            startActivity(intent)
        }

    }

    private fun changeDeliveryAddress(propertyId: String?){

        Log.d("propertyId",propertyId.toString())


        if(propertyId==null){
            Toast.makeText(this@CartActivity,"Please Select Delivery Address",Toast.LENGTH_SHORT).show()
        }
        else{
            authViewModel.getAddressById(propertyId)
            authViewModel.respGetAddressById.observe({lifecycle}){
                if(it?.success!!){
                    _binding.tvAddress.text=it.property?.address?.houseNo + ", " + it.property?.address?.colony + ", " + it.property?.address?.city + ", " + it.property?.address?.state + " - " + it.property?.address?.pincode
                }
                else{
                    Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getCostDeliveryDetails(){
        authViewModel.getCostDeliveryDetails(propertyId)
        authViewModel.respGetCostDeliveryDetails.observe({lifecycle}){
            if(it?.success!!){
                _binding.apply {
                    tvSubtotal.text="₹ " + it.cost!!.subtotal
                    tvTotal.text="₹ " + it.cost!!.total

                    if(it.estimatedDelivery!=null && it.cost!!.shipping!=null){
                        deliveryDateLayout.visibility=View.VISIBLE
                        tvShipping.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F);
                        tvEstimatedDeliveryDate.timeStamp=it.estimatedDelivery!!
                        tvShipping.text="₹ " + it.cost!!.shipping
                        shippingCost=it.cost!!.shipping!!
                    }
                    else{
                        deliveryDateLayout.visibility=View.GONE
                        tvShipping.text="Select delivery Address"
                        tvShipping.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12F);


                    }

                }

            }
            else
                Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
        }

    }

    override fun onResume() {
        super.onResume()
//        propertyId= sharedPrefrences.getString("propertyIdForCart",null)
        propertyId=(application as MyApplication).getPropertyId()


//        propertyId=tinyDB.getString("propertyIdForCart")
        changeDeliveryAddress(propertyId)
        getProductsFromCart()
        getCostDeliveryDetails()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Log.d("intentFromWishlist",intentFromWishlist.toString())
        if(intentFromWishlist!!)
            startActivity(Intent(this@CartActivity,WishlistActivity::class.java))
        else
            finish()
    }
}

