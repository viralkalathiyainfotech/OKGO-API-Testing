package com.example.myapplication.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginResult {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("access_token")
    @Expose
    var accessToken: String? = null

    @SerializedName("role_id")
    @Expose
    var roleId: Int? = null

    @SerializedName("languages")
    @Expose
    var languages: List<Any>? = null

    @SerializedName("artist")
    @Expose
    var artist: List<Any>? = null
}
