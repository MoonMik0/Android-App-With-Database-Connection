package com.ozlemelmali.kotlintriesv7

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8000/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // login username and password can be seen in logcat
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor) // login username and password can be seen in logcat
        .build()

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // transaction to JSON
            .client(client) // for logging
            .build()
            .create(ApiService::class.java)
    }
}
