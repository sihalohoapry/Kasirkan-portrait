package com.sihaloho.cashierapp.ui.transaction

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sihaloho.cashierapp.R
import com.sihaloho.cashierapp.databinding.AdapterTransactionDetailBinding
import com.sihaloho.cashierapp.retrofit.response.transactiondetail.TransactionDetail
import com.sihaloho.cashierapp.utils.idrFormat

class AdapterTransactionDetail : RecyclerView.Adapter<AdapterTransactionDetail.TransactionViewHolder>() {

    private var list = ArrayList<TransactionDetail>()
    var onClick : ((TransactionDetail)-> Unit) ?= null

    fun setData(newList: List<TransactionDetail>?){
        if (newList==null)return
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    inner class TransactionViewHolder(view: View) : RecyclerView.ViewHolder(view){

        val binding = AdapterTransactionDetailBinding.bind(view)
        fun bind(data: TransactionDetail){
            with(binding){
                Glide.with(itemView)
                    .load(data.gambar_produk)
                    .placeholder(R.drawable.ic_image_load)
                    .error(R.drawable.ic_image_load)
                    .into(imageProduct)
                tvName.text = data.nama_produk
                tvHarga.text = data.harga
                tvHarga.text= "Rp. ${idrFormat(data.harga.toInt() )} X ${data.jumlah} Item"
                tvTotal.text= "Rp. ${idrFormat(data.total.toInt() )}"
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder =
        TransactionViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_transaction_detail, parent, false))

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size


}