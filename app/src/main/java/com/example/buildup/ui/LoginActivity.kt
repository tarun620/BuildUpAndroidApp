package com.example.buildup.ui

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.api.BuildUpClient
import com.example.api.models.entities.LoginData
import com.example.api.models.responses.LoginResponse
import com.example.buildup.AuthViewModel
import com.example.buildup.databinding.ActivityLoginBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Converter
import retrofit2.Response
import java.io.IOException


class LoginActivity : AppCompatActivity() {
    val api= BuildUpClient.api
    private var _binding:ActivityLoginBinding?=null
    lateinit var authViewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login)

        _binding= ActivityLoginBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)

        setContentView(_binding?.root)

        _binding?.apply {
            LoginButton.setOnClickListener {
                loginActivityProgressBar.visibility=VISIBLE
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
//
//                        //FAILED
////                        if(!response.isSuccessful){
////                            val errorConverter: Converter<ResponseBody?, APIError> = ClientManager.getRestClient().getRetrofitInstance().responseConverter(BasicResponse::class.java, arrayOfNulls<Annotation>(0))
////                            val error: APIError? = errorConverter.convert(response.errorBody())
////                            //DO ERROR HANDLING HERE
////                            //DO ERROR HANDLING HERE
////                            return
////                        }
//
//
//
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

                authViewModel.login(mobileNoEditText.text.toString(),passwordEditText.text.toString())

                authViewModel.respNewImage.observe({lifecycle}){
                    if(it?.token!=null && it.success!!){
                        Log.d("TAG",it.message.toString())
                        loginActivityProgressBar.visibility= INVISIBLE
                        Toast.makeText(this@LoginActivity,it.message,Toast.LENGTH_SHORT).show()
                        val intent= Intent(this@LoginActivity,LoggedInActivity::class.java)
                        startActivity(intent)
                    }
                    else{
                        loginActivityProgressBar.visibility= INVISIBLE
                        Toast.makeText(this@LoginActivity,it?.error,Toast.LENGTH_SHORT).show()
                        Log.d("errorLogin",it?.error.toString())
                    }
                }

            }
        }
    }
}