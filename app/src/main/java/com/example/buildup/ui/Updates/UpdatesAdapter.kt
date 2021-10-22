package com.example.buildup.ui.Updates

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.api.models.responsesAndData.updates.Update
import com.example.buildup.R
import com.example.buildup.databinding.ItemUpdatesBinding
import com.example.buildup.extensions.loadImage
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
                R.layout.item_updates,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: UpdateViewHolder, position: Int) {
        var bind=ItemUpdatesBinding.bind(holder.itemView).apply {
            val update=getItem(position)

            updatesImage.loadImage(update.workCategoryId?.image!!)
            updatesTitle.text=update.workCategoryId?.name
            updatesDesc.text=update.description
            updatesDate.timeStamp=update.createdAt

        }
    }
}