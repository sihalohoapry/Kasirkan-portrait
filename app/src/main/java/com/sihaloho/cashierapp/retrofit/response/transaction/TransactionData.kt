package com.sihaloho.cashierapp.retrofit.response.transaction

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TransactionData(
    @field:SerializedName("catatan")
    val catatan: String,

    @field:SerializedName("created_at")
    val created_at: String,

    @field:SerializedName("id_transaksi")
    val id_transaksi: String,

    @field:SerializedName("nama_pelanggan")
    val nama_pelanggan: String,

    @field:SerializedName("no_meja")
    val no_meja: String,

    @field:SerializedName("no_transaksi")
    val no_transaksi: String,

    @field:SerializedName("status_pembayaran")
    val status_pembayaran: String,

    @field:SerializedName("total")
    val total: String,

    @field:SerializedName("updated_at")
    val updated_at: String,

    @field:SerializedName("username")
    val username: String,
): Serializable
