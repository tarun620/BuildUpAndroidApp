package com.example.buildup.ui.Address.layouts

import android.content.Intent
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
import com.example.buildup.R
import com.example.buildup.databinding.ActivityAddressesBinding
import com.example.buildup.databinding.ActivityAddressesProfileBinding
import com.example.buildup.databinding.ActivityProfileNewBinding
import com.example.buildup.ui.Address.adapters.AddressAdapter
import com.example.buildup.ui.Address.adapters.AddressRadioAdapter
import com.example.buildup.ui.Property.layouts.AddPropertyActivity

class AddressesActivityProfile : AppCompatActivity() {
    private lateinit var authViewModel: AuthViewModel
    private lateinit var _binding: ActivityAddressesProfileBinding
        private lateinit var addressAdapter: AddressAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddressesProfileBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)
        setContentView(_binding.root)

        drawLayout()
        _binding.btnRetry.setOnClickListener {
//            drawLayout()
            finish();
            startActivity(intent);
        }

        //        addressAdapter= AddressAdapter({onEditAddrressBtnClicked(it!!)},{onDeleteAddressBtnClicked(it!!)})
        addressAdapter= AddressAdapter({onEditAddrressBtnClicked(it!!)},{onDeleteAddressBtnClicked(it!!)})

        _binding.addressRecyclerView.layoutManager= LinearLayoutManager(this)
//        _binding.addressRecyclerView.adapter=addressAdapter
        _binding.addressRecyclerView.adapter=addressAdapter

//        getAddresses()

        _binding.btnAddAddress.setOnClickListener {
            startActivity(Intent(this, AddPropertyActivity::class.java))
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
                addressAdapter.submitList(it.properties)

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
        authViewModel.deletePropertyAddress(propertyId)
        authViewModel.respDeletePropertyAddress.observe({lifecycle}){
            if(it?.success!!) {
                getAddresses()
                Toast.makeText(this, "propertyAddress Deleted", Toast.LENGTH_SHORT).show()
            }
            else{
                if(it.error!="Network Failure")
                    Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
            }
        }
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
            _binding.addressRecyclerView.visibility= View.VISIBLE
            _binding.noInternetLayout.visibility= View.GONE
            _binding.btnAddAddress.visibility= View.VISIBLE
        } else {
            Log.d("internet","no internet")
            _binding.addressRecyclerView.visibility= View.GONE
            _binding.noInternetLayout.visibility= View.VISIBLE
            _binding.btnAddAddress.visibility= View.GONE

        }
    }
}