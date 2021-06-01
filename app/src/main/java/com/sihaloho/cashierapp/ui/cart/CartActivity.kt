package com.sihaloho.cashierapp.ui.cart

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.sihaloho.cashierapp.R
import com.sihaloho.cashierapp.databinding.ActivityCartBinding
import com.sihaloho.cashierapp.preference.PrefManager
import com.sihaloho.cashierapp.retrofit.ApiConfig
import com.sihaloho.cashierapp.retrofit.response.SubmitResponse
import com.sihaloho.cashierapp.retrofit.response.cart.CartItem
import com.sihaloho.cashierapp.retrofit.response.cart.CartResponse
import com.sihaloho.cashierapp.ui.print.PrintActivity
import com.sihaloho.cashierapp.utils.idrFormat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartActivity : AppCompatActivity() {
    private val api by lazy { ApiConfig.getApi() }
    private val pref by lazy { PrefManager(this) }
    private lateinit var binding: ActivityCartBinding
    private lateinit var adapterCart: AdapterCart
    private lateinit var progressBar : Dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpReclerView()
        setUpView()
        setUpListener()
        isLoading()
    }

    private fun setUpListener() {
        binding.btnCheckout.setOnClickListener {
            if(adapterCart.list.size > 0 && binding.etName.text.isNotEmpty() && binding.etTable.text.isNotEmpty() ){
                checkout()
            }else{
                Toast.makeText(this, "Isi data dengan benar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setUpView() {
        supportActionBar?.title = "Pesanan"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onStart() {
        super.onStart()
        getData()

    }

    private fun getData() {
        progressBar.show()
        val username = pref.getString("pref_user_name")
        if (username!= null){
            api.keranjang(username).enqueue(object : Callback<CartResponse> {
                override fun onResponse(
                    call: Call<CartResponse>,
                    response: Response<CartResponse>
                ) {
                    if (response.isSuccessful) {
                        progressBar.dismiss()
                        adapterCart.setData(response.body())

                    }
                }

                override fun onFailure(call: Call<CartResponse>, t: Throwable) {
                    progressBar.dismiss()
                    Toast.makeText(this@CartActivity, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show()

                }

            })

        }
    }

    private fun setUpReclerView() {
        adapterCart = AdapterCart(this,object : AdapterCart.OnAdapterListener{
            override fun onUpdate(cartItem: CartItem) {
                updateCart(cartItem.id_keranjang,cartItem.jumlah)
            }

            override fun onDelete(cartItem: CartItem, position: Int) {
                val alertDialog = AlertDialog.Builder(this@CartActivity)
                alertDialog.apply {
                    setTitle("Konfirmasi Hapus")
                    setMessage("Yakin menghapus ${cartItem.nama_produk}")
                    setNegativeButton("Batal"){dialog, _ ->
                        dialog.dismiss()
                    }
                    setPositiveButton("Hapus"){dialog,_->
                        dialog.dismiss()
                        deleteCart(cartItem.id_keranjang)
                        adapterCart.deleteItem(position)
                    }
                    alertDialog.show()
                }
            }

        })
        with(binding.rvCart){
            adapter = adapterCart
            setHasFixedSize(true)
        }


    }

    private fun isLoading(){
        progressBar = Dialog(this)
        val layout =  layoutInflater.inflate(R.layout.dialog_loading, null)
        progressBar.let {
            it.setContentView(layout)
            it.setCancelable(false)
            it.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }

     fun liveTotal(){
        var total = 0
        for (cart in adapterCart.list){
            total += cart.total
        }
        binding.tvTotal.text = "Total Rp : ${idrFormat(total)}"

    }

    private fun updateCart(id: String, amount: Int){
        api.updateKeranjang(id, amount).enqueue(object : Callback<SubmitResponse> {
            override fun onResponse(
                call: Call<SubmitResponse>,
                response: Response<SubmitResponse>
            ) {

            }

            override fun onFailure(call: Call<SubmitResponse>, t: Throwable) {
                Toast.makeText(this@CartActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
            }

        })
    }
    private fun deleteCart(id: String){
        api.deleteKeranjang(id).enqueue(object : Callback<SubmitResponse> {
            override fun onResponse(
                call: Call<SubmitResponse>,
                response: Response<SubmitResponse>
            ) {

            }

            override fun onFailure(call: Call<SubmitResponse>, t: Throwable) {
                Toast.makeText(this@CartActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun checkout(){
        progressBar.show()
        val username = pref.getString("pref_user_name")
        if (username!=null){
            api.checkout(
                username,
                binding.etName.text.toString(),
                binding.etTable.text.toString(),
                binding.etNote.text.toString()

            ).enqueue(object : Callback<SubmitResponse> {
                override fun onResponse(
                    call: Call<SubmitResponse>,
                    response: Response<SubmitResponse>
                ) {
                    progressBar.dismiss()
                    if (response.isSuccessful) {
                        responseCheckout(response.body())
                    }
                }

                override fun onFailure(call: Call<SubmitResponse>, t: Throwable) {
                    Toast.makeText(this@CartActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                }

            })
        }


    }

    private fun responseCheckout(data: SubmitResponse?) {
        if (data!= null){

            val alertDialog = AlertDialog.Builder(this@CartActivity)
            alertDialog.apply {
                setTitle("${data.message}")
                setMessage("Print bukti pembayaran?")
                setNegativeButton("Tutup"){dialog, _ ->
                    dialog.dismiss()
                    finish()
                }
                setPositiveButton("Print"){dialog,_->
                    dialog.dismiss()
                    val intent = Intent(this@CartActivity, PrintActivity::class.java)
                    startActivity(
                            intent
                                    .putExtra("intent_table", binding.etTable.text.toString())
                                    .putExtra("intent_name", binding.etName.text.toString())
                                    .putExtra("intent_total", binding.tvTotal.text.toString())
                                    .putExtra("intent_cart", adapterCart.list)
                    )
                    finish()
                }
                alertDialog.show()
            }

        }

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

}