package com.example.buildup.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.buildup.AuthViewModel
import com.example.buildup.R
import com.example.buildup.databinding.ActivityUpdatesBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MyDialogBottomSheet :BottomSheetDialogFragment() {
    private lateinit var updatesAdapter:UpdatesAdapter
    private lateinit var _binding: ActivityUpdatesBinding
    private lateinit var authViewModel: AuthViewModel
    private var propertyId:String?=null
//    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    companion object {
        const val PROPERTY_ID = "property_id"


        fun newInstance(propertyId: String): MyDialogBottomSheet {
            val fragment = MyDialogBottomSheet()

            val bundle = Bundle().apply {
                putString(PROPERTY_ID, propertyId)
            }

            fragment.arguments = bundle

            return fragment
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        propertyId = arguments?.getString(PROPERTY_ID)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)
        updatesAdapter= UpdatesAdapter()
        _binding=ActivityUpdatesBinding.inflate(inflater,container,false)
        _binding.updatesRecyclerView.layoutManager= LinearLayoutManager(context)
        _binding.updatesRecyclerView.adapter=updatesAdapter
//        _binding.updatesRecyclerView.isNestedScrollingEnabled=true
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getUpdates(propertyId!!)

    }

    fun getUpdates(propertyId:String){
        authViewModel.getUpdates(propertyId)
        authViewModel.respUpdatesArray.observe({lifecycle}){
            if(it?.success!!){
                Toast.makeText(context,"updates fetching successful", Toast.LENGTH_SHORT).show()
                updatesAdapter.submitList(it.updates)

            }
            else{
                Toast.makeText(context,it.error, Toast.LENGTH_SHORT).show()
                Log.d("errorUpdates",it.error.toString())
            }
        }
    }

}