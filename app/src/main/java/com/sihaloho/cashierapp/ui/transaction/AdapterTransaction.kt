package com.sihaloho.cashierapp.ui.transaction

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sihaloho.cashierapp.R
import com.sihaloho.cashierapp.databinding.AdapterTransactionBinding
import com.sihaloho.cashierapp.retrofit.response.transaction.TransactionData
import com.sihaloho.cashierapp.utils.convertTanggal
import com.sihaloho.cashierapp.utils.idrFormat

class AdapterTransaction : RecyclerView.Adapter<AdapterTransaction.TransactionViewHolder>() {

    private var list = ArrayList<TransactionData>()
    var onClick : ((TransactionData)-> Unit) ?= null
    private val items = arrayListOf<TextView>()

    fun setData(newList: List<TransactionData>?){
        if (newList==null)return
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    inner class TransactionViewHolder(view: View) : RecyclerView.ViewHolder(view){

        val binding = AdapterTransactionBinding.bind(view)
        fun bind(data: TransactionData){
            with(binding){
                tvDate.text = convertTanggal(data.created_at,"dd MMMM yyyy kk:mm","yyyy-MM-dd kk:mm:ss")
                tvNamaCashier.text = data.username
                tvNo.text = data.no_transaksi
                tvTotal.text= "Rp. ${idrFormat(data.total.toInt() )}"
            }
        }
        init {
            binding.root.setOnClickListener {
                onClick?.invoke(list[adapterPosition])
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder =
        TransactionViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_transaction, parent, false))

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    private fun setColor(textView: TextView){
        items.forEach {
            it.setBackgroundResource(R.color.white)
        }
        textView.setBackgroundResource(R.color.purple_200)

    }

}