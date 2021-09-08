package com.example.buildup.ui.Updates

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.api.models.responses.updates.Update
import com.example.buildup.R
import com.example.buildup.databinding.ListItemUpdateBinding
import com.example.buildup.extensions.timeStamp

class UpdatesAdapter: ListAdapter<Update, UpdatesAdapter.UpdateViewHolder>(

    object : DiffUtil.ItemCallback<Update>(){
        override fun areItemsTheSame(oldItem: Update, newItem: Update): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Update, newItem: Update): Boolean {
            return oldItem.toString() == newItem.toString()
        }
    }
) {

    inner class UpdateViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpdateViewHolder {

        return UpdateViewHolder(
            parent.context.getSystemService(LayoutInflater::class.java).inflate(
                R.layout.list_item_update,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: UpdateViewHolder, position: Int) {
        var bind=ListItemUpdateBinding.bind(holder.itemView).apply {
            val update=getItem(position)

            tvUpdateDesc.text=update.description
//            tvUpdateDate.text=update.createdAt
            tvUpdateDate.timeStamp=update.createdAt

        }
    }
}