package com.example.buildup.ui.LoginSignup

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.core.content.edit
import androidx.lifecycle.ViewModelProvider
import com.example.buildup.AuthViewModel
import com.example.buildup.TinyDB
import com.example.buildup.databinding.ActivityNewSignupBinding
import com.example.buildup.ui.HomeActivity


class NewSignupActivity : AppCompatActivity() {
    companion object {
        var PREFS_FILE_AUTH = "prefs_auth"
        var PREFS_KEY_TOKEN = "token"
    }
    private lateinit var sharedPrefrences: SharedPreferences
    private var _binding: ActivityNewSignupBinding?=null
    lateinit var authViewModel: AuthViewModel
    var mobileNoEditText: String?=null
    private lateinit var tinyDB : TinyDB


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_new_signup)

        _binding= ActivityNewSignupBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)
        sharedPrefrences = getSharedPreferences(PREFS_FILE_AUTH, Context.MODE_PRIVATE)

        setContentView(_binding?.root)

        drawLayout()
        _binding!!.btnRetry.setOnClickListener {
//            drawLayout()
            finish();
            startActivity(intent);
        }

        tinyDB = TinyDB(this)



        // Build a GoogleSignInClient with the options specified by gso.


        mobileNoEditText= intent.getStringExtra("mobileNo")

        _binding?.apply {
            signUpButton.setOnClickListener {
                if(validationSignUp()){
                    completeProfile(mobileNoEditText!!,nameEditText.text.toString(),emailEditText.text.toString())
                }
            }
        }
    }

    private fun completeProfile(mobileNo:String,name:String,email:String){
        authViewModel.completeProfile(mobileNo,name,email)

        authViewModel.respNew.observe({lifecycle}){
            if(it?.token!=null && it.success!!){
                it.token?.let { t ->
                    sharedPrefrences.edit {
                        putString("token", t)
                    }
                } ?: run {                       //     ?: IS CALLED ELVIS OPERATOR
                    sharedPrefrences.edit {
                        remove("token")
                    }
                }
                if(!it.user!!.profileImage.isNullOrBlank())
                    tinyDB.putString("userProfileImage",it.user!!.profileImage)
                tinyDB.putString("userName",it.user!!.name)
                tinyDB.putString("userMobile",it.user!!.mobileNo)
                val intent= Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }
            else{
                if(it!!.error!="Network Failure")
                    Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()            }
        }
    }

    private fun validationSignUp(): Boolean {
        _binding?.apply {

            nameTextInputLayout.error = null
            emailTextInputLayout.error = null
            nameTextInputLayout.isErrorEnabled=false
            emailTextInputLayout.isErrorEnabled=false

            val regex = """^[A-Za-z ]+${'$'}""".toRegex()  // TODO: name with space in between is not accepted

            if(nameEditText.text.toString().isNullOrBlank() || !nameEditText.text?.matches(regex)!!){
                nameTextInputLayout.error= "Please enter Valid Name"
                nameTextInputLayout.requestFocus()
            }
            else if (emailEditText.text.toString().isNullOrBlank() || !Patterns.EMAIL_ADDRESS.matcher(emailEditText.text.toString()).matches()) {
                emailTextInputLayout.error = "Please enter a valid email"
                emailTextInputLayout.requestFocus()
                return false
            }else {
                return true
            }
        }
        return false
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this,LoginSignupSelectorActivity::class.java))
    }

    private fun isNetworkAvailable(): Boolean {
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)

        return (capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))

    }
    private fun drawLayout() {
        if (isNetworkAvailable()) {
            Log.d("internet","internet")
            _binding!!.mainLayout.visibility= View.VISIBLE
            _binding!!.noInternetLayout.visibility= View.GONE
        } else {
            Log.d("internet","no internet")
            _binding!!.mainLayout.visibility= View.GONE
            _binding!!.noInternetLayout.visibility= View.VISIBLE

        }
    }


}