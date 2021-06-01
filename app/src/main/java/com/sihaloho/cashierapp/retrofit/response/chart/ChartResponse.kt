package com.sihaloho.cashierapp.retrofit.response.chart

import com.google.gson.annotations.SerializedName

data class ChartResponse(
    @field:SerializedName("data")
    val `data`: List<Chart>,

    @field:SerializedName("error")
    val error: String
)