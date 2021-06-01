package com.sihaloho.cashierapp.retrofit.response.cashier

import com.google.gson.annotations.SerializedName

data class CashierResponse(
    @field:SerializedName("data")
    val `data`: List<DataCashier>,

    @field:SerializedName("error")
    val error: Boolean
)
