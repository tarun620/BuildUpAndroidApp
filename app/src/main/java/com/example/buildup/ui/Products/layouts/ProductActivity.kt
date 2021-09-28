package com.example.buildup.ui.Products.layouts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.buildup.AuthViewModel
import com.example.buildup.R
import com.example.buildup.databinding.ActivityExpenditureBinding
import com.example.buildup.databinding.ActivityProductBinding
import com.example.buildup.ui.BottomNavigation.CartActivity

class ProductActivity : AppCompatActivity() {
    private lateinit var _binding:ActivityProductBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityProductBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)

//        setContentView(R.layout.activity_product)
        setContentView(_binding.root)

        if (getSupportActionBar() != null) {
            getSupportActionBar()?.hide();
        }

        var productId=intent.getStringExtra("productId")

        _binding.backBtn.setOnClickListener {
            finish()
        }
        _binding.btnCart.setOnClickListener {
            val intent=Intent(this,CartActivity::class.java)
            startActivity(intent)
        }

        Toast.makeText(this,"this is product Activity with prdoctId:${productId}",Toast.LENGTH_SHORT).show()
    }
}