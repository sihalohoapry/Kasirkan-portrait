package com.sihaloho.cashierapp.retrofit.response.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @field:SerializedName("data")
    val `data`: Login,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)