package com.example.buildup.ui.Products.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.api.models.responses.products.productsEntities.ProductSubCategory
import com.example.buildup.R
import com.example.buildup.databinding.ListItemProductCategoryBinding
import com.example.buildup.extensions.loadImage

class ProductSubCategoryAdapter(val onProductSubCategoryClicked:(productSubCategoryId:String?)->Unit): ListAdapter<ProductSubCategory, ProductSubCategoryAdapter.ProductSubCategoryViewHolder>(
    object : DiffUtil.ItemCallback<ProductSubCategory>(){
        override fun areItemsTheSame(oldItem: ProductSubCategory, newItem: ProductSubCategory): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ProductSubCategory, newItem: ProductSubCategory): Boolean {
            return oldItem.toString() == newItem.toString()
        }
    }
) {
    inner class ProductSubCategoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductSubCategoryViewHolder {
        return ProductSubCategoryViewHolder(
            parent.context.getSystemService(LayoutInflater::class.java).inflate(
                R.layout.list_item_product_category,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ProductSubCategoryViewHolder, position: Int) {
        val bind=ListItemProductCategoryBinding.bind(holder.itemView).apply {
            val productSubCategory=getItem(position)

            ivProductCategoryImage.loadImage(productSubCategory.image)
            tvProductCategoryName.text=productSubCategory.name

            root.setOnClickListener { onProductSubCategoryClicked(productSubCategory.id) }
        }
    }
}