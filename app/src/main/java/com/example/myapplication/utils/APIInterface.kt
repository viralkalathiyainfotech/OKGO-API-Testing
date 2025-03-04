package com.example.myapplication.utils

import android.app.Activity
import com.example.myapplication.model.Language
import com.example.myapplication.model.LoginResult
import com.example.myapplication.model.RegisterResult
import com.example.myapplication.utils.APIService.performApiCall
import org.json.JSONObject

object APIInterface {


    fun registerResponse(
        activity: Activity,
        email: String, password: String, name: String,
        onResult: (Any?) -> Unit,
        onErrorResponse: (JSONObject) -> Unit
    ) {
        performApiCall(
            activity = activity,
            api = Constants.REGISTERAPI,
            requestType = APIService.RequestType.POST,
            params = arrayOf("email" to email, "password" to password, "name" to name),
            clazz = RegisterResult::class.java,
            onResult = onResult,
            onErrorResponse = onErrorResponse
        )
    }

    fun languageResponse(
        activity: Activity,
        token: String = "",
        onResult: (Any?) -> Unit,
        onErrorResponse: (JSONObject) -> Unit
    ) {

        performApiCall(
            activity = activity,
            api = Constants.GetLanguageAPI,
            requestType = APIService.RequestType.GET,
            token = token,
            clazz = Language::class.java,
            isListResponse = true,
            onResult = onResult,
            onErrorResponse = onErrorResponse
        )
    }

    fun loginResponse(
        activity: Activity,
        email: String, password: String,
        onResult: (Any?) -> Unit,
        onErrorResponse: (JSONObject) -> Unit
    ) {
        performApiCall(
            activity = activity,
            api = Constants.LOGINAPI,
            requestType = APIService.RequestType.POST,
            params = arrayOf("email" to email, "password" to password),
            clazz = LoginResult::class.java,
            onResult = onResult,
            onErrorResponse = onErrorResponse
        )
    }
}