package com.example.buildup.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.api.models.responsesAndData.brand.Brand
import com.example.buildup.R
import com.example.buildup.databinding.ItemBrandBinding
import com.example.buildup.extensions.loadBrandImage

class BrandAdapter(val onBrandClicked:(brandName:String?)->Unit) : ListAdapter<Brand,BrandAdapter.BrandViewHolder>(
    object : DiffUtil.ItemCallback<Brand>(){
        override fun areItemsTheSame(oldItem: Brand, newItem: Brand): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Brand, newItem: Brand): Boolean {
            return oldItem.toString() == newItem.toString()
        }

    }
) {
    inner class BrandViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandViewHolder {
        return BrandViewHolder(
            parent.context.getSystemService(LayoutInflater::class.java).inflate(
//                R.layout.list_item_property,
                R.layout.item_brand,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BrandViewHolder, position: Int) {
        var bind=ItemBrandBinding.bind(holder.itemView).apply {
            val brand=getItem(position)
            ivBrandImage.loadBrandImage(brand.image!!)

            root.setOnClickListener { onBrandClicked(brand.name) }
        }
    }

}