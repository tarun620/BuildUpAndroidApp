package com.example.buildup.ui.Updates

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buildup.AuthViewModel
import com.example.buildup.R
import com.example.buildup.databinding.FragmentUpdatesBottomSheet2Binding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [UpdatesBottomSheetFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UpdatesBottomSheetFragment : Fragment() {
    private lateinit var _binding: FragmentUpdatesBottomSheet2Binding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var updatesAdapter: UpdatesAdapter
    private lateinit var propertyId:String

    // TODO: Rename and change types of parameters
    private val ARG_PARAM1 = "param1"
    private  val ARG_PARAM2 = "param2"
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)
        updatesAdapter= UpdatesAdapter()
        _binding= FragmentUpdatesBottomSheet2Binding.inflate(inflater,container,false)
        _binding.updatesRecyclerView.layoutManager= LinearLayoutManager(context)
        _binding.updatesRecyclerView.adapter=updatesAdapter

//        Log.d("propertyIdFragment",arguments?.getString("propertyId")!!)
//        propertyId = arguments?.getString("propertyId")!!
//        val bundle = arguments
//        propertyId = bundle?.getString("propertyId").toString()
//        Log.d("propertyId in fragment",propertyId)
        propertyId="6153282604447a0fec5b2938"


        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getUpdates(propertyId)
    }

    fun getUpdates(propertyId:String){
        authViewModel.getUpdates(propertyId)
        authViewModel.respUpdatesArray.observe({lifecycle}){
            if(it?.success!!){
                Toast.makeText(context,"updates fetching successful", Toast.LENGTH_SHORT).show()
                Log.d("updates",it.updates?.size.toString())
                updatesAdapter.submitList(it.updates)

            }
            else{
                Toast.makeText(context,it.error, Toast.LENGTH_SHORT).show()
                Log.d("errorUpdates",it.error.toString())
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UpdatesBottomSheetFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UpdatesBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}