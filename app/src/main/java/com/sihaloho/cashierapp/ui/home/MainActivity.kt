package com.sihaloho.cashierapp.ui.home

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sihaloho.cashierapp.R
import com.sihaloho.cashierapp.databinding.ActivityMainBinding
import com.sihaloho.cashierapp.preference.PrefManager
import com.sihaloho.cashierapp.retrofit.ApiConfig
import com.sihaloho.cashierapp.retrofit.response.SubmitResponse
import com.sihaloho.cashierapp.retrofit.response.category.CategoryResponse
import com.sihaloho.cashierapp.retrofit.response.product.Product
import com.sihaloho.cashierapp.retrofit.response.product.ProductResponse
import com.sihaloho.cashierapp.retrofit.response.transaction.TransactionResponse
import com.sihaloho.cashierapp.ui.cart.CartActivity
import com.sihaloho.cashierapp.ui.login.LoginActivity
import com.sihaloho.cashierapp.ui.transaction.AdapterTransaction
import com.sihaloho.cashierapp.utils.CalendarUtils
import com.sihaloho.cashierapp.utils.convertTanggal
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val api by lazy { ApiConfig.getApi() }
    private val prefManager by lazy { PrefManager(this) }
    private lateinit var adapterCategory: AdapterCategory
    private lateinit var adapterProduct: AdapterProduct
    private lateinit var menuItemCount: MenuItem
    private var itemCount: Int = 0

    var progresDialog: Dialog? = null

    private var idCategory : String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpRecyclerView()
        setUpListener()
        isLoading()

    }

    private fun setUpRecyclerView() {
        adapterCategory = AdapterCategory()
        with(binding.rvCategory){
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = adapterCategory
            setHasFixedSize(true)
        }
        adapterCategory.onClick = {
            idCategory = it.id_kategori
            listProduct()
        }
        adapterProduct = AdapterProduct()
        with(binding.rvProduct){
            adapter = adapterProduct
            setHasFixedSize(true)
        }
        adapterProduct.onClick = {
            cartDialog(it)
        }
    }

    private fun setUpListener(){
        binding.etSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH){
                listProduct()
                true
            }else{
                false
            }
        }
    }

    override fun onStart() {
        super.onStart()
        listCategory()
        listProduct()
        historyTransactionToday()
    }

    private fun historyTransactionToday() {
        val calendarUtils = CalendarUtils
        val username = prefManager.getString("pref_user_name")
        val today = "${calendarUtils.year}-${calendarUtils.month+1}-${calendarUtils.day}"
        api.penjualanHarianByKasir(convertTanggal(today, "yyyy-MM-d", "yyyy-M-d"),convertTanggal(today, "yyyy-MM-d", "yyyy-M-d") , username!!).enqueue(object : Callback<TransactionResponse> {
            override fun onResponse(call: Call<TransactionResponse>, response: Response<TransactionResponse>) {
                if (response.isSuccessful) {
                    val data = AdapterTransaction()
                    data.setData(response.body()?.data)
                    menuItemCount.title = data.itemCount.toString()
                }
            }

            override fun onFailure(call: Call<TransactionResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun listCategory(){
        api.category().enqueue(object : Callback<CategoryResponse>{
            override fun onResponse(
                call: Call<CategoryResponse>,
                response: Response<CategoryResponse>
            ) {
                if (response.isSuccessful){
                    response.body()?.let {
                        adapterCategory.setData(it)
                    }
                }
            }

            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
            }

        })
    }
    private fun listProduct(){
        progresDialog?.show()
        api.produk(binding.etSearch.text.toString(), idCategory).enqueue(object :
            Callback<ProductResponse> {
            override fun onResponse(
                call: Call<ProductResponse>,
                response: Response<ProductResponse>
            ) {
                if (response.isSuccessful) {
                    progresDialog?.dismiss()
                    response.body()?.let {
                        adapterProduct.setData(it)
                    }
                }
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                progresDialog?.dismiss()
                Toast.makeText(this@MainActivity, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun isLoading(){
        progresDialog = Dialog(this)
        val dialogLayout =layoutInflater.inflate(R.layout.dialog_loading, null)
        progresDialog?.let {
            it.setContentView(dialogLayout)
            it.setCancelable(false)
            it.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        menuItemCount = menu!!.findItem(R.id.action_count)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){

            R.id.action_cart->{
                startActivity(Intent(this,CartActivity::class.java))
                true
            }

            R.id.action_logout -> {
                prefManager.clear()
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
                true
            }else -> super.onOptionsItemSelected(item)
        }
    }

    private fun cartDialog(product: Product){
        val cartDialog = layoutInflater.inflate(R.layout.dialog_cart, null)
        val dialog = BottomSheetDialog(this)
        dialog.apply {
            setContentView(cartDialog)
            setTitle("")
            setCancelable(false)
        }

        cartDialog.findViewById<TextView>(R.id.tv_title).text = product.nama_produk
        Glide.with(baseContext)
            .load(product.gambar_produk)
            .into(cartDialog.findViewById<ImageView>(R.id.iv_product))

        val editQty = cartDialog.findViewById<TextView>(R.id.tv_qty)
        cartDialog.findViewById<Button>(R.id.btn_plus).setOnClickListener {
            var qty = editQty.text.toString().toInt()
            qty +=1
            editQty.text = qty.toString()

        }
        cartDialog.findViewById<Button>(R.id.btn_minus).setOnClickListener {
            var qty = editQty.text.toString().toInt()
            if (qty>1) qty -=1
            editQty.text = qty.toString()

        }
        cartDialog.findViewById<ImageView>(R.id.iv_close).setOnClickListener{
            dialog.dismiss()
        }
        cartDialog.findViewById<Button>(R.id.btn_tambah).setOnClickListener{
            itemCount +=1
            dialog.dismiss()
            addToCart(product.id_produk.toInt(), editQty.text.toString().toInt())
        }

        dialog.show()

    }

    private fun addToCart(idProduct: Int, qty: Int ){
        progresDialog?.show()
        val username = prefManager.getString("pref_user_name")
        if (username!=null) {
            api.keranjang(username, idProduct, qty).enqueue(object : Callback<SubmitResponse> {
                override fun onResponse(
                    call: Call<SubmitResponse>,
                    response: Response<SubmitResponse>
                ) {
                    if (response.isSuccessful) {
                        progresDialog?.dismiss()
                        Toast.makeText(this@MainActivity, response.body()?.message, Toast.LENGTH_SHORT).show()

                    }
                }

                override fun onFailure(call: Call<SubmitResponse>, t: Throwable) {
                    progresDialog?.dismiss()
                    Toast.makeText(this@MainActivity, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show()
                }

            })
        }

    }
    override fun onBackPressed() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setMessage("Anda Yakin Ingin Keluar ?")
        builder.setPositiveButton("Ya", DialogInterface.OnClickListener { dialog, which -> //jika kalian menekan tombol ya, maka otomatis akan keluar dari activity saat ini
            finish()
        })
        builder.setNegativeButton("Tidak", DialogInterface.OnClickListener { dialog, which -> //jika menekan tombol tidak, maka kalian akan tetap berada di activity saat ini
            dialog.cancel()
        })
        val alert: AlertDialog = builder.create()
        alert.show()

    }

}