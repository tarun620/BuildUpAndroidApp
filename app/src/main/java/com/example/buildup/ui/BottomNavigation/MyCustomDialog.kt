package com.example.buildup.ui.BottomNavigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.edit
import androidx.fragment.app.DialogFragment
import com.example.api.BuildUpClient
import com.example.buildup.R
import com.example.buildup.databinding.LayoutAlertDialogBinding
import com.example.buildup.ui.LoginSignup.LoginSignupSelectorActivity


class MyCustomDialog: DialogFragment() {
    companion object {
        var PREFS_FILE_AUTH = "prefs_auth"
        var PREFS_KEY_TOKEN = "token"
    }
    private lateinit var sharedPrefrences: SharedPreferences


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        sharedPrefrences = this.requireActivity()
            .getSharedPreferences(PREFS_FILE_AUTH, Context.MODE_PRIVATE)

        getDialog()!!.getWindow()?.setBackgroundDrawableResource(R.drawable.round_corner);
        val view=inflater.inflate(R.layout.layout_alert_dialog, container, false)
        val btnYes=view.findViewById<TextView>(R.id.btn_yes)
        val btnNo=view.findViewById<TextView>(R.id.btn_no)

        btnYes.setOnClickListener {
//            Log.d("yes","yes is clicked")
            signout()
        }
        btnNo.setOnClickListener {
            dismiss()
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)

    }

    private fun signout(){
        BuildUpClient.authToken=null
        sharedPrefrences.edit {
            putString("token", null)
        }
//                sharedPrefrences.edit {
//                    remove("token")
//                }
//        startActivity(Intent(this, LoginSignupSelectorActivity::class.java))
        val i = Intent(activity, LoginSignupSelectorActivity::class.java)
        startActivity(i)
        (activity as Activity?)!!.overridePendingTransition(0, 0)

    }


}