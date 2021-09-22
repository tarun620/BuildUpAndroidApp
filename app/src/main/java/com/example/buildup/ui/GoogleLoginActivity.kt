package com.example.buildup.ui

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.buildup.AuthViewModel
import com.example.buildup.databinding.ActivityGoogleLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task


class GoogleLoginActivity : AppCompatActivity() {

    private var RC_SIGN_IN = 123
    private var _binding: ActivityGoogleLoginBinding? = null
    lateinit var authViewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_google_login)


        _binding = ActivityGoogleLoginBinding.inflate(layoutInflater)
        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        setContentView(_binding?.root)

//        Log.d("google123","entered")
        val gso =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build()

        // Build a GoogleSignInClient with the options specified by gso.
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        _binding?.signInButton?.setSize(SignInButton.SIZE_STANDARD);

        _binding?.apply {
            signInButton.setOnClickListener {
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
            updateUI(null)
        }
    }

    private fun updateUI(account: GoogleSignInAccount??) {
        if (account == null) {
            _binding?.apply {
                signInButton?.visibility = View.VISIBLE

            }
        } else {
            _binding?.apply {
                signInButton?.visibility = View.GONE
                authViewModel.loginSignupGoogle(
                        account.displayName.toString(),
                        account.email.toString(),
                        account.photoUrl.toString()
                )

                authViewModel.respNewImageGoogle.observe({lifecycle}){
                    if(it?.token!=null && it.success!!){
                        Toast.makeText(this@GoogleLoginActivity,it.message, Toast.LENGTH_SHORT).show()
                        val intent=Intent(this@GoogleLoginActivity,LoggedInActivity::class.java)
                        startActivity(intent)
                    }
                    else if(it?.token==null && it?.user==null && it?.success!!){
                        Toast.makeText(this@GoogleLoginActivity,it.message,Toast.LENGTH_SHORT).show()
                        val intent=Intent(this@GoogleLoginActivity,SignupGoogleActivity::class.java)
                        intent.putExtra("emailgooogle",account.email.toString())
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this@GoogleLoginActivity,it.error,Toast.LENGTH_SHORT).show()
                        Log.d("errorGoogleLogin",it.error.toString())
                    }
                }
            }
        }
    }
}