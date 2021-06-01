package com.sihaloho.cashierapp.retrofit.response.transaction

import com.google.gson.annotations.SerializedName

data class TransactionResponse(
    @field:SerializedName("data")
    val `data`: List<TransactionData>,

    @field:SerializedName("error")
    val error: Boolean
)
