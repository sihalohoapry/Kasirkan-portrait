package com.sihaloho.cashierapp.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sihaloho.cashierapp.R
import com.sihaloho.cashierapp.databinding.AdapterProductBinding
import com.sihaloho.cashierapp.retrofit.response.product.Product

class AdapterProduct : RecyclerView.Adapter<AdapterProduct.ProductViewHolder>() {

    private var list = ArrayList<Product>()
    var onClick : ((Product)-> Unit) ?= null

    fun setData(newList: List<Product>?){
        if (newList==null)return
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view){

        val binding = AdapterProductBinding.bind(view)
        fun bind(data: Product){
            with(binding){
                Glide.with(itemView)
                    .load(data.gambar_produk)
                    .placeholder(R.drawable.ic_image_load)
                    .error(R.drawable.ic_image_load)
                    .into(imageProduct)
                tvName.text = data.nama_produk
                tvHarga.text = "Rp ${data.harga}"
            }
        }
        init {
            binding.root.setOnClickListener {

            }

            binding.root.setOnLongClickListener{
                onClick?.invoke(list[adapterPosition])
                true
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder =
        ProductViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_product, parent, false))

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

}