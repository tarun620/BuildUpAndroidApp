package com.example.buildup.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.api.models.responses.expenditure.Expenditure
import com.example.api.models.responses.updates.Update
import com.example.buildup.R
import com.example.buildup.databinding.ListItemExpenditureBinding
import com.example.buildup.extensions.timeStamp
import kotlin.math.exp

class ExpenditureAdapter : ListAdapter<Expenditure, ExpenditureAdapter.ExpenditureViewHolder>(
        object : DiffUtil.ItemCallback<Expenditure>(){
            override fun areItemsTheSame(oldItem: Expenditure, newItem: Expenditure): Boolean {
                return oldItem==newItem
            }

            override fun areContentsTheSame(oldItem: Expenditure, newItem: Expenditure): Boolean {
                return oldItem.toString() == newItem.toString()
            }
        }
) {

    inner class ExpenditureViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenditureViewHolder {
        return ExpenditureViewHolder(
                parent.context.getSystemService(LayoutInflater::class.java).inflate(
                        R.layout.list_item_expenditure,
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder: ExpenditureViewHolder, position: Int) {
        var bind=ListItemExpenditureBinding.bind(holder.itemView).apply {
            val expenditure=getItem(position)

            if(expenditure.fromCustomer){
                tvExpenditureDesc.text="Payment done by customer."
            }
            if(!expenditure.fromCustomer){
                if(expenditure.productId!=null)
                    tvExpenditureDesc.text="Normal Expenditure"
                else
                    tvExpenditureDesc.text="Custom Expenditure : ${expenditure.description}"
            }

            tvExpenditureAmount.text=expenditure.amount.toString()
            tvExpenditureDate.timeStamp=expenditure.createdAt
        }
    }
}