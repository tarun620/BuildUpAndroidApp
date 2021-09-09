package com.example.buildup.ui.Property.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.api.models.responses.property.propertyResponses.PropertyResponse
import com.example.buildup.R
import com.example.buildup.databinding.ItemPropertyBinding
import com.example.buildup.databinding.ListItemPropertyBinding
import kotlin.math.abs

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
//                R.layout.list_item_property,
                R.layout.item_property,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        var bind=ItemPropertyBinding.bind(holder.itemView).apply {
            val property=getItem(position)

            tvPropertyName.text=property.name
            tvPropertyLatestUpdate.text=property.latestUpdate?.description
            tvPropertyETA.text= "ETA" +" : "+ property.eta?.value.toString()+" "+property.eta?.unit
            var progress=((property.completed!!)*100)/6
            propertyProgress.progress=progress
            tvProgress.text= abs(progress).toString() + "%"
            root.setOnClickListener { onPropertyClicked(property.id) }
        }
    }
}