package com.example.buildup.ui.Property.layouts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.buildup.AuthViewModel
import com.example.buildup.databinding.ActivityAddPropertyBinding

class AddPropertyActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityAddPropertyBinding
    private lateinit var authViewModel: AuthViewModel
    private  var addressType: String = "Home"
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
                    addressType == radioHome.text.toString()
                } else if (checkedId == radioWork.id) {
                    addressType == radioWork.text.toString()
                }
            }
            submitButton.setOnClickListener {
                if (validation()) {
                    authViewModel.addProperty(
                        etName.text.toString(),
                        addressType,
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
            if (etName.text.toString() == null || etName.text.toString() == "") {
                etFullNameLayout.error = "Please fill Full name"
                etFullNameLayout.requestFocus()
                return false
            }else if (etPincode.text.toString() == null || etPincode.text.toString() == ""|| etPincode.text.toString().length!=6) {
                etPincodeLayout.error = "Please fill Pincode properly"
                etPincodeLayout.requestFocus()

                return false
            } else if (etState.text.toString() == null || etState.text.toString() == "") {
                etStateLayout.error = "Please fill State"
                etStateLayout.requestFocus()

                return false
            } else if (etCity.text.toString() == null || etCity.text.toString() == "") {
                etCityLayout.error = "Please fill City"
                etCityLayout.requestFocus()

                return false
            } else if (etHouseNo.text.toString() == null || etHouseNo.text.toString() == "") {
                etHousNoLayout.error = "Please fill House no"
                etHousNoLayout.requestFocus()
                return false
            }  else if (etColony.text.toString() == null || etColony.text.toString() == "") {
                etColonyLayout.error = "Please fill Colony"
                etColonyLayout.requestFocus()

                return false
            } else if (addressType == "") {
                Toast.makeText(
                    this@AddPropertyActivity,
                    "Please select Address Type",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }  else {
                return true
            }
        }
    }
}