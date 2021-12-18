package com.example.buildup.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.buildup.AuthViewModel
import com.example.buildup.R
import com.example.buildup.TinyDB
import com.example.buildup.databinding.ActivityNewLoginBinding
import com.example.buildup.databinding.ActivityNewSignupBinding
import com.example.buildup.ui.LoginSignup.loginSignupGoogle.SignupGoogleActivity
import com.example.buildup.ui.LoginSignup.loginSignupMobileGoogleHomePage.LoginSignupActivity
import com.example.buildup.ui.Property.layouts.PropertiesActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class NewSignupActivity : AppCompatActivity() {
    private var _binding: ActivityNewSignupBinding?=null
    lateinit var authViewModel: AuthViewModel
    var mobileNoEditText: String?=null
    private var RC_SIGN_IN = 123
    private lateinit var tinyDB : TinyDB


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_new_signup)

        _binding= ActivityNewSignupBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)
        setContentView(_binding?.root)

        tinyDB = TinyDB(this)


        // GOOGLE LOGIN/SIGNUP

        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestProfile()
                .requestEmail()
                .requestIdToken(resources.getString(R.string.googleAccountWebClientID))
                .build()

        // Build a GoogleSignInClient with the options specified by gso.
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        mobileNoEditText= intent.getStringExtra("mobileNo")

        _binding?.apply {
            signUpButton.setOnClickListener {
                if(validationSignUp()){
                    completeProfile(mobileNoEditText!!,nameEditText.text.toString(),emailEditText.text.toString())
                }
            }

            googleSignupButton.setOnClickListener {
                mGoogleSignInClient.signOut()
                val signInIntent = mGoogleSignInClient.signInIntent
                startActivityForResult(signInIntent, RC_SIGN_IN)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account =
                completedTask.getResult(ApiException::class.java)
            // Signed in successfully, show authenticated UI.
            updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("googleSignin", "signInResult:failed code=" + e.statusCode)
            Log.d("googleSignin", "I am in catch block..")
            updateUI(null)
        }
    }

    private fun updateUI(account: GoogleSignInAccount?) {
        if (account == null) {
            Toast.makeText(
                this,
                "Something Went Wrong. Please try again later.",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            _binding?.apply {
                authViewModel.loginSignupGoogle(
                    account.displayName,
                    account.email,
                    account.photoUrl.toString()
                )
                authViewModel.respNewImageGoogle.observe({ lifecycle }) {
                    if (it?.token != null && it.success!!) {
                        Toast.makeText(this@NewSignupActivity, it.message, Toast.LENGTH_SHORT)
                            .show()
                        val intent =
                            Intent(this@NewSignupActivity, HomeActivity::class.java)
                        startActivity(intent)
                    } else if (it?.token == null && it?.user == null && it?.success!!) {
                        Toast.makeText(this@NewSignupActivity, it.message, Toast.LENGTH_SHORT)
                            .show()
                        val intent =
                            Intent(this@NewSignupActivity, SignupGoogleActivity::class.java)
                        intent.putExtra("emailgoogle", account.email.toString())
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@NewSignupActivity, it.error, Toast.LENGTH_SHORT)
                            .show()
                        Log.d("errorGoogleLogin", it.error.toString())
                    }
                }
            }
        }
    }

    private fun completeProfile(mobileNo:String,name:String,email:String){
        authViewModel.completeProfile(mobileNo,name,email)

        authViewModel.respNew.observe({lifecycle}){
            if(it?.token!=null && it.success!!){
                Toast.makeText(this,it.message, Toast.LENGTH_SHORT).show()
                if(!it.user!!.profileImage.isNullOrBlank())
                    tinyDB.putString("userProfileImage",it.user!!.profileImage)
                tinyDB.putString("userName",it.user!!.name)
                tinyDB.putString("userMobile",it.user!!.mobileNo)
                val intent= Intent(this, PropertiesActivity::class.java)
                startActivity(intent)
            }
            else{
                Toast.makeText(this,it?.error, Toast.LENGTH_SHORT).show()
            }
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


}