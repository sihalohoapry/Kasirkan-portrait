package com.sihaloho.cashierapp.ui.transaction

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sihaloho.cashierapp.R
import com.sihaloho.cashierapp.databinding.AdapterCashierBinding
import com.sihaloho.cashierapp.retrofit.response.cashier.DataCashier

class AdapterCashier : RecyclerView.Adapter<AdapterCashier.CashierViewHolder>() {

    private var list = ArrayList<DataCashier>()
    var onClick : ((DataCashier)-> Unit) ?= null
    private val items = arrayListOf<TextView>()

    fun setData(newList: List<DataCashier>?){
        if (newList==null)return
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    inner class CashierViewHolder(view: View) : RecyclerView.ViewHolder(view){

        val binding = AdapterCashierBinding.bind(view)
        fun bind(data: DataCashier){
            with(binding){
                tvName.text = data.nama
                items.add(tvName)
            }
        }
        init {
            binding.root.setOnClickListener {
                setColor(binding.tvName)
                onClick?.invoke(list[adapterPosition])
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CashierViewHolder =
        CashierViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_cashier, parent, false))

    override fun onBindViewHolder(holder: CashierViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    private fun setColor(textView: TextView){
        items.forEach {
            it.setBackgroundResource(R.color.white)
        }
        textView.setBackgroundResource(R.color.light_gray)

    }



}