package com.example.buildup.ui.Address.layouts

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buildup.AuthViewModel
import com.example.buildup.TinyDB
import com.example.buildup.databinding.ActivityAddressesBinding
import com.example.buildup.ui.Address.adapters.AddressRadioAdapter
import com.example.buildup.MyApplication
import com.example.buildup.ui.BottomNavigation.MyCustomDialogWishlist
import com.example.buildup.ui.Property.layouts.AddPropertyActivity

class AddressesActivity : AppCompatActivity(), MyCustomDialogAddress.OnInputListener{
    companion object {
        var PREFS_FILE_AUTH = "prefs_property"
        var PREFS_KEY_TOKEN = "propertyId"
    }

    private lateinit var _binding: ActivityAddressesBinding
//    private lateinit var addressAdapter: AddressAdapter
    private lateinit var addressRadioAdapter: AddressRadioAdapter
    private lateinit var sharedPrefrences: SharedPreferences
    private lateinit var tinyDB: TinyDB
    private var propertyIdAddress:String?=null


    private lateinit var authViewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_addresses)

        _binding = ActivityAddressesBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)
        sharedPrefrences = getSharedPreferences(PREFS_FILE_AUTH, Context.MODE_PRIVATE)
        tinyDB= TinyDB(this)

//        addressAdapter= AddressAdapter({onEditAddrressBtnClicked(it!!)},{onDeleteAddressBtnClicked(it!!)})
        addressRadioAdapter= AddressRadioAdapter({onEditAddrressBtnClicked(it!!)},{onDeleteAddressBtnClicked(it!!)},{onRadioBtnClicked(it!!)})

        _binding.addressRecyclerView.layoutManager= LinearLayoutManager(this)
//        _binding.addressRecyclerView.adapter=addressAdapter
        _binding.addressRecyclerView.adapter=addressRadioAdapter


        setContentView(_binding.root)

        drawLayout()
        _binding.btnRetry.setOnClickListener {
//            drawLayout()
            finish();
            startActivity(intent);
        }


//        getAddresses()

        _binding.btnAddAddress.setOnClickListener {
            startActivity(Intent(this,AddPropertyActivity::class.java))
        }

        _binding.backBtn.setOnClickListener {
            finish()
        }

    }
    private fun getAddresses(){
        authViewModel.getAddresses()
        authViewModel.respGetAddresses.observe({lifecycle}){
            if(it?.success!!){
//                addressAdapter.submitList(it.properties)
                addressRadioAdapter.submitList(it.properties)

            }
            else{
                if(it.error!="Network Failure")
                    Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onEditAddrressBtnClicked(propertyId:String){
        val intent=Intent(this,AddPropertyActivity::class.java)
        intent.putExtra("propertyId",propertyId)
        startActivity(intent)

    }
    private fun onDeleteAddressBtnClicked(propertyId: String){
        propertyIdAddress=propertyId
        MyCustomDialogAddress().show(supportFragmentManager, "MyCustomFragment")

    }
    override fun sendInput(input: String?) {
        if(input=="yes")
            deleteAddress(propertyIdAddress!!)
    }

    private fun deleteAddress(propertyId: String){
        authViewModel.deletePropertyAddress(propertyId)
        authViewModel.respDeletePropertyAddress.observe({lifecycle}){
            if(it?.success!!) {
                getAddresses()
            }
            else{
                if(it.error!="Network Failure")
                    Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun onRadioBtnClicked(propertyId: String){
//        sharedPrefrences.edit {
//            putString("propertyIdForCart",propertyId)
//        }
        tinyDB.putString("propertyIdForCart",propertyId)

//        (application as MyApplication).addPropertyId(propertyId)

        finish()
    }

    override fun onResume() {
        super.onResume()
        getAddresses()
    }

    private fun isNetworkAvailable(): Boolean {
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)

        return (capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))

    }
    private fun drawLayout() {
        if (isNetworkAvailable()) {
            Log.d("internet","internet")
            _binding.addressRecyclerView.visibility=View.VISIBLE
            _binding.noInternetLayout.visibility=View.GONE
            _binding.btnAddAddress.visibility=View.VISIBLE
        } else {
            Log.d("internet","no internet")
            _binding.addressRecyclerView.visibility=View.GONE
            _binding.noInternetLayout.visibility=View.VISIBLE
            _binding.btnAddAddress.visibility=View.GONE

        }
    }
}