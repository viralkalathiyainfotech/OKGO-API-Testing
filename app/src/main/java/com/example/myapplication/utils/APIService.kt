package com.example.myapplication.utils

import android.app.Activity
import android.util.Log
import com.example.myapplication.callback.DialogCallback
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.lzy.okgo.model.Response
import com.lzy.okgo.request.GetRequest
import com.lzy.okgo.request.PostRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject


object APIService {

    data class BaseResponse<T>(
        @SerializedName("success") val success: Boolean?,
        @SerializedName("message") val message: String?,
        @SerializedName("result") val result: T?
    )

    data class BaseListResponse<T>(
        @SerializedName("success") val success: Boolean?,
        @SerializedName("message") val message: String?,
        @SerializedName("result") val result: List<T?>
    )

    enum class RequestType { POST, GET }

    fun <T> performApiCall(
        activity: Activity,
        api: String,
        requestType: RequestType,
        params: Array<out Pair<String, String>>? = null,
        token: String? = null,
        clazz: Class<T>,
        isListResponse: Boolean = false,
        onResult: (Any?) -> Unit,
        onErrorResponse: (JSONObject) -> Unit
    ) {
        val onSuccess: (String) -> Unit = { responseBody ->
            val response = if (isListResponse) {
                parseListResponse(responseBody, clazz)
            } else {
                parseResponse(responseBody, clazz)
            }
            onResult(response)
        }

        val onError: (JSONObject) -> Unit = { jsonObject ->
            Log.e("TAG@@@", "Request Failed: ${jsonObject.optString("message", "Unknown error")}")
            onErrorResponse(jsonObject)
        }

        when (requestType) {
            RequestType.POST -> {
                val body = createRequestBody(*params.orEmpty())
                sendPostRequest(activity, PostRequest(api), body, onSuccess, onError)
            }

            RequestType.GET -> {
                sendGetRequest(activity, GetRequest(api), token!!, onSuccess, onError)
            }
        }
    }

    private fun sendPostRequest(
        activity: Activity,
        api: PostRequest<String>,
        body: RequestBody,
        onSuccess: (String) -> Unit,
        onError: (JSONObject) -> Unit
    ) {
        api.tag(this).upRequestBody(body).execute(object : DialogCallback<String?>(activity) {
            override fun onError(response: Response<String?>?) {
                Log.e("TAG@@@", "Error: ${response?.exception}")
            }

            override fun onSuccess(response: Response<String?>?) {

//                    Log.e("TAG@@@", "onSuccess: ${response!!.code()}")
//                    Log.e("TAG@@@", "onSuccess: ${response.body()}")
                if (response!!.code() in 200..299) {
                    response.body()?.let { responseBody ->
                        val jsonObject = JSONObject(responseBody)
                        if (jsonObject.optBoolean("success", false)) onSuccess(responseBody)
                        else onError(jsonObject)
                    } ?: Log.e("TAG@@@", "Response body is null")
                } else {
                    if (response.code() != 405){
                        val jsonObject = response.body()?.let { JSONObject(it) }
                        if (jsonObject != null) {
                            onError(jsonObject)
                        }
                    }
                    Log.e("TAG@@@", "${response.code()}")
                }

            }
        })
    }

    private fun sendGetRequest(
        activity: Activity,
        api: GetRequest<String>,
        token: String,
        onSuccess: (String) -> Unit,
        onError: (JSONObject) -> Unit
    ) {
        Log.i("TAG@@@", "sendGetRequest: ${"Bearer $token"}")
        api.tag(this).headers("Authorization", "Bearer $token")
            .execute(object : DialogCallback<String?>(activity) {
                override fun onError(response: Response<String?>?) {
                    Log.e("TAG@@@", "Error: ${response?.exception}")
                }

                override fun onSuccess(response: Response<String?>?) {

//                    Log.e("TAG@@@", "onSuccess: ${response!!.code()}")
//                    Log.e("TAG@@@", "onSuccess: ${response.body()}")
                    if (response!!.code() in 200..299) {
                        response.body()?.let { responseBody ->
                            val jsonObject = JSONObject(responseBody)
                            if (jsonObject.optBoolean("success", false)) onSuccess(responseBody)
                            else onError(jsonObject)
                        } ?: Log.e("TAG@@@", "Response body is null")
                    } else {
                        if (response.code() != 405){
                            val jsonObject = response.body()?.let { JSONObject(it) }
                            if (jsonObject != null) {
                                onError(jsonObject)
                            }
                        }
                        Log.e("TAG@@@", "${response.code()}")
                    }

                }
            })
    }

    private fun createRequestBody(vararg params: Pair<String, String>): RequestBody {
        return MultipartBody.Builder().setType(MultipartBody.FORM)
            .apply { params.forEach { (key, value) -> addFormDataPart(key, value) } }.build()
    }

    private fun <T> parseResponse(responseBody: String, clazz: Class<T>): BaseResponse<T>? {
        return Gson().fromJson(
            responseBody, TypeToken.getParameterized(BaseResponse::class.java, clazz).type
        )
    }

    private fun <T> parseListResponse(responseBody: String, clazz: Class<T>): BaseListResponse<T>? {
        return Gson().fromJson(
            responseBody, TypeToken.getParameterized(BaseListResponse::class.java, clazz).type
        )
    }

}
