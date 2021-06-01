package com.sihaloho.cashierapp.retrofit.response.product

import com.google.gson.annotations.SerializedName

data class UpdetedAt(
    @field:SerializedName("data")
    val data: String,

    @field:SerializedName("timezone")
    val timezone: String,

    @field:SerializedName("timezone_type")
    val timezone_type: String
)
