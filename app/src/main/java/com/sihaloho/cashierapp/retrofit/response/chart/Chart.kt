package com.sihaloho.cashierapp.retrofit.response.chart

import com.google.gson.annotations.SerializedName

data class Chart(
    @field:SerializedName("data")
    val `data`: String,

    @field:SerializedName("nama_bulan")
    val nama_bulan : String
)
