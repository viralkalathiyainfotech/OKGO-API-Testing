package com.example.myapplication.retrofit

import android.service.autofill.UserData
import com.example.myapplication.model.LoginResult
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

// API Interface
interface ApiService {
    @POST("api/login")
    fun login(@Body request: Map<String, String>): Call<ApiResponse<LoginResult>>

    @POST("register")
    fun register(@Body request: Map<String, String>): Call<ApiResponse<LoginResult>>
}