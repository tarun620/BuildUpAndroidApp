package com.example.buildup.ui.Wishlist.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.api.models.responsesAndData.wishlist.Product
import com.example.api.models.responsesAndData.wishlist.WishlistData
import com.example.api.models.responsesAndData.wishlist.WishlistPositionData
import com.example.buildup.R
import com.example.buildup.databinding.ItemWishlistLayoutBinding
import com.example.buildup.extensions.newLoadImage

class WishlistAdapter(val onProductFromWishlistClicked:(productId:String?)->Unit, val removeProductFromWishlist:(wishlistPositionData:WishlistPositionData?)->Unit, val addProductToCartFromWishlist: (wishlistData:WishlistData) -> Unit): ListAdapter<Product, WishlistAdapter.WishlistViewHolder>(
    object : DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.toString() == newItem.toString()
        }
    }

) {
    inner class WishlistViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishlistViewHolder {
        return WishlistViewHolder(
            parent.context.getSystemService(LayoutInflater::class.java).inflate(
//                R.layout.list_item_property,
                R.layout.item_wishlist_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WishlistViewHolder, position: Int) {
        var bind=ItemWishlistLayoutBinding.bind(holder.itemView).apply {

            val wishlistedItem=getItem(position)

            ivProductImage.newLoadImage(wishlistedItem.image[0])
            tvProductBrand.text=wishlistedItem.brand.name
            tvProductName.text=wishlistedItem.name
            tvProductPrice.text="₹ " + wishlistedItem.amount.toString()
            tvProductMrp.text="₹ " + wishlistedItem.mrp.toString()
            tvProductMrp.paintFlags = tvProductMrp.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            if(wishlistedItem.inCart)
                btnAddToCart.text= "Go to Cart"

//            btnAddToCart.setOnClickListener {
//                if(wishlistedItem.)
//            }
            removeItemBtn.setOnClickListener { removeProductFromWishlist(WishlistPositionData(position,wishlistedItem.id)) }
            btnAddToCart.setOnClickListener { addProductToCartFromWishlist(WishlistData(wishlistedItem.inCart,wishlistedItem.id)) }
            root.setOnClickListener{onProductFromWishlistClicked(wishlistedItem.id)}

        }
    }


}