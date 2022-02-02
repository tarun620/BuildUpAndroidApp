package com.example.buildup.ui.Products.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.api.models.responsesAndData.products.productsEntities.ProductCategory
import com.example.api.models.responsesAndData.products.productsEntities.ProductCategoryIdData
import com.example.buildup.R
import com.example.buildup.databinding.ItemProductCategoryBinding
import com.example.buildup.extensions.loadCategoryImage
import com.example.buildup.extensions.newLoadImage

class ProductCategoryAdapterNew(val onProductCategoryClicked:(productCategoryIdData: ProductCategoryIdData)->Unit): ListAdapter<ProductCategory, ProductCategoryAdapterNew.ProductCategoryViewHolder> (
    object : DiffUtil.ItemCallback<ProductCategory>(){
        override fun areItemsTheSame(oldItem: ProductCategory, newItem: ProductCategory): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ProductCategory, newItem: ProductCategory): Boolean {
            return oldItem.toString() == newItem.toString()
        }
    }
){
    inner class ProductCategoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductCategoryViewHolder {
        return ProductCategoryViewHolder(
            parent.context.getSystemService(LayoutInflater::class.java).inflate(
                R.layout.item_product_category,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ProductCategoryViewHolder, position: Int) {
        var bind=ItemProductCategoryBinding.bind(holder.itemView).apply {

            val productCategory=getItem(position)
            ivCategoryImage.loadCategoryImage(productCategory.image) //TODO : handle if image is null
            tvCategoryName.text=productCategory.name

            root.setOnClickListener { onProductCategoryClicked(ProductCategoryIdData(productCategory.id,productCategory.name,productCategory.image!!)) }

        }
    }


}