package com.sihaloho.cashierapp.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sihaloho.cashierapp.R
import com.sihaloho.cashierapp.databinding.AdapterCategoryBinding
import com.sihaloho.cashierapp.retrofit.response.category.CategoryItem

class AdapterCategory : RecyclerView.Adapter<AdapterCategory.CategoryViewHolder>() {

    private var list = ArrayList<CategoryItem>()
    var onClick : ((CategoryItem)-> Unit) ?= null
    private val items = arrayListOf<TextView>()

    fun setData(newList: List<CategoryItem>?){
        if (newList==null)return
        list.clear()
        list.add(CategoryItem(created_at = "", deleted_at = "",id_kategori = "",nama_kategori = "Semua",updated_at = ""))
        list.addAll(newList)
        notifyDataSetChanged()
    }

    inner class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view){

        val binding = AdapterCategoryBinding.bind(view)
        fun bind(data: CategoryItem){
            with(binding){
                tvName.text = data.nama_kategori
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder =
        CategoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_category, parent, false))

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
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