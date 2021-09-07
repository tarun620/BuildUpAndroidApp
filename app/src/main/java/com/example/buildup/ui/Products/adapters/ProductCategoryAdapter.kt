package com.example.buildup.ui.Products.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.api.models.responses.products.productsEntities.ProductCategory
import com.example.buildup.R
import com.example.buildup.databinding.ListItemProductCategoryBinding
import com.example.buildup.extensions.loadImage

class ProductCategoryAdapter(val onProductCategoryClicked:(productCategoryId:String?)->Unit): ListAdapter<ProductCategory, ProductCategoryAdapter.ProductCategoryViewHolder>(
        object : DiffUtil.ItemCallback<ProductCategory>(){
            override fun areItemsTheSame(oldItem: ProductCategory, newItem: ProductCategory): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ProductCategory, newItem: ProductCategory): Boolean {
                return oldItem.toString() == newItem.toString()
            }
        }
) {
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

            val productCategory=getItem(position)

            ivProductCategoryImage.loadImage(productCategory.image)
            tvProductCategoryName.text=productCategory.name

            root.setOnClickListener { onProductCategoryClicked(productCategory.id) }
        }
    }

}