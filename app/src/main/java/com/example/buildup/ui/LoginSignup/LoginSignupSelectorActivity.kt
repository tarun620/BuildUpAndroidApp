package com.example.buildup.ui.LoginSignup

import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.buildup.AuthViewModel
import com.example.buildup.databinding.ActivityLoginSignupSelectorBinding

class LoginSignupSelectorActivity : AppCompatActivity() {

    private var _binding: ActivityLoginSignupSelectorBinding?=null
    lateinit var authViewModel: AuthViewModel
    private var doubleBackToExitPressedOnce = false
    private var isLogin=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login_signup_selector)
        _binding= ActivityLoginSignupSelectorBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)

        setContentView(_binding?.root)

        drawLayout()
        _binding?.btnRetry?.setOnClickListener {
//            drawLayout()
            finish();
            startActivity(intent);
        }

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        authViewModel.getAppData("registration")
        authViewModel.respGetAppData.observe({lifecycle}){
            if(it?.success!!){
                _binding?.tvDescription?.text=it.registrationData!!.text
            }
        }
        _binding?.apply {
            LoginButton.setOnClickListener {
                isLogin=true
                val intent=Intent(this@LoginSignupSelectorActivity, NewSendOtpActivity::class.java)
                intent.putExtra("isLogin", isLogin)
                startActivity(intent)
            }

            SignupButton.setOnClickListener {
                isLogin=false
                val intent=Intent(this@LoginSignupSelectorActivity, NewSendOtpActivity::class.java)
                intent.putExtra("isLogin", isLogin)
                startActivity(intent)
            }
        }
    }
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
//            return  //closes the current activity only
            this.finishAffinity();   //closes the entire application
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
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