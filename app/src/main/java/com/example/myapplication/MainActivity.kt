package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.callback.DialogCallback
import com.example.myapplication.callback.StringDialogCallback
import com.example.myapplication.model.Language
import com.example.myapplication.model.LoginResult
import com.example.myapplication.model.LzyResponse
import com.example.myapplication.model.Quote
import com.example.myapplication.utils.APIInterface.languageResponse
import com.example.myapplication.utils.APIInterface.loginResponse
import com.example.myapplication.utils.APIService
import com.example.myapplication.utils.ServerApi
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        loginResponse(this, "priya12@gmail.com", "password", { baseResponse ->
            if (baseResponse is APIService.BaseResponse<*>) {
                val loginResult = baseResponse.result as? LoginResult
                loginResult?.let {
                    Log.d("TAG@@@", "onCreate: ${it.name!!}")
                    // Fetch language data using the accessToken
                    fetchLanguages(it.accessToken!!)
                }
            }
            Log.d("TAG@@@", "onCreate: $baseResponse")
        }, { onError ->
            Log.d("TAG@@@", "onError: $onError")
        })


//        RegisterResponse()
//        loginResponse(this, "priya12@gmail.com", "password", { baseResponse ->
//            if (baseResponse is APIService.BaseResponse<*>) {
//                val loginResult = baseResponse.result as? LoginResult
//                loginResult?.let {
//                    Log.d("TAG@@@", "onCreate: ${it.name!!}")
//                    languageResponse(this, it.accessToken, { response ->
//                        val languageResponse = response as? APIService.BaseListResponse<Language>
//                        languageResponse?.result?.let { languages ->
//
//                            for (result in languages) {
//                                Log.d("TAG@@@", "onCreate: ${result!!.name}")
//                            }
//
//                            // Use the list of languages, e.g., display them in a RecyclerView
////                                    languages.forEach { language ->
////                                        Log.d("TAG@@@", "Language: ${language?.name}") // Replace 'name' with your actual property
////                                    }
//                        } ?: run {
//                            Log.e("TAG@@@", "No languages found or response is null.")
//                        }
//
//                    }, { onError -> Log.d("TAG@@@", "onError: $onError") })
//                }
//            }
//            Log.d("TAG@@@", "onCreate: $baseResponse")
//        }, { onError -> Log.d("TAG@@@", "onError: $onError") })


//        JsonArray()
//        response()
//        JsonObject()
//        retrofitRequest()
    }


    private fun fetchLanguages(token: String) {
        languageResponse(this, token, { response ->
            val languageResponse = response as? APIService.BaseListResponse<Language>
            languageResponse?.result?.let { languages ->
                languages.forEach { language ->
                    Log.d("TAG@@@", "onCreate: ${language?.name}")
                }
            } ?: run {
                Log.e("TAG@@@", "No languages found or response is null.")
            }
        }, { onError ->
            Log.d("TAG@@@", "onError: $onError")
        })
    }


    /**/
//    data class BaseResponse<T>(
//        @SerializedName("success") val success: Boolean?,
//        @SerializedName("message") val message: String?,
//        @SerializedName("result") val result: T?
//    )
//
//
//    private fun sendRequest(
//        api: PostRequest<String>,
//        body: RequestBody,
//        onSuccess: (String) -> Unit
//    ) {
//        api.tag(this)
//            .upRequestBody(body)
//            .execute(object : DialogCallback<String?>(this) {
//
//                override fun onError(response: Response<String?>?) {
//                    super.onError(response)
//                    Log.e("TAG@@@", "Error: ${response?.exception}")
//                }
//
//                override fun onSuccess(response: Response<String?>?) {
//                    response?.body()?.let { responseBody ->
//                        Log.i("TAG@@@", "Success: $responseBody")
//                        val jsonObject = JSONObject(responseBody)
//                        val success = jsonObject.optBoolean("success", false)
//
//                        if (success) {
//                            onSuccess(responseBody)
//                        } else {
//                            logErrorMessage(jsonObject)
//                        }
//                    } ?: Log.e("TAG@@@", "Response body is null")
//                }
//            })
//    }
//
//    private fun logErrorMessage(jsonObject: JSONObject) {
//        val message = jsonObject.optString("message")
//        val errors = jsonObject.optJSONObject("errors")
//        Log.i("TAG@@@", "Message: $message")
//        errors?.let {
//            it.optJSONArray("email")
//                ?.let { emailErrors -> Log.i("TAG@@@", "Email Errors: ${emailErrors.join(", ")}") }
//            it.optJSONArray("password")?.let { passwordErrors ->
//                Log.i(
//                    "TAG@@@",
//                    "Password Errors: ${passwordErrors.join(", ")}"
//                )
//            }
//        } ?: Log.i("TAG@@@", "No errors present.")
//    }
//
//    private fun registerResponse(
//        email: String,
//        password: String,
//        name: String,
//        onResult: (BaseResponse<RegisterResult>?) -> Unit
//    ) {
//        val body = MultipartBody.Builder()
//            .setType(MultipartBody.FORM)
//            .apply {
//                addFormDataPart("email", email)
//                addFormDataPart("password", password)
//                addFormDataPart("name", name)
//            }
//            .build()
//
//        sendRequest(Constants.REGISTERAPI, body) { responseBody ->
//            val registerResponse = Gson().fromJson<BaseResponse<RegisterResult>>(
//                responseBody, object : TypeToken<BaseResponse<RegisterResult>>() {}.type
//            )
//            Log.i("TAG@@@", "Message: ${registerResponse.result?.name ?: "No message"}")
//            onResult(registerResponse)
//        }
//    }
//
//
//    private fun loginResponse(
//        email: String,
//        password: String,
//        onResult: (BaseResponse<LoginResult>?) -> Unit
//    ) {
//        val body = MultipartBody.Builder()
//            .setType(MultipartBody.FORM)
//            .addFormDataPart("email", email)
//            .addFormDataPart("password", password)
//            .build()
//
//        sendRequest(Constants.LOGINAPI, body) { responseBody ->
//            val loginResponse = Gson().fromJson<BaseResponse<LoginResult>>(
//                responseBody, object : TypeToken<BaseResponse<LoginResult>>() {}.type
//            )
//            Log.i("TAG@@@", "Message: ${loginResponse.result?.name ?: "No message"}")
//            onResult(loginResponse)
//
//        }
//    }
    /**/

//    private fun RegisterResponse() {
//
//        val body: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
//            .addFormDataPart("email", "priya003@gmail.com")
//            .addFormDataPart("password", "priya004")
//            .addFormDataPart("name", "priya")
//            .build()
//
//        Constants.REGISTERAPI.tag(this)
//            .upRequestBody(body)
//            .execute(object : DialogCallback<String?>(this) {
//
//                override fun onError(response: Response<String?>?) {
//                    super.onError(response)
//                    Log.i(
//                        "TAG@@@",
//                        "onError: ${response!!.isSuccessful}"
//                    )
//                    Log.i(
//                        "TAG@@@",
//                        "onError: ${response.exception}"
//                    )
//                }
//
//                override fun onSuccess(response: Response<String?>?) {
//                    response?.body()?.let { responseBody ->
//                        Log.i("TAG@@@", "onSuccess: $responseBody")
//
//                        val jsonObject = JSONObject(responseBody)
//                        val success = jsonObject.optBoolean("success", false)
//
//                        if (success) {
//
//                            val loginResponse = Gson().fromJson(responseBody, object : TypeToken<BaseResponse<RegisterResult>>() {}.type) as BaseResponse<RegisterResult>
//
//                            Log.i("TAG@@@", "Message: ${loginResponse.result!!.name ?: "No message"}")
//
//                        } else {
//                            val message = jsonObject.optString("message")
//                            val errors = jsonObject.optJSONObject("errors")
//                            Log.i("TAG@@@", "Message: $message")
//                            errors?.let {
//                                it.optJSONArray("email")?.let { emailErrors ->
//                                    Log.i("TAG@@@", "Email Errors: ${emailErrors.join(", ")}")
//                                } ?: Log.i("TAG@@@", "No email errors.")
//
//                                it.optJSONArray("password")?.let { passwordErrors ->
//                                    Log.i("TAG@@@", "Password Errors: ${passwordErrors.join(", ")}")
//                                } ?: Log.i("TAG@@@", "No password errors.")
//                            } ?: Log.i("TAG@@@", "No errors present.")
//
//                        }
//                    } ?: Log.e("TAG@@@", "Response body is null")
//                }
//            })
//
//
//    }


//    private fun LoginResponse() {
//
//        val body: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
//            .addFormDataPart("email", "priya12@gmail.com")
//            .addFormDataPart("password", "password")
//            .build()
//
//        Constants.LOGINAPI.tag(this)
//            .upRequestBody(body)
//            .execute(object : DialogCallback<String?>(this) {
//
//                override fun onError(response: Response<String?>?) {
//                    super.onError(response)
//                    Log.i(
//                        "TAG@@@",
//                        "onError: ${response!!.isSuccessful}"
//                    )
//                    Log.i(
//                        "TAG@@@",
//                        "onError: ${response.exception}"
//                    )
//                }
//
//                override fun onSuccess(response: Response<String?>?) {
//                    response?.body()?.let { responseBody ->
//                        Log.i("TAG@@@", "onSuccess: $responseBody")
//
//                        val jsonObject = JSONObject(responseBody)
//                        val success = jsonObject.optBoolean("success", false)
//
//                        if (success) {
//
//                            val loginResponse = Gson().fromJson(responseBody, object : TypeToken<BaseResponse<LoginResult>>() {}.type) as BaseResponse<LoginResult>
//
//                            Log.i("TAG@@@", "Message: ${loginResponse.result!!.name ?: "No message"}")
//
//                        } else {
//                            val message = jsonObject.optString("message")
//                            val errors = jsonObject.optJSONObject("errors")
//                            Log.i("TAG@@@", "Message: $message")
//                            errors?.let {
//                                it.optJSONArray("email")?.let { emailErrors ->
//                                    Log.i("TAG@@@", "Email Errors: ${emailErrors.join(", ")}")
//                                } ?: Log.i("TAG@@@", "No email errors.")
//
//                                it.optJSONArray("password")?.let { passwordErrors ->
//                                    Log.i("TAG@@@", "Password Errors: ${passwordErrors.join(", ")}")
//                                } ?: Log.i("TAG@@@", "No password errors.")
//                            } ?: Log.i("TAG@@@", "No errors present.")
//
//                        }
//                    } ?: Log.e("TAG@@@", "Response body is null")
//                }
//            })
//
//
//    }


//    private fun response() {
//
//
//        OkGo.get<Language>("https://kalathiyainfotechapi.in/api/language").tag(this) //
//            .headers("Authorization", "Bearer 30|Ie5jcTwaQUgrciynIcNSyyLIEEcAUU5h1zMnyysu23a343d1")
//            .execute(object : DialogCallback<Language?>(this) {
//
//                override fun onError(response: Response<Language?>?) {
//                    super.onError(response)
//
//                    Log.i(
//                        "TAG@@@", "onError: ${response!!.isSuccessful}"
//                    )
//                }
//
//                override fun onSuccess(response: Response<Language?>?) {
//                    Log.i(
//                        "TAG@@@", "onSuccess: ${response!!.body()!!.message}"
//                    )
//                    Log.i(
//                        "TAG@@@", "onSuccess: ${response.body()!!.success}"
//                    )
//
//                    for (result: Language in response.body()!!.result!!) {
//                        Log.i(
//                            "TAG@@@",
//                            "createdAt: ${result.createdAt} deletedAt: ${result.updatedAt}"
//                        )
//                    }
//
//
//                }
//            })
//    }

    private fun JsonArray() {

        OkGo.get<List<Quote>>("https://api.breakingbadquotes.xyz/v1/quotes/500") //
            .tag(this) //
            .execute(object : DialogCallback<List<Quote>?>(this) {

                override fun onError(response: Response<List<Quote>?>?) {
                    super.onError(response)
                }

                override fun onSuccess(response: Response<List<Quote>?>?) {

                    val gson = Gson()
                    val bodyInStringFormat = gson.toJson(response!!.body())

                    Log.i("TAG@@@@", "onSuccess: $bodyInStringFormat")
                }
            })

    }

    private fun JsonString() {
        OkGo.get<String>("https://kalathiyainfotechapi.in/api/language").tag(this) //
            .headers(
                "Authorization", "Bearer 30|Ie5jcTwaQUgrciynIcNSyyLIEEcAUU5h1zMnyysu23a343d1"
            ).execute(object : StringDialogCallback(this) {
                override fun onError(response: Response<String?>?) {

                    val gson = Gson()
                    val bodyInStringFormat = gson.toJson(response!!.body())

                    Log.i("TAG@@@", "onError: $bodyInStringFormat")

//                    handleError(response)
                }

                override fun onSuccess(response: Response<String?>?) {
//                    Log.i("TAG@@@", "onSuccess: $response")

                    val gson = Gson()
                    val bodyInStringFormat = gson.toJson(response!!.body())

                    Log.i("TAG@@@", "onSuccess: $bodyInStringFormat")


//                    handleResponse(response)
                }
            })

    }

    private fun retrofitRequest() {


        ServerApi.getString(
            "Bearer 30|Ie5jcTwaQUgrciynIcNSyyLIEEcAUU5h1zMnyysu23a343d1", ""
        ) //
            .subscribeOn(Schedulers.io()) //
            .doOnSubscribe { Log.i("TAG@@@", "onCreate: showLoading()") } //
            .observeOn(AndroidSchedulers.mainThread()) //
            .subscribe(object : Observer<String> {
                override fun onSubscribe(d: Disposable) {
                    addDisposable(d)
                }

                override fun onError(e: Throwable) {
                    Log.i("TAG@@@", "onCreate: onError  ${e.message}")
                }

                override fun onComplete() {
                    Log.i("TAG@@@", "onCreate: dismissLoading()")
                }

                override fun onNext(t: String) {
                    Log.i("TAG@@@", "onCreate: onNext  $t")
                }
            })


    }

    private fun jsonRequest() {

        val type = object : TypeToken<LzyResponse<Language>>() {}.type

        ServerApi.getData<LzyResponse<Language>>(
            type,
            "https://kalathiyainfotechapi.in/api/language",
            "Bearer 30|Ie5jcTwaQUgrciynIcNSyyLIEEcAUU5h1zMnyysu23a343d1",
            ""
        ) //
            .subscribeOn(Schedulers.io()) //
            .doOnSubscribe { Log.i("TAG@@@", "onCreate: showLoading()") } //
            .map { response -> response.data!! }.observeOn(AndroidSchedulers.mainThread()) //
            .subscribe(object : Observer<Language> {
                override fun onSubscribe(d: Disposable) {
                    addDisposable(d)
                }

                override fun onError(e: Throwable) {
                    Log.i("TAG@@@", "onCreate: onError  ${e.message}")
                }

                override fun onComplete() {
                    Log.i("TAG@@@", "onCreate: dismissLoading()")
                }

                override fun onNext(t: Language) {
                    Log.i("TAG@@@", "onCreate: onNext  $t")
                }
            })

    }

    private var compositeDisposable: CompositeDisposable? = null

    fun addDisposable(disposable: Disposable?) {
        if (compositeDisposable == null) {
            compositeDisposable = CompositeDisposable()
        }
        compositeDisposable!!.add(disposable!!)
    }

    fun dispose() {
        if (compositeDisposable != null) compositeDisposable!!.dispose()
    }


    override fun onDestroy() {
        super.onDestroy()
        //Activity销毁时，取消网络请求
        OkGo.getInstance().cancelTag(this)

//        Rx
//        dispose()
    }

}