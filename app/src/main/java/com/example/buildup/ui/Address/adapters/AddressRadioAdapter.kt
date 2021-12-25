package com.example.buildup.ui.Address.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.api.models.responsesAndData.address.Property
import com.example.buildup.R
import com.example.buildup.TinyDB
import com.example.buildup.databinding.ItemAddressBinding
import com.example.buildup.databinding.ItemAddressRadioBinding

class AddressRadioAdapter(val onEditAddressBtnClicked:(propertyId:String?)->Unit, val onDeleteAddressBtnClicked:(propertyId:String?)->Unit, val onRadioBtnClicked:(propertyId:String?)->Unit): ListAdapter<Property, AddressRadioAdapter.AddressRadioViewHolder> (
    object : DiffUtil.ItemCallback<Property>(){
        override fun areItemsTheSame(oldItem: Property, newItem: Property): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Property, newItem: Property): Boolean {
            return oldItem.toString() == newItem.toString()
        }
    }
){

    var lastSelectedPosition=-1
    inner class AddressRadioViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddressRadioAdapter.AddressRadioViewHolder {
        return AddressRadioViewHolder(
            parent.context.getSystemService(LayoutInflater::class.java).inflate(
//                R.layout.list_item_property,
                R.layout.item_address_radio,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AddressRadioAdapter.AddressRadioViewHolder, position: Int) {
        var bind= ItemAddressRadioBinding.bind(holder.itemView).apply {
            val addresss=getItem(position)

//            var tinyDB=TinyDB(holder.itemView.context)
//            var propertyId=tinyDB.getString("propertyIdForCart")
//            Log.d("adapterDB",propertyId)
//            Log.d("adapterAPI",addresss.id)
//            if(propertyId==addresss.id)
//                btnRadio.isChecked=true

            btnRadio.isChecked=lastSelectedPosition == position
            Log.d("selected_property","test log")
            btnRadio.setOnClickListener {
                lastSelectedPosition = position
                notifyDataSetChanged()
                onRadioBtnClicked(addresss.id)
//                Log.d("selected_property",addresss.id)
            }
            if(addresss.type=="work"){
                homeIcon.setImageResource(R.drawable.ic_icon_work_type_new)
                homeText.setImageResource(R.drawable.ic_work_type_new)
            }
            if(addresss.isUnderConstruction){
                btnDeleteAddress.visibility=View.GONE
                btnEditAddress.visibility=View.GONE
            }
            tvClientName.text= addresss.propertyName
            tvShippingAddress.text=addresss.address.houseNo + ", " + addresss.address.colony + ", " + addresss.address.city + ", " + addresss.address.state + " - " + addresss.address.pincode
            tvClientMobileNumber.text=addresss.mobileNo.toString()

            btnEditAddress.setOnClickListener{ onEditAddressBtnClicked(addresss.id)}
            btnDeleteAddress.setOnClickListener{ onDeleteAddressBtnClicked(addresss.id)}

        }
    }

}