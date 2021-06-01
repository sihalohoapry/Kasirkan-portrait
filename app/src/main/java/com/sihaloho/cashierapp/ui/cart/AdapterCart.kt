package com.sihaloho.cashierapp.ui.cart

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Keep
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sihaloho.cashierapp.R
import com.sihaloho.cashierapp.databinding.AdapterCartBinding
import com.sihaloho.cashierapp.retrofit.response.cart.CartItem

@Keep class AdapterCart(var context: Context, val listener: OnAdapterListener) : RecyclerView.Adapter<AdapterCart.CartHolder>() {

    var list = ArrayList<CartItem>()

    fun setData(newList: List<CartItem>?){
        if (newList==null)return
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    inner class CartHolder(view: View) : RecyclerView.ViewHolder(view){

        val binding = AdapterCartBinding.bind(view)
        fun bind(data: CartItem){
            with(binding){
                Glide.with(itemView)
                    .load(data.gambar_produk)
                    .placeholder(R.drawable.ic_image_load)
                    .error(R.drawable.ic_image_load)
                    .into(ivProduct)
                tvTitle.text = data.nama_produk
                tvPrice.text = "Rp ${data.harga}"
                labelQty.text = data.jumlah.toString()
                (context as CartActivity).liveTotal()
                tvPlus.setOnClickListener {
                    data.jumlah ++
                    data.total = data.harga*data.jumlah
                    labelQty.text = data.jumlah.toString()
                    (context as CartActivity).liveTotal()
                    listener.onUpdate(data)
                }
                tvMin.setOnClickListener {
                    if (data.jumlah>1){
                        data.jumlah --
                        data.total = data.harga*data.jumlah
                        labelQty.text = data.jumlah.toString()
                        (context as CartActivity).liveTotal()
                        listener.onUpdate(data)

                    }else{
                        listener.onDelete(data, adapterPosition)
                    }
                }

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartHolder =
        CartHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_cart, parent, false))

    override fun onBindViewHolder(holder: CartHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    interface OnAdapterListener{
        fun onUpdate(cartItem: CartItem)
        fun onDelete(cartItem: CartItem,position: Int)
    }

    fun deleteItem(position: Int){
        list.removeAt(position)
        notifyItemRemoved(position)
        notifyItemChanged(position, list.size)
        (context as CartActivity).liveTotal()

    }

}