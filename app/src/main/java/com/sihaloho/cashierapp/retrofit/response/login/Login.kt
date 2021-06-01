package com.sihaloho.cashierapp.retrofit.response.login

import com.google.gson.annotations.SerializedName

data class Login(
    @field:SerializedName("created_at")
    val created_at: String,

    @field:SerializedName("is_aktif")
    val is_aktif: String,

    @field:SerializedName("level")
    val level: String,

    @field:SerializedName("nama")
    val nama: String,

    @field:SerializedName("password")
    val password: String,

    @field:SerializedName("updated_at")
    val updated_at: String,

    @field:SerializedName("username")
    val username: String
)
