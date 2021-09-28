package com.example.buildup.ui.LoginSignup.loginSignupMobileGoogleHomePage

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.buildup.R
import com.example.buildup.databinding.ActivityOtpNewBinding
import com.google.android.material.snackbar.Snackbar


class OtpNewActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityOtpNewBinding
    var START_MILLI_SECONDS = 60000L
    lateinit var countdown_timer: CountDownTimer
    var time_in_milli_seconds = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityOtpNewBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        if (getSupportActionBar() != null) {
            getSupportActionBar()?.hide()
        }

        _binding.etOtp1.requestFocus()
        _binding.resendOTP.visibility= View.INVISIBLE
        _binding.timerText.visibility= View.VISIBLE

        OtpTimer()
        blockLetterOtpHandler()
        _binding.backBtn.setOnClickListener {
            finish()
        }
        _binding.verifyOTP.setOnClickListener {
            val otp=getFullOtpFromBlockLetters()
            if(isOtpValid(otp!!)){
                //            TODO("functionality not implemented yet in backend")
                startActivity(Intent(this,ChangePasswordActivity::class.java))
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

        class GenericTextWatcher internal constructor(private val currentView: View, private val nextView: View?) : TextWatcher {
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

    private fun getFullOtpFromBlockLetters() :String?{
        _binding.apply {
            val otp=etOtp1.text.toString() + etOtp2.text.toString() + etOtp3.text.toString() + etOtp4.text.toString()
            return otp
        }

    }

    private fun isOtpValid(otp:String?):Boolean{
        if(otp.isNullOrBlank() || otp.length!=4){
            val snakbar=Snackbar.make(_binding.root,"Please enter OTP in valid format",Snackbar.LENGTH_SHORT)
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
}