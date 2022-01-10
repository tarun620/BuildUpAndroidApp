package com.example.buildup.ui.Return

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.buildup.R
import com.example.buildup.databinding.ItemReturnReasonBinding

class OrderReturnReasonAdapter(val onReasonClicked:(returnReasonItem:String?)->Unit) : ListAdapter<String, OrderReturnReasonAdapter.OrderReturnReasonViewHolder>(
    object : DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem

        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }
) {
    var lastSelectedPosition=-1
    inner class OrderReturnReasonViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderReturnReasonAdapter.OrderReturnReasonViewHolder {
        return OrderReturnReasonViewHolder(
            parent.context.getSystemService(LayoutInflater::class.java).inflate(
                R.layout.item_return_reason,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: OrderReturnReasonAdapter.OrderReturnReasonViewHolder,
        position: Int
    ) {
        var bind=ItemReturnReasonBinding.bind(holder.itemView).apply {
            val returnReasonItem=getItem(position)

            radioBtnReturnReason.text=returnReasonItem
            radioBtnReturnReason.isChecked=position == lastSelectedPosition
            radioBtnReturnReason.setOnClickListener {
                val copyOfLastCheckedPosition: Int = lastSelectedPosition
                lastSelectedPosition = position
                notifyItemChanged(copyOfLastCheckedPosition)
                notifyItemChanged(lastSelectedPosition)
                onReasonClicked(returnReasonItem)

            }

        }
    }
}