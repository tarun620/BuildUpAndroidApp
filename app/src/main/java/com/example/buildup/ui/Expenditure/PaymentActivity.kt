package com.example.buildup.ui.Expenditure

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.buildup.AuthViewModel
import com.example.buildup.R
import com.example.buildup.databinding.ActivityExpenditureBinding
import com.example.buildup.databinding.ActivityPaymentBinding

class PaymentActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityPaymentBinding
    private lateinit var authViewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityPaymentBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)
//        setContentView(R.layout.activity_payment)
        setContentView(_binding.root)

        var expenditureId=intent.getStringExtra("expenpenditureId")


    }
}