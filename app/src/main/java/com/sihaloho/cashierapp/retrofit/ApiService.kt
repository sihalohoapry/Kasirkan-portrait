package com.sihaloho.cashierapp.retrofit

import com.sihaloho.cashierapp.retrofit.response.ExportResponse
import com.sihaloho.cashierapp.retrofit.response.SubmitResponse
import com.sihaloho.cashierapp.retrofit.response.cart.CartResponse
import com.sihaloho.cashierapp.retrofit.response.cashier.CashierResponse
import com.sihaloho.cashierapp.retrofit.response.category.CategoryResponse
import com.sihaloho.cashierapp.retrofit.response.chart.ChartResponse
import com.sihaloho.cashierapp.retrofit.response.login.LoginResponse
import com.sihaloho.cashierapp.retrofit.response.product.ProductResponse
import com.sihaloho.cashierapp.retrofit.response.transaction.TransactionResponse
import com.sihaloho.cashierapp.retrofit.response.transactiondetail.TransactionDetailResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("kategori")
    fun category(): Call<CategoryResponse>

    @GET("produk")
    fun produk(
        @Query("nama") nama : String,
        @Query("id_kategori") id_kategori : String?
    ) : Call<ProductResponse>

    @FormUrlEncoded
    @POST("login/login")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String,
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("keranjang")
    fun keranjang(
        @Field("username") username: String,
        @Field("id_produk") id_produk: Int,
        @Field("jumlah") jumlah: Int
    ): Call<SubmitResponse>


    @GET("keranjang")
    fun keranjang(
        @Query("username") username: String
    ): Call<CartResponse>

    @FormUrlEncoded
    @PUT("keranjang/{id}")
    fun updateKeranjang(
        @Path("id")id: String,
        @Field("jumlah") jumlah: Int
    ): Call<SubmitResponse>


    @DELETE("keranjang/{id}")
    fun deleteKeranjang(
        @Path("id")id: String,
    ): Call<SubmitResponse>

    @FormUrlEncoded
    @POST("checkout")
    fun checkout(
        @Field("username") username: String,
        @Field("nama_pelanggan") nama_pelanggan: String,
        @Field("no_meja") no_meja: String,
        @Field("catatan") catatan: String
    ): Call<SubmitResponse>

    @GET("kasir")
    fun kasir(): Call<CashierResponse>

    @FormUrlEncoded
    @POST("transaksi-kasir")
    fun transaksiKasir(
        @Field("username") username: String,
        @Field("no_transaksi") no_transaksi: String
    ): Call<TransactionResponse>


    @FormUrlEncoded
    @POST("transaksi-date")
    fun transaksiDate(
        @Field("tgl_awal") tgl_awal: String,
        @Field("tgl_akhir") tgl_akhir: String,
        @Field("no_transaksi") no_transaksi: String
    ): Call<TransactionResponse>

    @FormUrlEncoded
    @POST("transaksi/get-transaksi-bykasir-date")
    fun penjualanHarianByKasir(
        @Field("tgl_awal") tgl_awal: String,
        @Field("tgl_akhir") tgl_akhir: String,
        @Field("username") username: String
    ): Call<TransactionResponse>

    @FormUrlEncoded
    @POST("transaksi/detail")
    fun transaksiDetail(
        @Field("id_transaksi") id_transaksi: String
    ): Call<TransactionDetailResponse>


    @GET("export-excel")
    fun exportExcel(
            @Query("tgl_awal") tgl_awal: String,
            @Query("tgl_akhir") tgl_akhir: String
    ): Call<ExportResponse>
    @GET("export-pdf")
    fun exportPdf(
            @Query("tgl_awal") tgl_awal: String,
            @Query("tgl_akhir") tgl_akhir: String
    ): Call<ExportResponse>

    @FormUrlEncoded
    @POST("chart")
    fun chart(
        @Field("tahun") tahun: String
    ): Call<ChartResponse>

}