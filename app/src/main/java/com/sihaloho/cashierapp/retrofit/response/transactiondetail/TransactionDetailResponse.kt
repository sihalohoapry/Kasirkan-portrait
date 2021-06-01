package com.sihaloho.cashierapp.retrofit.response.transactiondetail

import com.google.gson.annotations.SerializedName

data class TransactionDetailResponse(
    @field:SerializedName("data")
    val `data` : List<TransactionDetail>,

    @field:SerializedName("error")
    val error: Boolean
)
