package com.example.buildup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.buildup.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

//    companion object{
//        const val PREFS_FILE_AUTH="prefs_auth"
//        const val PREFS_KEY_TOKEN="token"
//    }
    private var _binding:ActivityMainBinding?=null
    private lateinit var authViewModel: AuthViewModel
//    private lateinit var sharedPrefrences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityMainBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)
//        sharedPrefrences=getSharedPreferences(PREFS_FILE_AUTH, Context.MODE_PRIVATE)

        setContentView(_binding?.root)

        //shared prefrences handling

//        sharedPrefrences.getString(PREFS_KEY_TOKEN,null)?.let{
//            val intent=Intent(this,PropertiesActivity::class.java)
//            startActivity(intent)
//
//        }


    }
}