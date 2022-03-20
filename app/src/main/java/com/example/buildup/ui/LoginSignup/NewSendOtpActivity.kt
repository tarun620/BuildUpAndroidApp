package com.example.buildup.ui.LoginSignup

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.ViewModelProvider
import com.example.buildup.AuthViewModel
import com.example.buildup.R
import com.example.buildup.TinyDB
import com.example.buildup.databinding.ActivityNewestOtpBinding
import com.example.buildup.databinding.ActivityWorkInProgressBinding
import com.example.buildup.ui.HomeActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task


class NewSendOtpActivity : AppCompatActivity() {
    companion object {
        var PREFS_FILE_AUTH = "prefs_auth"
        var PREFS_KEY_TOKEN = "token"
    }
    private lateinit var sharedPrefrences: SharedPreferences
    private var _binding: ActivityNewestOtpBinding?=null
    lateinit var authViewModel: AuthViewModel
    private var isLogin=false
    private var RC_SIGN_IN = 123
    private lateinit var tinyDB : TinyDB



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_newest_otp)

        _binding= ActivityNewestOtpBinding.inflate(layoutInflater)

        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)
        setContentView(_binding?.root)

        drawLayout()
        _binding!!.btnRetry.setOnClickListener {
//            drawLayout()
            finish();
            startActivity(intent);
        }

        sharedPrefrences = getSharedPreferences(PREFS_FILE_AUTH, Context.MODE_PRIVATE)
        tinyDB = TinyDB(this)


        isLogin=intent.getBooleanExtra("isLogin",false)


        if(isLogin)
            _binding?.googleBtn?.text="Login With Google"
        else
            _binding?.googleBtn?.text="Signup With Google"

        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestProfile()
                .requestEmail()
                .requestIdToken(resources.getString(R.string.googleAccountWebClientID))
                .build()

        // Build a GoogleSignInClient with the options specified by gso.
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        _binding?.apply {
            sendOTPButton.setOnClickListener {
                if(validationMobileNumber()){
                    if(isLogin){
                        authViewModel.login(mobileEditText.text.toString())
                    }
                    else{
                        authViewModel.signup(mobileEditText.text.toString())
                    }

                    authViewModel.resp.observe({lifecycle}){
                        if(it?.success!=null && it.success!!){
                            val intent: Intent = Intent(this@NewSendOtpActivity, NewOtpActivity::class.java)
                            intent.putExtra("mobileNo",mobileEditText.text.toString())
                            intent.putExtra("isLogin",isLogin)
                            startActivity(intent)
                        }
                        else{
                            if(it!!.error!="Network Failure")
                                Toast.makeText(this@NewSendOtpActivity,it.error,Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else{
                    Toast.makeText(this@NewSendOtpActivity,"Please fill all required fields correctly",
                        Toast.LENGTH_SHORT).show()
                }
            }

            googleBtn.setOnClickListener {
//                startActivity(Intent(this@NewSendOtpActivity,GoogleSignupActivity::class.java))
                mGoogleSignInClient.signOut()
                val signInIntent = mGoogleSignInClient.signInIntent
                startActivityForResult(signInIntent, RC_SIGN_IN)
            }
        }
    }


    private fun validationMobileNumber(): Boolean {
        _binding?.apply {

            mobileTextInputLayout.error=null

            if (mobileEditText.text.toString().isNullOrBlank() || mobileEditText.text.toString().length<10) {
                mobileTextInputLayout.error = "Please enter valid mobile number"
                mobileTextInputLayout.requestFocus()
                return false
            }
            else {
                return true
            }
        }
        return false
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
                        val intent = Intent(this@NewSendOtpActivity, HomeActivity::class.java)
                        startActivity(intent)
                    } else if (it?.token == null && it?.user == null && it?.success!!) {
                        val intent = Intent(this@NewSendOtpActivity, GoogleSignupActivity::class.java)
                        val bundle=Bundle()
                        bundle.putString("emailGoogle", account.email.toString())
                        bundle.putString("profileImageGoogle",account.photoUrl.toString())
                        intent.putExtras(bundle)
                        startActivity(intent)
                    } else {
                        if(it.error!="Network Failure")
                            Toast.makeText(this@NewSendOtpActivity,it.error,Toast.LENGTH_SHORT).show()                    }
                }
            }
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
            _binding!!.mainLayout.visibility= View.VISIBLE
            _binding!!.noInternetLayout.visibility= View.GONE
        } else {
            Log.d("internet","no internet")
            _binding!!.mainLayout.visibility= View.GONE
            _binding!!.noInternetLayout.visibility= View.VISIBLE

        }
    }
}