package com.sihaloho.cashierapp.retrofit.response.transactiondetail

import com.google.gson.annotations.SerializedName


data class TransactionDetail(

    @field:SerializedName("created_at")
    val created_at: String,

    @field:SerializedName("gambar_produk")
    val gambar_produk : String,

    @field:SerializedName("harga")
    val harga : String,

    @field:SerializedName("id_produk")
    val id_produk : String,

    @field:SerializedName("id_transaksi")
    val id_transaksi: String,

    @field:SerializedName("id_transaksi_detail")
    val id_transaksi_detail: String,

    @field:SerializedName("jumlah")
    val jumlah: String,

    @field:SerializedName("nama_produk")
    val nama_produk : String,

    @field:SerializedName("total")
    val total: String,

    @field:SerializedName("updated_at")
    val updated_at: String
)
