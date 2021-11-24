package com.example.buildup.ui.LoginSignup.loginSignupMobileGoogleHomePage

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.edit
import androidx.lifecycle.ViewModelProvider
import com.example.buildup.AuthViewModel
import com.example.buildup.databinding.ActivityChangePasswordBinding
import com.example.buildup.ui.Property.layouts.PropertiesActivity

class ChangePasswordActivity : AppCompatActivity() {
    companion object {
        var PREFS_FILE_AUTH = "prefs_auth"
        var PREFS_KEY_TOKEN = "token"
    }
    private lateinit var sharedPrefrences: SharedPreferences
    private lateinit var _binding: ActivityChangePasswordBinding
    lateinit var authViewModel: AuthViewModel
    var mobileNoEditText: String?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityChangePasswordBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)
        sharedPrefrences = getSharedPreferences(LoginSignupActivity.PREFS_FILE_AUTH, Context.MODE_PRIVATE)

        setContentView(_binding.root)

        //AUTO LOGIN USING SAVED INSTANCE OF LOGIN CREDENTIALS IN SHARED PREFERENCES

//        token=sharedPrefrences.getString("token",null)
//        if(token!=null){
//            BuildUpClient.authToken=token
//            val intent=Intent(this,PropertiesActivity::class.java)
//            startActivity(intent)
//        }


        if (getSupportActionBar() != null) {
            getSupportActionBar()?.hide()
        }

        mobileNoEditText= intent.getStringExtra("mobileNo")

        _binding.backBtn.setOnClickListener {
            finish()
        }
        _binding.resetPasswordButton.setOnClickListener {
            if(validationPassword()){
                authViewModel.setNewPassword(mobileNoEditText!!,_binding.passwordEditText.text.toString())

                authViewModel.respSetNewPassword.observe({lifecycle}){
                    if(it?.success!!){
                        Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
                        login(mobileNoEditText!!,_binding.passwordEditText.text.toString())

                    }
                }
            }
            else{
                Toast.makeText(this,"Please fill all required fields correctly", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun validationPassword(): Boolean {
        _binding?.apply {

            passwordTextInputLayout.error=null
            confirmPasswordTextInputLayout.error=null

            if (passwordEditText.text.toString().isNullOrBlank() || passwordEditText.length() < 8) {
                passwordTextInputLayout.error = "Please input password of atleast 8 characters"
                passwordTextInputLayout.requestFocus()
                return false
            }
            else if (confirmPasswordEditText.text.toString().isNullOrBlank() || confirmPasswordEditText.text.toString() != passwordEditText.text.toString()) {
                confirmPasswordTextInputLayout.error = "Password doesn't match"
                confirmPasswordTextInputLayout.requestFocus()
                return false
            }
            else {
                return true
            }
        }
        return false
    }

    private fun login(mobileNo:String,password:String){
        authViewModel.login(
            mobileNo.toString())

        authViewModel.respNewImage.observe({ lifecycle }) {
            if (it?.token != null && it.success!!) {
                it.token?.let { t ->
                    sharedPrefrences.edit {
                        putString("token", t)
                    }
                } ?: run {                       //     ?: IS CALLED ELVIS OPERATOR
                    sharedPrefrences.edit {
                        remove("token")
                    }
                }
                val intent =
                    Intent(this, PropertiesActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(
                    this,
                    it?.error,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}