package com.example.buildup.ui.Products.layouts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buildup.AuthViewModel
import com.example.buildup.R
import com.example.buildup.data.UserRepo.getBrands
import com.example.buildup.databinding.ActivityFilterBinding
import com.example.buildup.databinding.ActivityProductBinding
import com.example.buildup.ui.FilterBrandAdapter
import com.example.buildup.ui.Products.adapters.ProductAdapter

class FilterActivity : AppCompatActivity() {
    private lateinit var _binding:ActivityFilterBinding
    private lateinit var  authViewModel:AuthViewModel
    private lateinit var filderBrandAdapter:FilterBrandAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityFilterBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)
        setContentView(_binding.root)

        filderBrandAdapter= FilterBrandAdapter{onBrandClicked(it)}

        _binding.listRecyclerView.layoutManager= LinearLayoutManager(this,)
        _binding.listRecyclerView.adapter=filderBrandAdapter

        getBrands()




    }
    private fun getBrands(){
        authViewModel.getBrands(false,null)
        authViewModel.respGetBrands.observe({lifecycle}){
            if(it?.success!!){
                filderBrandAdapter.submitList(it.brands)
            }
            else
                Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
        }
    }
    private fun onBrandClicked(brandId:String?){

    }
}