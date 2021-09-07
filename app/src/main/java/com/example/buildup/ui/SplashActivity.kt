package com.example.buildup.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.example.buildup.MainActivity
import com.example.buildup.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        val zoomOut = AnimationUtils.loadAnimation(applicationContext, R.anim.zoom_out)
        val homeIcon = findViewById<ImageView>(R.id.homeIcon)

        val zoomIn = AnimationUtils.loadAnimation(applicationContext, R.anim.zoom_in)
        val titleText = findViewById<TextView>(R.id.titleText)
        val bottomUp = AnimationUtils.loadAnimation(applicationContext, R.anim.bottom_anim)
        val topDown = AnimationUtils.loadAnimation(applicationContext, R.anim.top_anim)

        homeIcon.startAnimation(zoomOut)
        homeIcon.visibility = View.VISIBLE

        Handler().postDelayed({
            titleText.startAnimation(bottomUp)
            titleText.visibility = View.VISIBLE
            Handler().postDelayed({
                titleText.startAnimation(topDown)
                titleText.visibility = View.GONE
                homeIcon.startAnimation(zoomIn)
                homeIcon.visibility = View.GONE
                Handler().postDelayed({
                    startActivity(Intent(this, MainActivity::class.java))

                }, 400)

            }, 500)

        }, 600)


//        homeIcon.startAnimation(zoomIn)

    }
}