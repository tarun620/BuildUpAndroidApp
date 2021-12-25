package com.example.buildup.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.edit
import androidx.lifecycle.ViewModelProvider
import com.example.buildup.AuthViewModel
import com.example.buildup.R
import com.example.buildup.databinding.ActivityNewLoginBinding
import com.example.buildup.ui.LoginSignup.loginSignupGoogle.SignupGoogleActivity
import com.example.buildup.ui.LoginSignup.loginSignupMobileGoogleHomePage.ForgetPasswordActivity
import com.example.buildup.ui.LoginSignup.loginSignupMobileGoogleHomePage.LoginSignupActivity
import com.example.buildup.ui.Property.layouts.PropertiesActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class NewLoginActivity : AppCompatActivity() {
    companion object {
        var PREFS_FILE_AUTH = "prefs_auth"
        var PREFS_KEY_TOKEN = "token"
    }
    private lateinit var sharedPrefrences: SharedPreferences
    private var RC_SIGN_IN = 123
    private var _binding: ActivityNewLoginBinding?=null
    lateinit var authViewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_new_login)

        _binding= ActivityNewLoginBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)
        sharedPrefrences = getSharedPreferences(LoginSignupActivity.PREFS_FILE_AUTH, Context.MODE_PRIVATE)

        setContentView(_binding?.root)


        //AUTO LOGIN USING SAVED INSTANCE OF LOGIN CREDENTIALS IN SHARED PREFERENCES

//        token=sharedPrefrences.getString("token",null)
//        if(token!=null){
//            BuildUpClient.authToken=token
//            val intent=Intent(this,PropertiesActivity::class.java)
//            startActivity(intent)
//        }

        // GOOGLE LOGIN/SIGNUP

        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestProfile()
                .requestEmail()
                .requestIdToken(resources.getString(R.string.googleAccountWebClientID))
                .build()

        // Build a GoogleSignInClient with the options specified by gso.
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        _binding?.apply {
            loginButton.setOnClickListener {
                if (validationSignIn()) {
                    authViewModel.login(
                        mobileEditText.text.toString()
                    )

                    authViewModel.respNewImage.observe({ lifecycle }) {
                        if(it?.token != null && it.success!!) {
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
                                Intent(this@NewLoginActivity, PropertiesActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(
                                this@NewLoginActivity,
                                it?.error,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    Toast.makeText(
                        this@NewLoginActivity,
                        "Please fill all required fields correctly",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            googleLoginButton.setOnClickListener {
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
                        Toast.makeText(this@NewLoginActivity, it.message, Toast.LENGTH_SHORT)
                            .show()
                        val intent =
                            Intent(this@NewLoginActivity, PropertiesActivity::class.java)
                        startActivity(intent)
                    } else if (it?.token == null && it?.user == null && it?.success!!) {
                        Toast.makeText(this@NewLoginActivity, it.message, Toast.LENGTH_SHORT)
                            .show()
                        val intent =
                            Intent(this@NewLoginActivity, SignupGoogleActivity::class.java)
                        intent.putExtra("emailgoogle", account.email.toString())
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@NewLoginActivity, it.error, Toast.LENGTH_SHORT)
                            .show()
                        Log.d("errorGoogleLogin", it.error.toString())
                    }
                }
            }
        }
    }

    private fun validationSignIn(): Boolean {

        _binding?.apply {

            mobileTextInputLayout.error = null
            passwordTextInputLayout.error = null

            mobileTextInputLayout.isErrorEnabled=false
            passwordTextInputLayout.isErrorEnabled=false

            if (mobileEditText.text.toString()
                    .isNullOrBlank() || mobileEditText.text.toString().length < 10
            ) {
                mobileTextInputLayout.error = "Please enter valid mobile number"
                mobileTextInputLayout.requestFocus()
                return false
            } else if (passwordEditText.text.toString()
                    .isNullOrBlank() || passwordEditText.length() < 8
            ) {
                passwordTextInputLayout.error = "Please input password of atleast 8 characters"
                passwordTextInputLayout.requestFocus()
                return false
            } else {
                return true
            }
        }
        return false
    }
}