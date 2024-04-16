package com.quanutrition.app.Utils

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit


var okHttpClient = OkHttpClient().newBuilder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

val retrofit = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create()) //For String Requests
        .baseUrl(NetworkManager.BASE_URL)
        .client(okHttpClient)
        .build()

/*

Structure to put in the network layer of the feature

object APICall {
    val retrofitService : APICall by lazy {
        retrofit.create(MarsApiService1::class.java)
    }
}*/
