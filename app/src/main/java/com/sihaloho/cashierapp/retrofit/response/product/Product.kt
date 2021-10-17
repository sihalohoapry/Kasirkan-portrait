package com.sihaloho.cashierapp.retrofit.response.product

import com.google.gson.annotations.SerializedName

data class Product(
    @field:SerializedName("created_at")
    val created_at : String,

    @field:SerializedName("deleted_at")
    val deleted_at : Any,

    @field:SerializedName("gambar_produk")
    val gambar_produk : String,

    @field:SerializedName("harga")
    val harga : String,

    @field:SerializedName("id_kategori")
    val id_kategori : String,

    @field:SerializedName("id_produk")
    val id_produk : String,

    @field:SerializedName("nama_produk")
    val nama_produk : String,

    @field:SerializedName("updated_at")
    val updated_at : String,
)
