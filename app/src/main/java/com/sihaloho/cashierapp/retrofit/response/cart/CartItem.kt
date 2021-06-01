package com.sihaloho.cashierapp.retrofit.response.cart

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartItem(
    @field:SerializedName("created_at")
    val created_at : String,

    @field:SerializedName("gambar_produk")
    val gambar_produk: String,

    @field:SerializedName("harga")
    var harga: Int,

    @field:SerializedName("id_keranjang")
    val id_keranjang: String,

    @field:SerializedName("id_produk")
    val id_produk: String,

    @field:SerializedName("jumlah")
    var jumlah: Int,

    @field:SerializedName("nama_produk")
    val nama_produk: String,

    @field:SerializedName("total")
    var total: Int,

    @field:SerializedName("updated_at")
    val updated_at : String,

    @field:SerializedName("username")
    val username: String
) : Parcelable
