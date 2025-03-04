package com.example.myapplication.retrofit

import android.service.autofill.UserData
import com.example.myapplication.model.LoginResult

// Helper class for API calls
object ApiHelper {
    fun login(
        email: String,
        password: String,
        callback: (Boolean, LoginResult?, String) -> Unit
    ) {
        val request = mapOf(
            "email" to email,
            "password" to password
        )
        ApiClient.handleResponse(ApiClient.api.login(request), callback)
    }

    fun register(
        email: String,
        password: String,
        name: String,
        region: String,
        callback: (Boolean, LoginResult?, String) -> Unit
    ) {
        val request = mapOf(
            "email" to email,
            "password" to password,
            "name" to name,
            "region" to region
        )
        ApiClient.handleResponse(ApiClient.api.register(request), callback)
    }
}
