package com.sihaloho.cashierapp.retrofit.response

import com.google.gson.annotations.SerializedName

data class ExportResponse(
        @field:SerializedName("data")
        val `data` : String,

        @field:SerializedName("error")
        val error : String
)
