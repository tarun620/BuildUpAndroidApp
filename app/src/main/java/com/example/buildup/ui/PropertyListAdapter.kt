package com.example.buildup.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.api.models.responses.property.PropertyResponse
import com.example.buildup.R

class PropertyListAdapter(val context: Context,val properties: List<PropertyResponse>,val onPropertyClicked:(propertyId:String?)->Unit) : RecyclerView.Adapter<PropertyListAdapter.PropertyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.list_item_property, parent, false)
        return PropertyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return properties.size
    }

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        val property = properties[position]
        holder.propertyName.text = property.name
        holder.propertyDesc.text = property.latestUpdate?.description
        holder.propertyEta.text = property.eta?.value.toString() + " " + property.eta?.unit.toString()


    }

    inner class PropertyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var propertyName = itemView.findViewById<TextView>(R.id.tv_propertyName)
        var propertyDesc = itemView.findViewById<TextView>(R.id.tv_propertydesc)
        var propertyEta = itemView.findViewById<TextView>(R.id.tv_eta)


        init {
            itemView.setOnClickListener {
                val propertyId = properties[adapterPosition].id
                onPropertyClicked(propertyId)
//                Toast.makeText(itemView.context, "this is ${propertyId}", Toast.LENGTH_SHORT).show()
            }


        }
    }
}