package com.sihaloho.cashierapp.retrofit.response

import com.google.gson.annotations.SerializedName

data class SubmitResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)
