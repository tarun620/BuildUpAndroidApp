package com.example.buildup.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.buildup.AuthViewModel
import com.example.buildup.TinyDB
import com.example.buildup.databinding.ActivityNewOtpBinding
import com.example.buildup.ui.Property.layouts.PropertiesActivity
import com.google.android.material.snackbar.Snackbar

class NewOtpActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityNewOtpBinding
    lateinit var authViewModel: AuthViewModel
    var START_MILLI_SECONDS = 60000L
    lateinit var countdown_timer: CountDownTimer
    var time_in_milli_seconds = 0L
    var mobileNoEditText: String?=null
    private var isLogin=false
    private lateinit var tinyDB : TinyDB


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_new_otp)
        _binding = ActivityNewOtpBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)
        setContentView(_binding.root)

        tinyDB = TinyDB(this)


        mobileNoEditText= intent.getStringExtra("mobileNo")
        isLogin=intent.getBooleanExtra("isLogin",false)

        _binding.apply {
            etOtp1.requestFocus()
            resendOTP.visibility= View.INVISIBLE
            timerText.visibility= View.VISIBLE

            OtpTimer()
            blockLetterOtpHandler()
            backBtn.setOnClickListener {
                finish()
            }
            verifyOTP.setOnClickListener {
                Log.d("verifyOTP","clicked1")
                val otp=getFullOtpFromBlockLetters()
                Log.d("verifyOTP","clicked2")
                if(isOtpValid(otp)){
                    Log.d("verifyOTP","clicked3")
                    verifyMobileNumberWithOTP(otp)
                    Log.d("verifyOTP","clicked4")
                }
            }

            resendOTP.setOnClickListener {
                sendOTP(mobileNoEditText!!)
                OtpTimer()
                resendOTP.visibility= View.INVISIBLE
                timerText.visibility= View.VISIBLE
            }
        }

    }

    private fun blockLetterOtpHandler() {
        class GenericKeyEvent internal constructor(private val currentView: EditText, private val previousView: EditText?) : View.OnKeyListener{
            override fun onKey(p0: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if(event!!.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && currentView.id != _binding.etOtp1.id && currentView.text.isEmpty()) {
                    //If current is empty then previous EditText's number will also be deleted
                    previousView!!.text = null
                    previousView.requestFocus()
                    return true
                }
                return false
            }

        }

        class GenericTextWatcher internal constructor(private val currentView: View, private val nextView: View?) :
            TextWatcher {
            override fun afterTextChanged(editable: Editable) { // TODO Auto-generated method stub
                val text = editable.toString()
                when (currentView.id) {
                    _binding.etOtp1.id -> if (text.length == 1) nextView!!.requestFocus()
                    _binding.etOtp2.id -> if (text.length == 1) nextView!!.requestFocus()
                    _binding.etOtp3.id -> if (text.length == 1) nextView!!.requestFocus()
//                    _binding.etOtp4.id -> if (text.length == 1) nextView!!.requestFocus()
//                    You can use EditText4 to hide the keyboard
                }
            }

            override fun beforeTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {
                // TODO Auto-generated method stub
            }

            override fun onTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {
                // TODO Auto-generated method stub
            }

        }

        _binding.apply {
            //GenericTextWatcher here works only for moving to next EditText when a number is entered
//first parameter is the current EditText and second parameter is next EditText
            etOtp1.addTextChangedListener(GenericTextWatcher(etOtp1, etOtp2))
            etOtp2.addTextChangedListener(GenericTextWatcher(etOtp2, etOtp3))
            etOtp3.addTextChangedListener(GenericTextWatcher(etOtp3, etOtp4))
            etOtp4.addTextChangedListener(GenericTextWatcher(etOtp4, null))

//GenericKeyEvent here works for deleting the element and to switch back to previous EditText
//first parameter is the current EditText and second parameter is previous EditText
            etOtp1.setOnKeyListener(GenericKeyEvent(etOtp1, null))
            etOtp2.setOnKeyListener(GenericKeyEvent(etOtp2, etOtp1))
            etOtp3.setOnKeyListener(GenericKeyEvent(etOtp3, etOtp2))
            etOtp4.setOnKeyListener(GenericKeyEvent(etOtp4, etOtp3))

        }
    }

    private fun getFullOtpFromBlockLetters() :String{
        _binding.apply {
            val otp=etOtp1.text.toString() + etOtp2.text.toString() + etOtp3.text.toString() + etOtp4.text.toString()
            return otp
        }

    }

    private fun isOtpValid(otp:String?):Boolean{
        if(otp.isNullOrBlank() || otp.length!=4){
            val snakbar= Snackbar.make(_binding.root,"Please enter OTP in valid format", Snackbar.LENGTH_SHORT)
            snakbar.show()
            return false
        }
        return true
    }
    private fun OtpTimer(){
        time_in_milli_seconds = 1 *10000L // 10 sec
        startTimer(time_in_milli_seconds)
    }

    private fun startTimer(time_in_seconds: Long) {
        countdown_timer = object : CountDownTimer(time_in_seconds, 1000) {
            override fun onFinish() {
                _binding.resendOTP.visibility= View.VISIBLE
                _binding.timerText.visibility= View.INVISIBLE
            }
            override fun onTick(p0: Long) {
                time_in_milli_seconds = p0
                updateTextUI()
            }
        }
        countdown_timer.start()

    }

    private fun updateTextUI() {
        val minute = (time_in_milli_seconds / 1000) / 60
        val seconds = (time_in_milli_seconds / 1000) % 60

        if(seconds.toString().length==1)
            _binding.timerText.text = "$minute:0$seconds"
        else
            _binding.timerText.text = "$minute:$seconds"
    }

    private fun verifyMobileNumberWithOTP(otp:String){
        Log.d("verifyOTP","clicked5")
        _binding?.apply {
            if(isLogin){
                authViewModel.verifyOTPLogin(mobileNoEditText!!,otp)

                authViewModel.respNewImage.observe({lifecycle}){
                    if(it?.success!! && it.token!=null && it.user!=null){
                        Toast.makeText(this@NewOtpActivity,it.message,Toast.LENGTH_SHORT).show()
                        if(!it.user!!.profileImage.isNullOrBlank())
                            tinyDB.putString("userProfileImage",it.user!!.profileImage)
                        tinyDB.putString("userName",it.user!!.name)
                        tinyDB.putString("userMobile",it.user!!.mobileNo)
                        val intent=Intent(this@NewOtpActivity,PropertiesActivity::class.java)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this@NewOtpActivity,it.error,Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else{
                authViewModel.verifyOtpSignup(mobileNoEditText!!,otp)

                authViewModel.resp.observe({lifecycle}){
                    if(it?.success!!){
                        Toast.makeText(this@NewOtpActivity,it.message,Toast.LENGTH_SHORT).show()
                        val intent=Intent(this@NewOtpActivity,NewSignupActivity::class.java)
                        intent.putExtra("mobileNo",mobileNoEditText)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this@NewOtpActivity,it.error,Toast.LENGTH_SHORT).show()
                        Log.d("errorOtp",it.error.toString())
                    }
                }
            }
        }
    }

    private fun sendOTP(mobileNo:String){

        if(isLogin){
            authViewModel.login(mobileNo)
        }
        else{
            authViewModel.signup(mobileNo)

        }
        authViewModel.resp.observe({lifecycle}){
            if(it?.success!!){
                Toast.makeText(this,"Otp Sent Successfully,Please check your inbox..",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
                Log.d("errorSignup",it.error.toString())
            }
        }
    }

}
