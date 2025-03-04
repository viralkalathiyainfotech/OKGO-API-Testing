package com.example.myapplication.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    private const val BASE_URL = "https://music.kalathiyainfotechapi.in/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .connectTimeout(30, TimeUnit.SECONDS)
                .build()
        )
        .build()

    val api: ApiService = retrofit.create(ApiService::class.java)

    fun <T> handleResponse(
        call: Call<ApiResponse<T>>,
        callback: (Boolean, T?, String) -> Unit
    ) {
        call.enqueue(object : Callback<ApiResponse<T>> {
            override fun onResponse(
                call: Call<ApiResponse<T>>,
                response: Response<ApiResponse<T>>
            ) {
                response.body()?.let { apiResponse ->
                    callback(apiResponse.success, apiResponse.result, apiResponse.message)
                } ?: callback(false, null, "Response body is null")
            }

            override fun onFailure(call: Call<ApiResponse<T>>, t: Throwable) {
                callback(false, null, t.message ?: "Unknown error")
            }
        })
    }
}
