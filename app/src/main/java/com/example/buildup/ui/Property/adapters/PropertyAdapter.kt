package com.example.buildup.ui.Property.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.api.models.responsesAndData.property.propertyResponses.PropertyResponse
import com.example.buildup.R
import com.example.buildup.databinding.ItemPropertyBinding
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
            if(!property?.latestUpdate?.description.isNullOrBlank())
                if(property?.latestUpdate?.description?.length!!>15)
                    tvPropertyLatestUpdate.text=property.latestUpdate?.description!!.substring(0,15) + " " + ". . ."
                else
                    tvPropertyLatestUpdate.text=property.latestUpdate?.description
            if(property.eta!=null)
                tvPropertyETA.text= "ETA" +" : "+ property.eta?.value.toString()+" "+property.eta?.unit
            var progress=0
            if(property.completed!=null){
                progress=((property.completed!!)*100)/6
            }
            propertyProgress.progress=progress
            tvProgress.text= abs(progress).toString() + "%"
            root.setOnClickListener { onPropertyClicked(property.id) }
        }
    }
}