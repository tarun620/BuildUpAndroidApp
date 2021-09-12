package com.example.buildup.ui.Property.layouts

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.buildup.AuthViewModel
import com.example.buildup.R
import com.example.buildup.databinding.ActivityAddPropertyBinding
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import java.util.*

class AddPropertyActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityAddPropertyBinding
    private lateinit var authViewModel: AuthViewModel
    private  var addressType: String = "Home"
    private lateinit var dialog: Dialog


    private val REQUEST_CHECK_SETTINGS = 1000
    private lateinit var locationRequest: LocationRequest

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var currentAddress: String = "No address Found"

    private lateinit var locationCallback: LocationCallback

    var count: Int = 0

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_add_property)
        _binding = ActivityAddPropertyBinding.inflate(layoutInflater)
        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)


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

            locationButton.setOnClickListener{
                showDialog()
                turnOnGPS()

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

    fun turnOnGPS() {

        locationRequest = LocationRequest.create()
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        locationRequest.setInterval(300)
        locationRequest.setFastestInterval(300)

        val builder: LocationSettingsRequest.Builder =
            LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        builder.setAlwaysShow(true)

        val result: Task<LocationSettingsResponse> =
            LocationServices.getSettingsClient(this)
                .checkLocationSettings(builder.build())


        result.addOnCompleteListener { task ->
            try {
                val response: LocationSettingsResponse = task.getResult(ApiException::class.java)

                getLocation() //user device location is already turned on and hence didn't entre the catch block
            } catch (ex: ApiException) {
                when (ex.statusCode) {

                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {  //user device location is turned off
                        val resolvableApiException = ex as ResolvableApiException
                        resolvableApiException
                            .startResolutionForResult(
                                this,
                                REQUEST_CHECK_SETTINGS
                            )
                    } catch (e: IntentSender.SendIntentException) {
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {  //user device doesn't have location
                    }
                }
            }
        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            44 -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    turnOnGPS()

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(
                        this,
                        "We need location permission to move forward. Try again.",
                        Toast.LENGTH_LONG
                    ).show()
                }
                return
            }
        }
    }

    private fun getLocation() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                44
            )
        } else {

//            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())


            Log.e("atElseOfGetLocation", "reached here")

            fusedLocationProviderClient.lastLocation.addOnCompleteListener(object :
                OnCompleteListener<Location> {

                @SuppressLint("MissingPermission")
                override fun onComplete(p0: Task<Location>) {

                    Log.e("atOnOnCompleteGetLoc", "reached here")

                    val location = p0.result

//                    Log.e("locationValue", location.toString())

                    if (location != null) {

                        Log.e("atif(location != null)", "reached here")

                        val geoCoder = Geocoder(this@AddPropertyActivity, Locale.getDefault())
                        val address: ArrayList<Address>? = geoCoder.getFromLocation(
                            location.latitude, location.longitude, 1
                        ) as ArrayList<Address>?

                        currentAddress =
                            address?.get(0)?.subLocality + ", " + address?.get(0)?.locality
                        _binding.locationButton.text = currentAddress
                        _binding.locationButton.setBackgroundColor(resources.getColor(R.color.textGrey))

                        hideDialog()

                        Log.d("Location if", currentAddress)

//                        startAddPostActivity(currentAddress)


                    } else {
//                        showDialog()


                        Log.e("above_requestUpdates", "arrived here")



                        locationCallback = object : LocationCallback() {
                            override fun onLocationResult(locationResult: LocationResult) {
                                super.onLocationResult(locationResult)

                                for (location in locationResult.locations) {

                                    if (count == 0) {

                                        Log.e("latitude : ", location.latitude.toString())
                                        Log.e("longitude : ", location.longitude.toString())

                                        latitude = location.latitude
                                        longitude = location.longitude
                                        count += 1
                                    } else {

                                        Log.e("above remove updates : ", "arrived here")
//                                                fusedLocationProviderClient.removeLocationUpdates(
//                                                    object :
//                                                        LocationCallback() {})

                                        Log.e("below remove updates : ", "arrived here")
                                        break
                                    }
                                }


                                Log.e("above remove function: ", "arrived here")
                                removeLocationUpdates()
                                Log.e("below remove function: ", "arrived here")

                                val geoCoder =
                                    Geocoder(this@AddPropertyActivity, Locale.getDefault())
                                val address: ArrayList<Address>? =
                                    geoCoder.getFromLocation(
                                        latitude, longitude, 1
                                    ) as ArrayList<Address>?

                                currentAddress =
                                    address?.get(0)?.subLocality + ", " + address?.get(0)?.locality

                                Log.d("Location else", currentAddress)
                                _binding.locationButton.text = currentAddress
                                _binding.locationButton.setBackgroundColor(resources.getColor(R.color.textGrey))

                                hideDialog()

//                                startAddPostActivity(currentAddress)
                            }
                        }

                        fusedLocationProviderClient.requestLocationUpdates(
                            locationRequest,
                            locationCallback,
                            Looper.myLooper()
                        )
                    }
                }
            })
        }
    }

    fun removeLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    fun showDialog() {
        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before

        dialog.setContentView(R.layout.progress_bar_layout)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawableResource(
            android.R.color.transparent
        )
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.show()
    }

    fun hideDialog() {
        dialog.dismiss()
    }
}