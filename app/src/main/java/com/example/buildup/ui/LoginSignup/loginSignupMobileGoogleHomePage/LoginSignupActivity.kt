package com.example.buildup.ui.LoginSignup.loginSignupMobileGoogleHomePage

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.View.*
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.edit
import androidx.lifecycle.ViewModelProvider
import com.example.buildup.AuthViewModel
import com.example.buildup.R
import com.example.buildup.databinding.ActivityLoginSignupBinding
import com.example.buildup.ui.Property.layouts.PropertiesActivity
import com.example.buildup.ui.LoginSignup.signupMobile.SignupActivity
import com.example.buildup.ui.LoginSignup.loginSignupGoogle.SignupGoogleActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task


class LoginSignupActivity : AppCompatActivity() {
    companion object {
        var PREFS_FILE_AUTH = "prefs_auth"
        var PREFS_KEY_TOKEN = "token"
    }

    //    private var _binding: ActivityLoginBinding?=null
    private var _binding: ActivityLoginSignupBinding? = null
    private var isLOGIN = false
    lateinit var authViewModel: AuthViewModel
    private var token: String? = null
    private lateinit var sharedPrefrences: SharedPreferences
    private var RC_SIGN_IN = 123
    private var isEmailValid = false
    private var isMobileNoValid = false
    private var isPasswordValid = false
    private var isConfirmPasswordValid = false
    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login)

//        _binding= ActivityLoginBinding.inflate(layoutInflater)
        _binding = ActivityLoginSignupBinding.inflate(layoutInflater)
        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        sharedPrefrences = getSharedPreferences(PREFS_FILE_AUTH, Context.MODE_PRIVATE)

        setContentView(_binding?.root)


        if (getSupportActionBar() != null) {
            getSupportActionBar()?.hide();
        }

        //AUTO LOGIN USING SAVED INSTANCE OF LOGIN CREDENTIALS IN SHARED PREFERENCES

//        token=sharedPrefrences.getString("token",null)
//        Log.d("TAGGettingSPOutside",token.toString())
//        if(token!=null){
//            Log.d("TAGGettingSPInside", token.toString())
//            BuildUpClient.authToken=token
//            val intent=Intent(this,PropertiesActivity::class.java)
//            startActivity(intent)
//        }


        // GOOGLE LOGIN/SIGNUP

        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        // Build a GoogleSignInClient with the options specified by gso.
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        //validate email , mobile number , password, confirm passowrd

//        _binding?.SignUpButton?.isEnabled=false
//        setValidationsSignUp()

        _binding?.apply {

            forgetPasswordTextview.visibility = View.GONE

            LoginButtonToggle.setOnClickListener {
                isLOGIN = true
                confirmPasswordTextInputLayout.visibility = View.GONE
                forgetPasswordTextview.visibility = VISIBLE
                SignUpButton.text = "Sign In"
                emailTextInputLayout.hint = "Mobile Number"
//                emailEditText.hint="Mobile Number"
                emailEditText.text?.clear()
                passwordEditText.text!!.clear()
                SignupButtonToggle.setBackgroundColor(resources.getColor(R.color.white))
                SignupButtonToggle.setTextColor(resources.getColor(R.color.black))
                LoginButtonToggle.setBackgroundColor(resources.getColor(R.color.orange_main))
                LoginButtonToggle.setTextColor(resources.getColor(R.color.white))

                var layoutparams: ConstraintLayout.LayoutParams =
                    forgetPasswordTextview.layoutParams as ConstraintLayout.LayoutParams
                layoutparams.topToBottom = passwordTextInputLayout.id
                layoutparams.endToEnd = passwordTextInputLayout.id
                forgetPasswordTextview.layoutParams = layoutparams
            }

            SignupButtonToggle.setOnClickListener {
                isLOGIN = false
                confirmPasswordTextInputLayout.visibility = VISIBLE
                forgetPasswordTextview.visibility = View.GONE
                SignUpButton.text = "Sign Up"
                SignupButtonToggle.setBackgroundColor(resources.getColor(R.color.orange_main))
                SignupButtonToggle.setTextColor(resources.getColor(R.color.white))
                LoginButtonToggle.setBackgroundColor(resources.getColor(R.color.white))
                LoginButtonToggle.setTextColor(resources.getColor(R.color.black))


//                emailEditText.hint="Email"
                emailTextInputLayout.hint = "Email"
                emailEditText.text?.clear()
                passwordEditText.text!!.clear()
            }

            SignUpButton.setOnClickListener {
//                loginActivityProgressBar.visibility=VISIBLE
//                val call=api.login(LoginData( mobileNoEditText.text.toString(),passwordEditText.text.toString()))
//                call.enqueue(object : retrofit2.Callback<LoginResponse>{
//                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
//                        Toast.makeText(this@LoginActivity,"Login Failure: ${t.message}",Toast.LENGTH_SHORT).show()
//
//                    }
//
//                    override fun onResponse(
//                        call: Call<LoginResponse>,
//                        response: Response<LoginResponse>
//                    ) {
//
//                        //PASSED
////                        if(!response.isSuccessful){
////                            val gson = Gson()
////                            val type = object : TypeToken<APIError>() {}.type
////                            var errorResponse: APIError = gson.fromJson(response.errorBody()!!.charStream(), type)
////                            Toast.makeText(this@LoginActivity,errorResponse.error,Toast.LENGTH_SHORT).show()
////                        }
//
//                        //PASSED
////                        if (!response.isSuccessful) {
////                            val error = StringBuilder()
////                            try {
////                                var bufferedReader: BufferedReader? = null
////                                if (response.errorBody() != null) {
////                                    bufferedReader = BufferedReader(InputStreamReader(
////                                            response.errorBody()!!.byteStream()))
////                                    var eLine: String? = null
////                                    while (bufferedReader.readLine().also({ eLine = it }) != null) {
////                                        error.append(eLine)
////                                    }
////                                    bufferedReader.close()
////                                }
////                            } catch (e: Exception) {
//////                                error.append(e.message)
////                            }
////                            Log.e("Error", error.toString())
////                            val jObjError = JSONObject(error.toString())
////                            var errorMessage=jObjError.getString("error")
////                            Toast.makeText(this@LoginActivity,errorMessage,Toast.LENGTH_SHORT).show()
////                        }
//
//
//
//
//                        //PASSED
////                        if (response.isSuccessful) {
////                            // Do your success stuff...
////                        } else {
////                            try {
////                                val jObjError = JSONObject(response.errorBody()!!.string())
////                                var errorMessage=jObjError.getJSONObject("error").getString("message")
////                                errorMessage= errorMessage.substringAfter("Value")
////                                errorMessage=errorMessage?.substringBefore(" at")
////                                Toast.makeText(this@LoginActivity, errorMessage, Toast.LENGTH_LONG).show()
////                                Log.d("LoginActivity",errorMessage)
////                            } catch (e: Exception) {
////                                var errorMessage=e.message
////                                errorMessage=errorMessage?.substringAfter("Value ")
////                                errorMessage=errorMessage?.substringBefore(" at")
////                                Toast.makeText(this@LoginActivity, errorMessage, Toast.LENGTH_LONG).show()
////                                Log.d("LoginActivity",e.message.toString())
////
////                            }
////                        }
//                    }
//                })


//authviewmodel way-initial way

                if (validations()) {
                    if (isLOGIN) {  // Login request ( we are in login toggle button)
                        authViewModel.login(
                            emailEditText.text.toString(),
                            passwordEditText.text.toString()
                        )

                        authViewModel.respNewImage.observe({ lifecycle }) {
                            if (it?.token != null && it.success!!) {
                                Log.d("TAGTokenOutside", it.token.toString())
                                it.token?.let { t ->
                                    sharedPrefrences.edit {
//                                putString(PREFS_KEY_TOKEN,t)
                                        putString("token", t)
                                        Log.d(
                                            "TAGSettingSP",
                                            sharedPrefrences.getString("token", null).toString()
                                        )
//                                Log.d("TAGSettingSP", PREFS_KEY_TOKEN)
                                    }
                                } ?: run {                       //     ?: IS CALLED ELVIS OPERATOR
                                    sharedPrefrences.edit {
                                        remove("token")
                                    }
                                }

                                Log.d("TAGSuccess", it.message.toString())
//                        loginActivityProgressBar.visibility= INVISIBLE
                                Toast.makeText(
                                    this@LoginSignupActivity,
                                    it.message,
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                val intent =
                                    Intent(this@LoginSignupActivity, PropertiesActivity::class.java)
                                startActivity(intent)
                            } else {
//                        loginActivityProgressBar.visibility= INVISIBLE
                                Toast.makeText(
                                    this@LoginSignupActivity,
                                    it?.error,
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                Log.d("TAGError", it?.error.toString())
                            }
                        }
                    } else {  // Signup Request using Mobile Number (we are in Signup Toggle Button)
                        val intent = Intent(this@LoginSignupActivity, SignupActivity::class.java)
                        intent.putExtra("email", emailEditText.text.toString())
                        intent.putExtra("password", passwordEditText.text.toString())
                        startActivity(intent)
                    }
                }

            }

            // LOGIN-SIGNUP using GOOGLE
            googleLoginButton.setOnClickListener {
                val signInIntent = mGoogleSignInClient.signInIntent
                startActivityForResult(signInIntent, RC_SIGN_IN)
            }
        }
    }

//    private fun setValidationsLogin(){
//    }
//    private fun setValidationsSignUp(){
//        _binding?.apply {
//            if(emailEditText.text.toString().isNullOrBlank()){
//                emailEditText.error="Email Cannot be Blank"
//                isEmailValid = false;
//            }
//
//            else if (!Patterns.EMAIL_ADDRESS.matcher(emailEditText.text.toString()).matches()) {
//                emailEditText.error="Invalid Email"
//                isEmailValid = false;
//            }
//            else  {
//                isEmailValid = true;
//            }
//
//            if (passwordEditText.text.toString().isEmpty()) {
//                passwordEditText.error="Password Cannot be Empty"
//                isPasswordValid = false;
//            } else  {
//                isPasswordValid = true;
//            }
//
//            if (isEmailValid && isPasswordValid) {
////                Toast.makeText(getApplicationContext(), "Successfully", Toast.LENGTH_SHORT).show();
//                SignUpButton.isEnabled=true
//            }
//        }
//
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account =
                completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            Log.d("googleSignin", "I am in try block..")
            updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("googleSignin", "signInResult:failed code=" + e.statusCode)
            Log.d("googleSignin", "I am in catch block..")
            updateUI(null)
        }
    }

    private fun updateUI(account: GoogleSignInAccount?) {
        if (account == null) {
//            _binding?.apply {
//                signInButton?.visibility = View.VISIBLE
//
//            }
            Toast.makeText(
                this,
                "Something Went Wrong. Please try again later.",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            _binding?.apply {
//                signInButton?.visibility = View.GONE
//                Log.d("test123","testLog")
                authViewModel.loginSignupGoogle(
                    account.displayName.toString(),
                    account.email.toString(),
                    account.photoUrl.toString()
                )

                authViewModel.respNewImageGoogle.observe({ lifecycle }) {
                    if (it?.token != null && it.success!!) {
                        Toast.makeText(this@LoginSignupActivity, it.message, Toast.LENGTH_SHORT)
                            .show()
                        val intent =
                            Intent(this@LoginSignupActivity, PropertiesActivity::class.java)
                        startActivity(intent)
                    } else if (it?.token == null && it?.user == null && it?.success!!) {
                        Toast.makeText(this@LoginSignupActivity, it.message, Toast.LENGTH_SHORT)
                            .show()
                        val intent =
                            Intent(this@LoginSignupActivity, SignupGoogleActivity::class.java)
                        intent.putExtra("emailgoogle", account.email.toString())
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@LoginSignupActivity, it.error, Toast.LENGTH_SHORT)
                            .show()
                        Log.d("errorGoogleLogin", it.error.toString())
                    }
                }
            }
        }
    }

    private fun validations(): Boolean {
        _binding?.apply {
            if (emailEditText.text.toString() == null || emailEditText.text.toString() == ""
            ) {
                emailTextInputLayout.error = "Please fill your email properly"
                return false
            } else if (passwordEditText.text.toString() === null || passwordEditText.text.toString() == "" || passwordEditText.length() < 6) {
                passwordTextInputLayout.error = "Please input password of atleast 5 characters"
                return false
            } else if (confirmPasswordTextInputLayout.visibility == View.VISIBLE) {
                if (passwordConfirmEditText.text.toString() == null || passwordConfirmEditText.text.toString() == "" || passwordConfirmEditText.text.toString() != passwordEditText.text.toString()) {
                    confirmPasswordTextInputLayout.error = "Password doesn't match"
                    return false
                }
            } else {
                return true
            }
        }
        return false
    }



}