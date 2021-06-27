package com.example.buildup.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.buildup.AuthViewModel
import com.example.buildup.R
import com.example.buildup.databinding.ActivityPropertyBinding
import com.example.buildup.databinding.ActivityUpdatesBinding

class UpdatesActivity : AppCompatActivity() {

    private lateinit var _binding:ActivityUpdatesBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var updatesAdapter:UpdatesAdapter
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_updates)

        val propertyId:String?=intent.getStringExtra("propertyId")
        Log.d("propertyId",propertyId.toString())

        _binding= ActivityUpdatesBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)
        updatesAdapter= UpdatesAdapter()

        _binding.updatesRecyclerView.layoutManager=LinearLayoutManager(this)
        _binding.updatesRecyclerView.adapter=updatesAdapter

        setContentView(_binding?.root)

        swipeRefreshLayout=findViewById(R.id.swipeRefreshLayout)

        getUpdates(propertyId!!)

        swipeRefreshLayout.setOnRefreshListener{
            Toast.makeText(this,"refreshed.",Toast.LENGTH_SHORT).show()
            getUpdates(propertyId)
            swipeRefreshLayout.isRefreshing = false
        }


    }

    fun getUpdates(propertyId:String){
        authViewModel.getUpdates(propertyId)
        authViewModel.respUpdatesArray.observe({lifecycle}){
            if(it?.success!!){
                Toast.makeText(this,"updates fetching successful", Toast.LENGTH_SHORT).show()
                updatesAdapter.submitList(it.updates)
            }
            else{
                Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
                Log.d("errorUpdates",it.error.toString())
            }
        }
    }
}