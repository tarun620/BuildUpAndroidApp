package com.example.buildup.ui.Return

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.buildup.R
import com.example.buildup.databinding.ItemQuantityBinding

class QuantityAdapter(val onQuantityClicked:(quantity:Int?)->Unit) : ListAdapter<Int,QuantityAdapter.QuantityViewHolder>(
    object : DiffUtil.ItemCallback<Int>(){
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem

        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }

    }
) {
    inner class QuantityViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuantityViewHolder {
        return QuantityViewHolder(
            parent.context.getSystemService(LayoutInflater::class.java).inflate(
                R.layout.item_quantity,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: QuantityViewHolder, position: Int) {
        var bind=ItemQuantityBinding.bind(holder.itemView).apply {
            val quantity=getItem(position)

            root.setOnClickListener {
                onQuantityClicked(quantity)
            }
        }
    }

}