package com.example.buildup.ui.Products.adapters

import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.api.models.responsesAndData.products.productsResponses.RecentProduct
import com.example.buildup.R
import com.example.buildup.databinding.ItemRecentViewedProductsBinding
import com.example.buildup.extensions.newLoadImage


class RecentViewedProductsAdapter(val onProductClicked:(productId:String?)->Unit) : ListAdapter<RecentProduct, RecentViewedProductsAdapter.RecentViewedProductsViewHolder>(
    object : DiffUtil.ItemCallback<RecentProduct>(){
        override fun areItemsTheSame(oldItem: RecentProduct, newItem: RecentProduct): Boolean {
            return oldItem == newItem

        }

        override fun areContentsTheSame(oldItem: RecentProduct, newItem: RecentProduct): Boolean {
            return oldItem.toString() == newItem.toString()
        }
    }
) {
    inner class RecentViewedProductsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecentViewedProductsViewHolder {
        return RecentViewedProductsViewHolder(
            parent.context.getSystemService(LayoutInflater::class.java).inflate(
                R.layout.item_recent_viewed_products,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecentViewedProductsViewHolder, position: Int) {
        var bind=ItemRecentViewedProductsBinding.bind(holder.itemView).apply{
            val product=getItem(position)

            if(product!=null){
                ivProductImage.newLoadImage(product.image[0])
//            ivProductImage.setImageResource(R.drawable.basin_mixer_1x)
                tvBrandName.text=product.brand.name
                tvProductName.text=product.name
                tvProductPrice.text="₹" + " " + product.amount.toString()
                tvProductMRP.text="₹" + " " + product.mrp.toString()
                tvProductMRP.paintFlags = tvProductMRP.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                Log.d("productId",product.id)
                root.setOnClickListener { onProductClicked(product.id) }
            }
        }
    }

}