package com.example.buildup

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.api.models.responsesAndData.appData.IntroData
import com.example.buildup.databinding.ActivitySplashBinding
import com.example.buildup.ui.IntroScreens.IntroScreen
import kotlinx.coroutines.delay


class MainActivity : AppCompatActivity() {

        companion object{
        const val PREFS_FILE_AUTH="prefs_auth"
        const val PREFS_KEY_TOKEN="token"
    }
    private var _binding: ActivitySplashBinding? = null
    private lateinit var authViewModel: AuthViewModel
    private var introAppDataHeadingList=ArrayList<String>()
    private var introAppDataTextList=ArrayList<String>()
    private lateinit var sharedPrefrences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashBinding.inflate(layoutInflater)
        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        sharedPrefrences = getSharedPreferences(PREFS_FILE_AUTH, Context.MODE_PRIVATE)

        setContentView(_binding?.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

//        getAppData()

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
//                    if (sharedPrefrences.getString(PREFS_KEY_TOKEN, null) != null) {
//                        val intent = Intent(this, IntroScreen::class.java)
//                        startActivity(intent)
//                    } else {
//
//                        startActivity(Intent(this, IntroScreen::class.java))
//
//                    }
                    authViewModel.getAppData("intro")
                    authViewModel.respGetAppData.observe({lifecycle}){
                        if(it?.success!!){
                            it.introData!!.forEach {
                                introAppDataHeadingList.add(it.heading)
                                introAppDataTextList.add(it.text)
                            }

                            val intent=Intent(this,IntroScreen::class.java)
                            intent.putExtra("introAppDataHeadingList",introAppDataHeadingList)
                            intent.putExtra("introAppDataTextList",introAppDataTextList)
                            startActivity(intent)
                        }
                        else
                            Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
                    }

                }, 400)

            }, 500)

        }, 600)


    }
    private fun getAppData(){
        authViewModel.getAppData("intro")
        authViewModel.respGetAppData.observe({lifecycle}){
            if(it?.success!!){
                it.introData!!.forEach {
                    introAppDataHeadingList.add(it.heading)
                    introAppDataTextList.add(it.text)
                }
                Log.d("appListHeadingAPI",introAppDataHeadingList.toString())
                Log.d("appListTextAPI",introAppDataTextList.toString())
            }
            else
                Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
        }
    }
}