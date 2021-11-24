package com.example.buildup.ui.IntroScreens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.example.buildup.R
import com.example.buildup.databinding.ActivityGetStartedBinding
import com.example.buildup.databinding.ActivityLoginSignupSelectorBinding
import com.example.buildup.ui.LoginSignupSelectorActivity

class GetStartedActivity : AppCompatActivity() {
    private var _binding: ActivityGetStartedBinding?=null
    private var doubleBackToExitPressedOnce = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_get_started)
        _binding= ActivityGetStartedBinding.inflate(layoutInflater)
        setContentView(_binding?.root)

        if (getSupportActionBar() != null) {
            getSupportActionBar()?.hide();
        }

        _binding?.btnGetStarted?.setOnClickListener {
            val intent= Intent(this,LoginSignupSelectorActivity::class.java)
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