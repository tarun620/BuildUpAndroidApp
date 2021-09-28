package com.example.buildup.ui.Products.layouts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.buildup.AuthViewModel
import com.example.buildup.R
import com.example.buildup.databinding.ActivityProductBinding
import com.example.buildup.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {
    private lateinit var _binding: ActivitySearchBinding
    private lateinit var authViewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivitySearchBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)
//        setContentView(R.layout.activity_search)
        setContentView(_binding.root)

        _binding.backBtn.setOnClickListener {
            finish()
        }

    }
}