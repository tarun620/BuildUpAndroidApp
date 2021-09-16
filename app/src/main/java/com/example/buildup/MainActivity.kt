package com.example.buildup

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import androidx.lifecycle.ViewModelProvider
import com.example.buildup.databinding.ActivitySplashBinding
import com.example.buildup.ui.LoginSignup.loginSignupMobileGoogleHomePage.LoginSignupActivity
import com.example.buildup.ui.LoginSignup.loginSignupMobileGoogleHomePage.LoginSignupActivity.Companion.PREFS_FILE_AUTH
import com.example.buildup.ui.LoginSignup.loginSignupMobileGoogleHomePage.LoginSignupActivity.Companion.PREFS_KEY_TOKEN
import com.example.buildup.ui.LoginSignup.signupMobile.SignupActivity
import com.example.buildup.ui.Property.layouts.PropertiesActivity

class MainActivity : AppCompatActivity() {

    //    companion object{
//        const val PREFS_FILE_AUTH="prefs_auth"
//        const val PREFS_KEY_TOKEN="token"
//    }
    private var _binding: ActivitySplashBinding? = null
    private lateinit var authViewModel: AuthViewModel

    private lateinit var sharedPrefrences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashBinding.inflate(layoutInflater)
        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        sharedPrefrences = getSharedPreferences(PREFS_FILE_AUTH, Context.MODE_PRIVATE)

        setContentView(_binding?.root)



        val zoomOut = AnimationUtils.loadAnimation(applicationContext, R.anim.zoom_out)

        val zoomIn = AnimationUtils.loadAnimation(applicationContext, R.anim.zoom_in)
        val bottomUp = AnimationUtils.loadAnimation(applicationContext, R.anim.bottom_anim)
        val topDown = AnimationUtils.loadAnimation(applicationContext, R.anim.top_anim)

        _binding!!.homeIcon.startAnimation(zoomOut)
        _binding!!.homeIcon.visibility = View.VISIBLE

        Handler().postDelayed({
            _binding!!.titleText.startAnimation(bottomUp)
            _binding!!.titleText.visibility = View.VISIBLE
            Handler().postDelayed({
                _binding!!.titleText.startAnimation(topDown)
                _binding!!.titleText.visibility = View.GONE
                _binding!!.homeIcon.startAnimation(zoomIn)
                _binding!!.homeIcon.visibility = View.GONE
                Handler().postDelayed({
                    if (sharedPrefrences.getString(PREFS_KEY_TOKEN, null) != null) {
                        val intent = Intent(this, LoginSignupActivity::class.java)
                        startActivity(intent)
                    } else {

                        startActivity(Intent(this, LoginSignupActivity::class.java))

                    }
                }, 400)

            }, 500)

        }, 600)


    }
}