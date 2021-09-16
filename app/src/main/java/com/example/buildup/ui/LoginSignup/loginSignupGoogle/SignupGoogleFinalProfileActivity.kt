package com.example.buildup.ui.LoginSignup.loginSignupGoogle

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.Patterns
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.buildup.AuthViewModel
import com.example.buildup.R
import com.example.buildup.databinding.ActivitySignupGoogleFinalProfileBinding
import com.example.buildup.databinding.AssetSuccesDialogBinding
import com.example.buildup.ui.Property.layouts.PropertiesActivity

class SignupGoogleFinalProfileActivity : AppCompatActivity() {
    private var _binding:ActivitySignupGoogleFinalProfileBinding?=null
    private lateinit var _bindingDialog : AssetSuccesDialogBinding
    lateinit var authViewModel: AuthViewModel
    private lateinit var dialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding= ActivitySignupGoogleFinalProfileBinding.inflate(layoutInflater)
        _bindingDialog= AssetSuccesDialogBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)

        setContentView(_binding?.root)

        if (getSupportActionBar() != null) {
            getSupportActionBar()?.hide();
        }

        val mobileNoGoogle:String?=intent.getStringExtra("mobileNoGoogle")
        val emailGoogle:String?=intent.getStringExtra("emailGoogle")

        _binding?.signInButton?.setOnClickListener {

            if(validationPassword()){
                authViewModel.completeProfileGoogle(emailGoogle!!,mobileNoGoogle!!,_binding?.passwordEditText.toString())

                authViewModel.respNew.observe({lifecycle}){
                    if(it?.token!=null && it.success!!){
                        Toast.makeText(this@SignupGoogleFinalProfileActivity,it.message, Toast.LENGTH_SHORT).show()
                        showDialog()
//                        val intent=Intent(this, PropertiesActivity::class.java)
//                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this@SignupGoogleFinalProfileActivity,it?.error,Toast.LENGTH_SHORT).show()
                        Log.d("errorCompleteProfileGoogle",it?.error.toString())
                    }
                }
            }
            else{
                Toast.makeText(this,"Please fill all required fields correctly",Toast.LENGTH_SHORT).show()
            }

        }
    }
    private fun validationPassword(): Boolean {
        _binding?.apply {

            passwordTextInputLayout.error=null
            confirmPasswordTextInputLayout.error=null

            if (passwordEditText.text.toString().isNullOrBlank() || passwordEditText.length() < 8) {
                passwordTextInputLayout.error = "Please input password of atleast 8 characters"
                passwordTextInputLayout.requestFocus()
                return false
            }
            else if (passwordConfirmEditText.text.toString().isNullOrBlank() || passwordConfirmEditText.text.toString() != passwordEditText.text.toString()) {
                confirmPasswordTextInputLayout.error = "Password doesn't match"
                confirmPasswordTextInputLayout.requestFocus()
                return false
            }
            else {
                return true
            }
        }
        return false
    }
    private fun showDialog() {
        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
        dialog.setContentView(_bindingDialog.root)
        _bindingDialog.titleText.text="Successfully\nLogged In"
        dialog.setCancelable(false)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.show()


        Handler().postDelayed({
            dialog.dismiss()
            val intent=Intent(this, PropertiesActivity::class.java)
            startActivity(intent)
        }, 3000)

    }

}