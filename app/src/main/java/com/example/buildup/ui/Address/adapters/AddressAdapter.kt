package com.example.buildup.ui.Address.adapters

import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.api.models.responsesAndData.address.Property
import com.example.buildup.R
import com.example.buildup.databinding.ItemAddressBinding

class AddressAdapter(val onEditAddressBtnClicked:(propertyId:String?)->Unit, val onDeleteAddressBtnClicked:(propertyId:String?)->Unit) : ListAdapter<Property, AddressAdapter.AddressViewHolder>(
    object : DiffUtil.ItemCallback<Property>(){
        override fun areItemsTheSame(oldItem: Property, newItem: Property): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Property, newItem: Property): Boolean {
            return oldItem.toString() == newItem.toString()
        }
    }
) {
    inner class AddressViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        return AddressViewHolder(
            parent.context.getSystemService(LayoutInflater::class.java).inflate(
//                R.layout.list_item_property,
                R.layout.item_address,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        var bind=ItemAddressBinding.bind(holder.itemView).apply {
            val addresss=getItem(position)

            if(addresss.type=="work"){
                homeIcon.setImageResource(R.drawable.ic_icon_work_type)
                homeText.setImageResource(R.drawable.ic_work_type_new)
            }
            if(addresss.isUnderConstruction){
                btnDeleteAddress.visibility=View.GONE
                btnEditAddress.visibility=View.GONE
            }
            tvClientName.text= addresss.propertyName
            tvShippingAddress.text=addresss.address.houseNo + "," + addresss.address.colony + "," + addresss.address.city + "," + addresss.address.state + " - " + addresss.address.pincode
            tvClientMobileNumber.text=addresss.mobileNo.toString()

            btnEditAddress.setOnClickListener{ onEditAddressBtnClicked(addresss.id)}
            btnDeleteAddress.setOnClickListener{ onDeleteAddressBtnClicked(addresss.id)}

        }
    }

}