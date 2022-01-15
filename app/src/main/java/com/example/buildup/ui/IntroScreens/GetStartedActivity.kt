package com.example.buildup.ui.IntroScreens

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.example.api.BuildUpClient
import com.example.buildup.databinding.ActivityGetStartedBinding
import com.example.buildup.ui.HomeActivity
import com.example.buildup.ui.LoginSignup.LoginSignupSelectorActivity

class GetStartedActivity : AppCompatActivity() {
    companion object {
        var PREFS_FILE_AUTH = "prefs_auth"
        var PREFS_KEY_TOKEN = "token"
    }
    private lateinit var sharedPrefrences: SharedPreferences
    private var _binding: ActivityGetStartedBinding?=null
    private var doubleBackToExitPressedOnce = false
    private var token:String?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_get_started)
        _binding= ActivityGetStartedBinding.inflate(layoutInflater)
        sharedPrefrences = getSharedPreferences(PREFS_FILE_AUTH, Context.MODE_PRIVATE)

        setContentView(_binding?.root)

        //AUTO LOGIN USING SAVED INSTANCE OF LOGIN CREDENTIALS IN SHARED PREFERENCES

        token=sharedPrefrences.getString("token",null)
        if(token!=null){
            Log.d("token",token.toString())
            BuildUpClient.authToken=token
            val intent=Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        _binding?.btnGetStarted?.setOnClickListener {
            val intent= Intent(this, LoginSignupSelectorActivity::class.java)
            startActivity(intent)
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
}