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
import com.example.buildup.databinding.ActivityExpenditureBinding

class ExpenditureActivity : AppCompatActivity() {
    private lateinit var _binding:ActivityExpenditureBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var expenditureAdapter: ExpenditureAdapter
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val propertyId:String?=intent.getStringExtra("propertyId")
//        Log.d("expproprtyid",propertyId.toString())

        Log.d("expenditurePropertyId",propertyId.toString())
        _binding= ActivityExpenditureBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)

        expenditureAdapter= ExpenditureAdapter()
        _binding.expenditureRecyclerView.layoutManager=LinearLayoutManager(this)
        _binding.expenditureRecyclerView.adapter=expenditureAdapter

        setContentView(_binding?.root)

        if (getSupportActionBar() != null) {
            getSupportActionBar()?.hide();
        }

        swipeRefreshLayout=findViewById(R.id.swipeRefreshLayout)

        getExpenditureAmount(propertyId!!)
        getExpenditureArray(propertyId)

        swipeRefreshLayout.setOnRefreshListener{
            Toast.makeText(this,"refreshed.",Toast.LENGTH_SHORT).show()
            getExpenditureAmount(propertyId)
            getExpenditureArray(propertyId)
            swipeRefreshLayout.isRefreshing = false
        }
    }

    fun getExpenditureAmount(propertyId:String){
        authViewModel.getTotalExpenditure(propertyId)

        authViewModel.respTotalExpenditure.observe({lifecycle}){
            if(it?.success!!){
                Toast.makeText(this,"expenditure amount fetched successfully.",Toast.LENGTH_SHORT).show()
                val totalExpenditure:Int?=it.expenditureAmount?.totalPaid
                val moneyPaid:Int?= it.expenditureAmount?.totalReceived
                val balance=totalExpenditure!!-moneyPaid!!

                Log.d("expenditureTotal",totalExpenditure.toString())
                Log.d("expenditurePaid",balance.toString())
                _binding.apply {
                    tvTotalExpenditure.text= totalExpenditure.toString()
                    tvMoneyPaid.text=moneyPaid.toString()
                    tvBalance.text=balance.toString()
                }
            }
            else{
                Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
                Log.d("errorExpenditureAmount",it.error.toString())
            }

        }
    }

    fun getExpenditureArray(propertyId: String){
        authViewModel.getExpenditureArray(propertyId)

        authViewModel.respExpenditureArray.observe({lifecycle}){
            if(it?.success!!){
                Toast.makeText(this,"expenditure array fetched successfully.",Toast.LENGTH_SHORT).show()
                Log.d("expenditureArraySize",it.expenditures?.size.toString())
                expenditureAdapter.submitList(it.expenditures)
            }
            else{
                Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
                Log.d("errorExpenditureArray",it.error.toString())
            }

        }
    }
}