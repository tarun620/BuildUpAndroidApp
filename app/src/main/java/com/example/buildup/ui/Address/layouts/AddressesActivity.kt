package com.example.buildup.ui.Address.layouts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buildup.AuthViewModel
import com.example.buildup.R
import com.example.buildup.databinding.ActivityAddressesBinding
import com.example.buildup.databinding.ActivityCartBinding
import com.example.buildup.ui.Address.adapters.AddressAdapter
import com.example.buildup.ui.Cart.adapters.CartAdapter
import com.example.buildup.ui.Property.layouts.AddPropertyActivity

class AddressesActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityAddressesBinding
    private lateinit var addressAdapter: AddressAdapter
    private lateinit var authViewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_addresses)

        _binding = ActivityAddressesBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)

        addressAdapter= AddressAdapter({onEditAddrressBtnClicked(it!!)},{onDeleteAddressBtnClicked(it!!)})
        _binding.addressRecyclerView.layoutManager= LinearLayoutManager(this)
        _binding.addressRecyclerView.adapter=addressAdapter

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
                addressAdapter.submitList(it.properties)
            }
            else
                Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
        }
    }

    private fun onEditAddrressBtnClicked(propertyId:String){
        //TODO : ask backend developer about get address by id
    }
    private fun onDeleteAddressBtnClicked(propertyId: String){
        //TODO : ask developer to make a call to delete the address
    }

    override fun onResume() {
        super.onResume()
        getAddresses()
    }
}