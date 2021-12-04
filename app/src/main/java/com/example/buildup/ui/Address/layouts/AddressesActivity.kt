package com.example.buildup.ui.Address.layouts

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.edit
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buildup.AuthViewModel
import com.example.buildup.R
import com.example.buildup.databinding.ActivityAddressesBinding
import com.example.buildup.databinding.ActivityCartBinding
import com.example.buildup.ui.Address.adapters.AddressAdapter
import com.example.buildup.ui.Address.adapters.AddressRadioAdapter
import com.example.buildup.ui.Cart.adapters.CartAdapter
import com.example.buildup.ui.Property.layouts.AddPropertyActivity
import com.example.buildup.ui.Property.layouts.PropertyActivity

class AddressesActivity : AppCompatActivity() {
    companion object {
        var PREFS_FILE_AUTH = "prefs_property"
        var PREFS_KEY_TOKEN = "propertyId"
    }

    private lateinit var _binding: ActivityAddressesBinding
//    private lateinit var addressAdapter: AddressAdapter
    private lateinit var addressRadioAdapter: AddressRadioAdapter
    private lateinit var sharedPrefrences: SharedPreferences


    private lateinit var authViewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_addresses)

        _binding = ActivityAddressesBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)
        sharedPrefrences = getSharedPreferences(PREFS_FILE_AUTH, Context.MODE_PRIVATE)

//        addressAdapter= AddressAdapter({onEditAddrressBtnClicked(it!!)},{onDeleteAddressBtnClicked(it!!)})
        addressRadioAdapter= AddressRadioAdapter({onEditAddrressBtnClicked(it!!)},{onDeleteAddressBtnClicked(it!!)},{onRadioBtnClicked(it!!)})

        _binding.addressRecyclerView.layoutManager= LinearLayoutManager(this)
//        _binding.addressRecyclerView.adapter=addressAdapter
        _binding.addressRecyclerView.adapter=addressRadioAdapter


        setContentView(_binding.root)

        if (getSupportActionBar() != null) {
            getSupportActionBar()?.hide();
        }

        getAddresses()

        _binding.btnAddAddress.setOnClickListener {
            startActivity(Intent(this,AddPropertyActivity::class.java))
        }

    }
    private fun getAddresses(){
        authViewModel.getAddresses()
        authViewModel.respGetAddresses.observe({lifecycle}){
            if(it?.success!!){
//                addressAdapter.submitList(it.properties)
                addressRadioAdapter.submitList(it.properties)

            }
            else
                Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
        }
    }

    private fun onEditAddrressBtnClicked(propertyId:String){
        val intent=Intent(this,AddPropertyActivity::class.java)
        intent.putExtra("propertyId",propertyId)
        startActivity(intent)

    }
    private fun onDeleteAddressBtnClicked(propertyId: String){
        authViewModel.deletePropertyAddress(propertyId)
        authViewModel.respDeletePropertyAddress.observe({lifecycle}){
            if(it?.success!!) {
                getAddresses()
                Toast.makeText(this, "propertyAddress Deleted", Toast.LENGTH_SHORT).show()
            }
            else
                Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
        }
    }

    private fun onRadioBtnClicked(propertyId: String){
        sharedPrefrences.edit {
            putString("propertyId",propertyId)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        getAddresses()
    }
}