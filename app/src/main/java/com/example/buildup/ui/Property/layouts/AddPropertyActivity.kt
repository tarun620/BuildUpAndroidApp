package com.example.buildup.ui.Property.layouts

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.api.models.responsesAndData.property.propertyEntities.AddPropertyData
import com.example.buildup.AuthViewModel
import com.example.buildup.R
import com.example.buildup.databinding.ActivityAddPropertyBinding
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import java.util.*
import com.google.android.gms.location.LocationRequest
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import kotlinx.coroutines.android.asCoroutineDispatcher
import kotlin.collections.ArrayList

class AddPropertyActivity : AppCompatActivity() {
    companion object{
        private const val PERMISSION_REQUEST_ACCESS_LOCATION=100
    }
    private lateinit var _binding: ActivityAddPropertyBinding
    private lateinit var authViewModel: AuthViewModel
    private var propertyId:String?=null
    //    private  var addressType: String?=null
//    private lateinit var dialogProgress: Dialog
    private lateinit var dialogSuccess: Dialog


    private val REQUEST_CHECK_SETTINGS = 1000
    private lateinit var locationRequest: LocationRequest

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var currentAddress: String = "No address Found"

    private lateinit var locationCallback: LocationCallback

    var count: Int = 0

    private var latitude: Double=0.0
    private var longitude: Double=0.0
    private lateinit var coordinates:ArrayList<Double>
    private var addressType: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_add_property)
        _binding = ActivityAddPropertyBinding.inflate(layoutInflater)
        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        propertyId=intent.getStringExtra("propertyId")

        coordinates=ArrayList()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

//        locationRequest = LocationRequest.create()
//        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//        locationRequest.interval = 300
//        locationRequest.fastestInterval = 300

//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            requestPermission()
//            return
//        }
//        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())


        setContentView(_binding.root)

        drawLayout()
        _binding.btnRetry.setOnClickListener {
//            drawLayout()
            finish();
            startActivity(intent);
        }

//        _binding.idPBLoading.visibility= View.GONE


        if(propertyId.isNullOrEmpty() || propertyId.isNullOrBlank()){
            _binding.mainLayout.visibility=View.VISIBLE
            _binding.idPBLoading.visibility=View.GONE
        }
        if(!propertyId.isNullOrBlank()){
            getPropertyAddressById(propertyId!!)
        }

        _binding.backBtn.setOnClickListener {
            finish()
        }
        _binding?.apply {
            radioTypeGroup.setOnCheckedChangeListener { group, checkedId ->
                if (checkedId == radioHome.id) {
                    addressType = "home"
                } else if (checkedId == radioWork.id) {
                    addressType = "work"
                }
            }

            locationButton.setOnClickListener {
//                showProgressDialog()
//                turnOnGPS()
                getCurrentLocation()
        }
//            _binding.apply {
//                etFullNameLayout.error = null
//                etMobileLayout.error = null
//                etPincodeLayout.error = null
//                etStateLayout.error = null
//                etCityLayout.error = null
//                etHousNoLayout.error = null
//                etColonyLayout.error = null
//            }


            var landmark:String?=null

            Log.d("landmark",landmark.toString())
            submitButton.setOnClickListener {
                if(!etLandmark.text.isNullOrBlank())
                    landmark=etLandmark.text.toString()
                if (validation()) {
                    if(!propertyId.isNullOrBlank()){
                        authViewModel.editPropertyAddress(
                            propertyId!!,
                        AddPropertyData(
                            etName.text.toString(),
                            etMobile.text.toString(),
                            addressType!!,
                            etHouseNo.text.toString(),
                            etColony.text.toString(),
                            etCity.text.toString(),
                            etState.text.toString(),
                            etPincode.text.toString().toInt(),
                            coordinates,
                            landmark
                        ))

                        authViewModel.respEditPropertyAddress.observe({ lifecycle }) {
                            if (it?.success!!) {
                                finish()
                            } else {
//                                if(it.error!="Network Failure")
//                                    Toast.makeText(this@AddPropertyActivity,it.error,Toast.LENGTH_SHORT).show()
                                val snackBar = Snackbar.make(
                                    view, it.error!!,
                                    Snackbar.LENGTH_SHORT
                                ).setAction("Action", null)
                                snackBar.show()
                            }
                        }
                    }
                    else{
                        authViewModel.addProperty(
                            etName.text.toString(),
                            etMobile.text.toString(),
                            addressType!!,
                            etHouseNo.text.toString(),
                            etColony.text.toString(),
                            etCity.text.toString(),
                            etState.text.toString(),
                            etPincode.text.toString().toInt(),
                            coordinates,
                            landmark
                        )

                        authViewModel.resp.observe({ lifecycle }) {
                            if (it?.success!!) {
                                finish()
                            } else {
//                                if(it.error!="Network Failure")
//                                    Toast.makeText(this@AddPropertyActivity,it.error,Toast.LENGTH_SHORT).show()
                                val snackBar = Snackbar.make(
                                    view, it.error!!,
                                    Snackbar.LENGTH_SHORT
                                ).setAction("Action", null)
                                snackBar.show()
                            }
                        }
                    }
                }
                else
                    Toast.makeText(this@AddPropertyActivity,"Enter All Required Details",Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun validation(): Boolean {
        _binding.apply {
            etFullNameLayout.error = null
            etMobileLayout.error = null
            etPincodeLayout.error = null
            etStateLayout.error = null
            etCityLayout.error = null
            etHousNoLayout.error = null
            etColonyLayout.error = null

            etFullNameLayout.isErrorEnabled=false
            etMobileLayout.isErrorEnabled=false
            etPincodeLayout.isErrorEnabled=false
            etStateLayout.isErrorEnabled=false
            etCityLayout.isErrorEnabled=false
            etHousNoLayout.isErrorEnabled=false
            etColonyLayout.isErrorEnabled=false


            if (etName.text.toString().isNullOrBlank() || etName.text.toString() == "") {
                etFullNameLayout.error = "Please Fill Full Name"
                etFullNameLayout.requestFocus()
                return false
            } else if (etMobile.text.isNullOrBlank() || etMobile.text.toString().length < 10) {
                etMobileLayout.error = "Enter Mobile Number"
                etMobileLayout.requestFocus()
                return false
            }
            else if (etPincode.text.isNullOrBlank() || etPincode.text.toString().length < 6) {
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

//    fun turnOnGPS() {
////        Log.d("coordinates","In turnOnGPS")
//        locationRequest = LocationRequest.create()
//        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//        locationRequest.interval = 300
//        locationRequest.fastestInterval = 300
//
//        val builder: LocationSettingsRequest.Builder =
//            LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
//
//        builder.setAlwaysShow(true)
//
//        val result: Task<LocationSettingsResponse> =
//            LocationServices.getSettingsClient(this)
//                .checkLocationSettings(builder.build())
//
//
//        result.addOnCompleteListener { task ->
//            try {
////                Log.d("location","reached in try block")
//                val response: LocationSettingsResponse = task.getResult(ApiException::class.java)
//                if(response.locationSettingsStates.isLocationPresent){
//                    Log.d("location","reached in try block")
////                    hideProgressDialog()
//                }
//                getLocation() //user device location is already turned on and hence didn't entre the catch block
//            } catch (ex: ApiException) {
//                Log.d("location","reached in catch block")
//                when (ex.statusCode) {
//                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {  //user device location is turned off
//                        Log.d("location","inside try of catch block")
//                        val resolvableApiException = ex as ResolvableApiException
//                        resolvableApiException
//                            .startResolutionForResult(
//                                this,
//                                REQUEST_CHECK_SETTINGS
//                            )
//                    } catch (e: IntentSender.SendIntentException) {
//                    }
//                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {  //user device doesn't have location
//                    }
//                }
//            }
//        }
//
//    }


//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//
//        when (requestCode) {
//            44 -> {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//
//                    turnOnGPS()
//
//                } else {
//
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                    Toast.makeText(
//                        this,
//                        "We need location permission to move forward. Try again.",
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//                return
//            }
//        }
//    }

//    private fun getLocation() {
//
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//                ),
//                44
//            )
////            hideProgressDialog()
//        } else {
//
//
//            Log.e("atElseOfGetLocation", "reached here")
//
//            fusedLocationProviderClient.lastLocation.addOnCompleteListener(object :
//                OnCompleteListener<Location> {
//
//                @SuppressLint("MissingPermission")
//                override fun onComplete(p0: Task<Location>) {
//
//                    Log.e("atOnOnCompleteGetLoc", "reached here")
//
//                    val location = p0.result
//
////                    Log.e("locationValue", location.toString())
//
//                    if (location != null) {
//
//                        Log.e("atif(location != null)", "reached here")
//
//                        val geoCoder = Geocoder(this@AddPropertyActivity, Locale.getDefault())
//                        val address: MutableList<Address>? = geoCoder.getFromLocation(
//                            location.latitude, location.longitude, 1
//                        )
//                        // adding coordinates value to arrayList
//                        coordinates.add(location.longitude)
//                        coordinates.add(location.latitude)
//
//                        val colony = address?.get(0)?.subLocality
//                        val city = address?.get(0)?.locality
//
//                        val state = address?.get(0)?.adminArea
//                        val postalCode = address?.get(0)?.postalCode
//
//
//
//                        _binding.apply{
//                            etState.setText(state.toString())
//                            etCity.setText(city.toString())
//                            etCity.setText(city.toString())
//                            etState.setText(state.toString())
//                            etPincode.setText(postalCode.toString())
//                            etColony.setText(colony.toString())
//                            locationButton.isEnabled = false
//                            locationButton.setBackgroundColor(getColor(R.color.textGrey))
//                            locationButton.setTextColor(getColor(R.color.white))
////                            etPincode.isEnabled = false
////                            etState.isEnabled = false
////                            etCity.isEnabled = false
//                        }
//
////                        hideProgressDialog()
//
//
////                        startAddPostActivity(currentAddress)
//
//
//                    } else {
////                        showDialog()
//
//
//                        Log.e("above_requestUpdates", "arrived here")
//
//
//
//                        locationCallback = object : LocationCallback() {
//                            override fun onLocationResult(locationResult: LocationResult) {
//                                super.onLocationResult(locationResult)
//
//                                for (location in locationResult.locations) {
//
//                                    if (count == 0) {
//
//                                        Log.e("latitude : ", location.latitude.toString())
//                                        Log.e("longitude : ", location.longitude.toString())
//
//                                        latitude = location.latitude
//                                        longitude = location.longitude
//                                        count += 1
//                                    } else {
//
//                                        Log.e("above remove updates : ", "arrived here")
////                                                fusedLocationProviderClient.removeLocationUpdates(
////                                                    object :
////                                                        LocationCallback() {})
//
//                                        Log.e("below remove updates : ", "arrived here")
//                                        break
//                                    }
//                                }
//
//
//                                Log.e("above remove function: ", "arrived here")
//                                removeLocationUpdates()
//                                Log.e("below remove function: ", "arrived here")
//
//                                val geoCoder =
//                                    Geocoder(this@AddPropertyActivity, Locale.getDefault())
//                                val address: MutableList<Address>? =
//                                    geoCoder.getFromLocation(
//                                        latitude, longitude, 1
//                                    )
//
//                                val colony = address?.get(0)?.subLocality
//                                val city = address?.get(0)?.locality
//
//                                val state = address?.get(0)?.adminArea
//                                val postalCode = address?.get(0)?.postalCode
//
//
//                                _binding.apply{
//                                    etState.setText(state.toString())
//                                    etCity.setText(city.toString())
//                                    etCity.setText(city.toString())
//                                    etState.setText(state.toString())
//                                    etPincode.setText(postalCode.toString())
//                                    etColony.setText(colony.toString())
//                                    locationButton.isEnabled = false
//                                    locationButton.setBackgroundColor(getColor(R.color.textGrey))
//                                    locationButton.setTextColor(getColor(R.color.white))
////                            etPincode.isEnabled = false
////                            etState.isEnabled = false
////                            etCity.isEnabled = false
//                                }
//
//
////                                hideProgressDialog()
//
////                                startAddPostActivity(currentAddress)
//                            }
//                        }
//
//                        fusedLocationProviderClient.requestLocationUpdates(
//                            locationRequest,
//                            locationCallback,
//                            Looper.myLooper()
//                        )
////                        hideProgressDialog()
//                    }
//                }
//            })
//        }
//    }

//    private fun removeLocationUpdates() {
//        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
//    }

//    private fun showProgressDialog() {
//        dialogProgress = Dialog(this)
//        dialogProgress.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
//
//        dialogProgress.setContentView(R.layout.asset_progress_bar)
//        dialogProgress.setCancelable(false)
//        dialogProgress.window?.setBackgroundDrawableResource(
//            android.R.color.transparent
//        )
//        val lp = WindowManager.LayoutParams()
//        lp.copyFrom(dialogProgress.window!!.attributes)
//        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
//        dialogProgress.show()
//    }
//    private fun hideProgressDialog() {
//        Log.d("location","reached iin hideProgressBar()")
//        dialogProgress.dismiss()
//    }

//    private fun showSuccessDialog(){
//        dialogSuccess = Dialog(this)
//        dialogSuccess.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
//        dialogSuccess.setContentView(_bindingDialog.root)
//        _bindingDialog.titleText.text ="Property\nAdded\nSuccessfully"
//
//        dialogSuccess.setCancelable(false)
//
//        val lp = WindowManager.LayoutParams()
//        lp.copyFrom(dialogSuccess.window!!.attributes)
//        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
//        dialogSuccess.show()
//
//
//        Handler().postDelayed({
//            dialogSuccess.dismiss()
//            finish()  // closes the current acitivity and goes back to PropertiesActivity
//        }, 3000)
//
//    }


    private fun getPropertyAddressById(propertyId:String){
        authViewModel.getAddressById(propertyId)
        authViewModel.respGetAddressById.observe({lifecycle}){
            if(it?.success!!){
                _binding.mainLayout.visibility=View.VISIBLE
                _binding.idPBLoading.visibility=View.GONE
                _binding.apply {
                    etName.setText(it.property?.propertyName)
                    etMobile.setText(it.property?.mobileNo.toString())
                    etPincode.setText(it.property?.address?.pincode!!.toString())
                    etState.setText(it.property?.address?.state)
                    etCity.setText(it.property?.address?.city)
                    etHouseNo.setText(it.property?.address?.houseNo)
                    etColony.setText(it.property?.address?.colony)
                    Log.d("propertyId",propertyId)
//                    Log.d("landmark",it.property?.address?.landmark.toString())
                    if(!it.property?.address?.landmark.isNullOrBlank())
                        etLandmark.setText(it.property?.address?.landmark)

                    if(it.property?.type=="home"){
                        radioHome.isChecked=true
                        radioWork.isChecked=false
                    }
                    else if(it.property?.type=="work"){
                        radioHome.isChecked=false
                        radioWork.isChecked=true
                    }
                }
            }
            else{
                if(it.error!="Network Failure")
                    Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
            }
        }
    }

//    private fun isLocationPermissionGranted(): Boolean {
//        return if (ActivityCompat.checkSelfPermission(
//                this,
//                android.Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                android.Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(
//                    android.Manifest.permission.ACCESS_FINE_LOCATION,
//                    android.Manifest.permission.ACCESS_COARSE_LOCATION
//                ),
//                requestcode
//            )
//            false
//        } else {
//            true
//        }
//    }

    private fun getCurrentLocation(){
        if(checkPermissions()){
            if(isLocationEnabled()){
                locationRequest = LocationRequest().setInterval(2000).setFastestInterval(2000)
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermission()
                    return
                }
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult) {
                            super.onLocationResult(locationResult)
                            for (location in locationResult.locations) {
                                setAddressByGPS(location)
                            }
                            // Things don't end here
                            // You may also update the location on your web app
                        }
                    },
                    Looper.myLooper()
                )






//                fusedLocationProviderClient.lastLocation.addOnCompleteListener(this){task->
//                    val location:Location?=task.result
//                    if(location==null){
//                        Toast.makeText(this,"Something Went Wrong, Cannot Access Current Location.",Toast.LENGTH_SHORT).show()
//                    }
//                    else{
//
//                        setAddressByGPS(location)
//
//                    }
//                }




//                locationCallback = object : LocationCallback() {
//                    override fun onLocationResult(locationResult: LocationResult?) {
//                        super.onLocationResult(locationResult)
//                        locationResult?.lastLocation?.let {
//                            setAddressByGPS(it)
//                            // use latitude and longitude as per your need
//                        }
//                    }
//                }
            }

            else{
                //open location settings here
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
        }
        else{
            //request permission here
            requestPermission()

        }
    }

    private fun checkPermissions():Boolean{
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_REQUEST_ACCESS_LOCATION
        )
    }

    private fun isLocationEnabled():Boolean{
        val locationManager:LocationManager=getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode== PERMISSION_REQUEST_ACCESS_LOCATION){
            if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getCurrentLocation()
            }
            else{
                Toast.makeText(this,"Permission Denied.",Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun setAddressByGPS(location:Location){
        val geoCoder = Geocoder(this, Locale.getDefault())
        val address: MutableList<Address>? = geoCoder.getFromLocation(
            location.latitude, location.longitude, 1
        )
        // adding coordinates value to arrayList
//                        coordinates.add(location.longitude)
//                        coordinates.add(location.latitude)

        val colony = address?.get(0)?.subLocality
        val city = address?.get(0)?.locality

        val state = address?.get(0)?.adminArea
        val postalCode = address?.get(0)?.postalCode



        _binding.apply{
            etState.setText(state.toString())
            etCity.setText(city.toString())
            etCity.setText(city.toString())
            etState.setText(state.toString())
            etPincode.setText(postalCode.toString())
            etColony.setText(colony.toString())
            locationButton.isEnabled = false
            locationButton.setBackgroundColor(getColor(R.color.textGrey))
            locationButton.setTextColor(getColor(R.color.white))
//                            etPincode.isEnabled = false
//                            etState.isEnabled = false
//                            etCity.isEnabled = false
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)

        return (capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))

    }
    private fun drawLayout() {
        if (isNetworkAvailable()) {
            Log.d("internet","internet")
//            _binding.mainLayout.visibility=View.VISIBLE
            _binding.noInternetLayout.visibility=View.GONE
            _binding.submitButton.visibility=View.VISIBLE
        } else {
            Log.d("internet","no internet")
            _binding.mainLayout.visibility=View.GONE
            _binding.noInternetLayout.visibility=View.VISIBLE
            _binding.idPBLoading.visibility=View.GONE
            _binding.submitButton.visibility=View.GONE

        }
    }



}