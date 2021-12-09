package com.example.buildup.ui.Products.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.api.models.responsesAndData.products.productsEntities.Products
import com.example.api.models.responsesAndData.wishlist.IsWishlistedData
import com.example.buildup.R
import com.example.buildup.databinding.ListItemProductBinding
import com.example.buildup.extensions.newLoadImage


class ProductAdapter(val onProductClicked:(productId:String?)->Unit, val onWishlistClicked:(isWishlistedData : IsWishlistedData)->Unit): ListAdapter<Products, ProductAdapter.ProductViewHolder>(
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

            if(product!=null){
                ivProductImage.newLoadImage(product.image[0])
//            ivProductImage.setImageResource(R.drawable.basin_mixer_1x)
                tvBrandName.text=product.brand.name
                tvProductName.text=product.name
                tvProductPrice.text="₹" + " " + product.amount.toString()
                tvProductMRP.text="₹" + " " + product.mrp.toString()
                tvProductMRP.paintFlags = tvProductMRP.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                var isWishlisted=product.isWishlisted!!
                if(isWishlisted){
                    btnWishlist.setImageResource(R.drawable.ic_icon_wishlisted_new)
                }
                btnWishlist.setOnClickListener {
                    if(isWishlisted){
                        btnWishlist.setImageResource(R.drawable.ic_save_wishlist)
                    }
                    else{
                        btnWishlist.setImageResource(R.drawable.ic_icon_wishlisted_new)
                    }
                    onWishlistClicked(IsWishlistedData(isWishlisted,product.id))
                    isWishlisted=!isWishlisted
                }

                root.setOnClickListener { onProductClicked(product.id) }
            }
        }
    }

}