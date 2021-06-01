package com.sihaloho.cashierapp.retrofit.response.category

import com.google.gson.annotations.SerializedName

data class CategoryItem(
    @field:SerializedName("created_at")
    val created_at : String,

    @field:SerializedName("deleted_at")
    val deleted_at : Any,

    @field:SerializedName("id_kategori")
    val id_kategori : String,

    @field:SerializedName("nama_kategori")
    val nama_kategori : String,

    @field:SerializedName("updated_at")
    val updated_at : String
)
