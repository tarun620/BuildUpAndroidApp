package com.example.buildup.ui.Products.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.api.models.responsesAndData.products.productsEntities.Products
import com.example.buildup.R
import com.example.buildup.databinding.ListItemProductBinding
import com.example.buildup.extensions.newLoadImage


class ProductAdapter(val onProductClicked:(productId:String?)->Unit): ListAdapter<Products, ProductAdapter.ProductViewHolder>(
        object : DiffUtil.ItemCallback<Products>(){
            override fun areItemsTheSame(oldItem: Products, newItem: Products): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Products, newItem: Products): Boolean {
                return oldItem.toString() == newItem.toString()
            }
        }
) {
    inner class ProductViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
                parent.context.getSystemService(LayoutInflater::class.java).inflate(
                        R.layout.list_item_product,
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        var bind=ListItemProductBinding.bind(holder.itemView).apply {
            val product=getItem(position)

            ivProductImage.newLoadImage(product.image[0])
//            ivProductImage.setImageResource(R.drawable.basin_mixer_1x)
            tvProductName.text=product.name
            tvProductPrice.text="₹" + " " + product.amount.toString()
            tvProductMRP.text="₹" + " " + product.mrp.toString()

            root.setOnClickListener { onProductClicked(product.id) }
        }
    }

}