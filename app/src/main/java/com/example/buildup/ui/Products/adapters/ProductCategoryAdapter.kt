package com.example.buildup.ui.Products.adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat.getColor
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.api.models.responsesAndData.products.productsEntities.ProductCategory
import com.example.api.models.responsesAndData.products.productsEntities.ProductCategoryIdData
import com.example.buildup.R
import com.example.buildup.databinding.ListItemProductCategoryBinding
import com.example.buildup.extensions.loadImage

class ProductCategoryAdapter(val onProductCategoryClicked:(productCategoryIdData:ProductCategoryIdData)->Unit): ListAdapter<ProductCategory, ProductCategoryAdapter.ProductCategoryViewHolder>(
        object : DiffUtil.ItemCallback<ProductCategory>(){
            override fun areItemsTheSame(oldItem: ProductCategory, newItem: ProductCategory): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ProductCategory, newItem: ProductCategory): Boolean {
                return oldItem.toString() == newItem.toString()
            }
        }
) {
    var row_index=0
    inner class ProductCategoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductCategoryViewHolder {
        return ProductCategoryViewHolder(
                parent.context.getSystemService(LayoutInflater::class.java).inflate(
                        R.layout.list_item_product_category,
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder: ProductCategoryViewHolder, position: Int) {
        var bind=ListItemProductCategoryBinding.bind(holder.itemView).apply {

            if(row_index==position){
                Log.d("color","reached in if")
                holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
            else
            {
                Log.d("color","reached in else")
                holder.itemView.setBackgroundColor(Color.parseColor("#F3F3F3"));
            }
            val productCategory=getItem(position)

//            ivProductCategoryImage.loadImage(productCategory.image)
            tvProductCategoryName.text=productCategory.name

            root.setOnClickListener {
                row_index=position
                notifyDataSetChanged()
                onProductCategoryClicked(ProductCategoryIdData(productCategory.id,productCategory.name))
            }
        }
    }

}