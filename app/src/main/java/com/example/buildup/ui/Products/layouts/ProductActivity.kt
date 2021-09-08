package com.example.buildup.ui.Products.layouts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.buildup.R

class ProductActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        var productId=intent.getStringExtra("productId")

        Toast.makeText(this,"this is product Activity with prdoctId:${productId}",Toast.LENGTH_SHORT).show()
    }
}