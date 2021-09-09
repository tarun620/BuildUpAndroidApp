package com.example.buildup.ui.Property.layouts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.buildup.AuthViewModel
import com.example.buildup.R
import com.example.buildup.databinding.ActivityPropertiesBinding
import com.example.buildup.databinding.HomeFragmentBinding
import com.example.buildup.ui.Property.adapters.PropertyAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior

class PropertiesActivity : AppCompatActivity() {
//    private lateinit var _binding:ActivityPropertiesBinding
    private lateinit var _binding:HomeFragmentBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var propertyAdapter: PropertyAdapter
    lateinit var swipeRefreshLayout:SwipeRefreshLayout
    private var doubleBackToExitPressedOnce = false
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_logged_in)

        _binding= HomeFragmentBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)
        propertyAdapter= PropertyAdapter{openProperty(it)}

        _binding.propertyRecyclerView.layoutManager= LinearLayoutManager(this)
        _binding.propertyRecyclerView.adapter=propertyAdapter

        setContentView(_binding.root)

        if (getSupportActionBar() != null) {
            getSupportActionBar()?.hide();
        }

//        swipeRefreshLayout=findViewById(R.id.swipeRefreshLayout)


        _binding.addPropertyButton.setOnClickListener {
            var intent= Intent(this, AddPropertyActivity::class.java)
            startActivity(intent)
        }

        getProperties()

//        swipeRefreshLayout.setOnRefreshListener {
//            Toast.makeText(this,"refreshed.",Toast.LENGTH_SHORT).show()
//            getProperties()
//            swipeRefreshLayout.isRefreshing = false
//        }


    }

    fun getProperties(){
        authViewModel.getProperties()
        authViewModel.respPropertyArray.observe({lifecycle}){
            if(it?.success!!){
//                Log.d("completed", it.properties?.get(0)?.completed.toString())
                Toast.makeText(this,"property fetching successful",Toast.LENGTH_SHORT).show()
                propertyAdapter.submitList(it.properties)
            }
            else{
                Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
                Log.d("errorLoggedIn",it.error.toString())
            }

        }
    }

    fun openProperty(propertyId:String?){
        val intent=Intent(this, PropertyActivity::class.java)
        intent.putExtra("propertyId",propertyId)
        startActivity(intent)
//        Toast.makeText(this,"propertyId:${propertyId}",Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
//            return  //closes the current activity only
            this.finishAffinity();   //closes the entire application
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }
}