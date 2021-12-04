package com.example.buildup.ui.Products.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.api.models.responsesAndData.products.productsEntities.RecentySearchedQueryData
import com.example.buildup.R
import com.example.buildup.databinding.ItemRecentSearchedProductsBinding

class RecentSearchedAdapter(val onItemClicked:(searchQuery:String?)->Unit) : ListAdapter<RecentySearchedQueryData,RecentSearchedAdapter.RecentSearchedViewHolder> (
    object : DiffUtil.ItemCallback<RecentySearchedQueryData>(){
        override fun areItemsTheSame(
            oldItem: RecentySearchedQueryData,
            newItem: RecentySearchedQueryData
        ): Boolean {
            return oldItem == newItem

        }

        override fun areContentsTheSame(
            oldItem: RecentySearchedQueryData,
            newItem: RecentySearchedQueryData
        ): Boolean {
            return oldItem.toString() == newItem.toString()
        }

    }
){
    inner class RecentSearchedViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentSearchedViewHolder {
        return RecentSearchedViewHolder(
            parent.context.getSystemService(LayoutInflater::class.java).inflate(
                R.layout.item_recent_searched_products,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecentSearchedViewHolder, position: Int) {
        var bind=ItemRecentSearchedProductsBinding.bind(holder.itemView).apply {
            val searchQuery=getItem(position)

            title.text=searchQuery.searchQuery

            root.setOnClickListener { onItemClicked(searchQuery.searchQuery) }
        }
    }


}