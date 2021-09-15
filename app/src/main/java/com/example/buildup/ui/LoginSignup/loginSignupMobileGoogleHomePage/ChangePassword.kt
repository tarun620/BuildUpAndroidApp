package com.example.buildup.ui.LoginSignup.loginSignupMobileGoogleHomePage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.buildup.databinding.ActivityChangePasswordBinding

private lateinit var _binding: ActivityChangePasswordBinding
class ChangePassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        _binding.resetPasswordButton.setOnClickListener {
            if(validationPassword()){
                    //TODO("Backend not yet implemented")
            }
            else{
                Toast.makeText(this,"Please fill all required fields correctly", Toast.LENGTH_SHORT).show()
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

}