package com.example.api

import com.example.api.services.BuildUpAPI
import com.example.api.services.BuildUpAuthAPI
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object BuildUpClient {

    var authToken:String?=null

    private val authInterceptor = Interceptor { chain ->
        var req = chain.request()
        authToken?.let {
            req = req.newBuilder()
                    .header("Authorization", "Token $it")
                    .build()
        }
        chain.proceed(req)
    }

    val okHttpBuilder = OkHttpClient.Builder()
            .readTimeout(5, TimeUnit.SECONDS)
            .connectTimeout(5, TimeUnit.SECONDS)

    val retrofitBuilder=Retrofit.Builder()
        .baseUrl("https://api.buildup.org.in/")
//        .baseUrl(" http://192.168.0.103:5000/")
//        .baseUrl("http://192.168.157.11:5000/")
        .addConverterFactory(MoshiConverterFactory.create())

    val retrofit= retrofitBuilder.client(okHttpBuilder.build()).build()
//    val retrofit= retrofitBuilder.build()


    val api= retrofit.create(BuildUpAPI::class.java)

//    val api= retrofitBuilder
////            .client(okHttpBuilder.build())
//            .build()
//            .create(BuildUpAPI::class.java)

    val authApi= retrofitBuilder
            .client(okHttpBuilder.addInterceptor(authInterceptor).build())
            .build()
            .create(BuildUpAuthAPI::class.java)
}