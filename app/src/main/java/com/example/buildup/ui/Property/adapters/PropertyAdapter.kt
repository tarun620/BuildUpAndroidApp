package com.example.buildup.ui.Property.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.api.models.responses.property.propertyResponses.PropertyResponse
import com.example.buildup.R
import com.example.buildup.databinding.ListItemPropertyBinding

class PropertyAdapter(val onPropertyClicked:(propertyId:String?)->Unit) : ListAdapter<PropertyResponse, PropertyAdapter.PropertyViewHolder>(
    object : DiffUtil.ItemCallback<PropertyResponse>(){
        override fun areItemsTheSame(oldItem: PropertyResponse, newItem: PropertyResponse): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PropertyResponse, newItem: PropertyResponse): Boolean {
            return oldItem.toString() == newItem.toString()
        }
    }

) {
    inner class PropertyViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        return PropertyViewHolder(
            parent.context.getSystemService(LayoutInflater::class.java).inflate(
                R.layout.list_item_property,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        var bind=ListItemPropertyBinding.bind(holder.itemView).apply {
            val property=getItem(position)

            tvPropertyName.text=property.name
            tvPropertydesc.text=property.latestUpdate?.description
            tvEta.text= property.eta?.value.toString()+" "+property.eta?.unit

            root.setOnClickListener { onPropertyClicked(property.id) }
        }
    }
}