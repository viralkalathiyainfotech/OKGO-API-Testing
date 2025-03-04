package com.example.myapplication.retrofit

// Base response wrapper
data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val result: T?
)
