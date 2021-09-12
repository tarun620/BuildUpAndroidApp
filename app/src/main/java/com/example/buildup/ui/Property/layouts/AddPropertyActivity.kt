package com.example.buildup.ui.Property.layouts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.buildup.AuthViewModel
import com.example.buildup.databinding.ActivityAddPropertyBinding
import com.example.buildup.ui.Property.adapters.PropertyAdapter

class AddPropertyActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityAddPropertyBinding
    private lateinit var authViewModel: AuthViewModel
    private  var addressType: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_add_property)
        _binding = ActivityAddPropertyBinding.inflate(layoutInflater)
        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        setContentView(_binding?.root)

        if (getSupportActionBar() != null) {
            getSupportActionBar()?.hide();
        }

        _binding?.apply {
            radioTypeGroup.setOnCheckedChangeListener { group, checkedId ->
                if (checkedId == radioHome.id) {
                    addressType = "Home"
                } else if (checkedId == radioWork.id) {
                    addressType = "Work"
                }
            }

            submitButton.setOnClickListener {
                if (validation()) {
                    authViewModel.addProperty(
                        etName.text.toString(),
                        addressType!!,
                        etHouseNo.text.toString(),
                        etColony.text.toString(),
                        etCity.text.toString(),
                        etState.text.toString(),
                        etPincode.text.toString().toInt()
                    )
                }

            }


            authViewModel.resp.observe({ lifecycle }) {
                if (it?.success!!) {
                    Toast.makeText(this@AddPropertyActivity, it.message, Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@AddPropertyActivity, it?.error, Toast.LENGTH_SHORT).show()
                    Log.d("errorAddProperty", it?.error.toString())
                }
            }
        }
    }

    private fun validation(): Boolean {
        _binding.apply {
            etFullNameLayout.error = null
            etPincodeLayout.error = null
            etStateLayout.error = null
            etCityLayout.error = null
            etHousNoLayout.error = null
            etColonyLayout.error = null
            if (etName.text.toString() == null || etName.text.toString() == "") {
                etFullNameLayout.error = "Please Fill Full Name"
                etFullNameLayout.requestFocus()
                return false
            } else if (etPincode.text.isNullOrBlank() || etPincode.text.toString().length < 6) {
                etPincodeLayout.error = "Enter Pincode"
                etPincodeLayout.requestFocus()

                return false
            } else if (etState.text.isNullOrBlank()) {
                etStateLayout.error = "Please Fill State"
                etStateLayout.requestFocus()

                return false
            } else if (etCity.text.isNullOrBlank()) {
                etCityLayout.error = "Please Fill City"
                etCityLayout.requestFocus()

                return false
            } else if (etHouseNo.text.toString().isNullOrBlank()) {
                etHousNoLayout.error = "Please Fill House Number"
                etHousNoLayout.requestFocus()
                return false
            } else if (etColony.text.toString().isNullOrBlank()) {
                etColonyLayout.error = "Please Fill Colony"
                etColonyLayout.requestFocus()
                return false
            } else if (addressType.isNullOrBlank()) {
                Toast.makeText(
                    this@AddPropertyActivity,
                    "Please Select Address Type",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            } else {
                return true
            }
        }
    }

}