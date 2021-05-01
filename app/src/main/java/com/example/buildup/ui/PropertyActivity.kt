package com.example.buildup.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.buildup.AuthViewModel
import com.example.buildup.R
import com.example.buildup.databinding.ActivityPropertyBinding
import com.example.buildup.databinding.ActivitySignupGoogleBinding

class PropertyActivity : AppCompatActivity() {

    private lateinit var _binding:ActivityPropertyBinding
    private lateinit var authViewModel: AuthViewModel
    private var completed:Int=0
//    private var propertyId=intent.getStringExtra("propertyId").toString()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_property)

        _binding= ActivityPropertyBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)

        setContentView(_binding?.root)

        val propertyId:String?=intent.getStringExtra("propertyId")

//        Toast.makeText(this,"this is propertyActivity with propertyId: ${propertyId}",Toast.LENGTH_SHORT).show()
//        authViewModel.getUpdates(propertyId!!,0)

//        authViewModel.respUpdatesArray.observe({lifecycle}){
//            if(it?.success!!){
//                Log.d("updatesArray",it.updates.size.toString())
//                Log.d("updatesArray",it.updates[0].description)
//            }
//        }

        authViewModel.getProperty(propertyId!!)

        authViewModel.respProperty.observe({lifecycle}){
            if(it?.success!!){
                _binding.propertyName.text=it.property.name
                _binding.completedStatus.text="Completed Stage : ${it.property.completed.toString()}"
            }
        }

        _binding.getUpdatesButton.setOnClickListener {
            val intent=Intent(this,UpdatesActivity::class.java)
            intent.putExtra("propertyId",propertyId)
            startActivity(intent)
        }

    }
}