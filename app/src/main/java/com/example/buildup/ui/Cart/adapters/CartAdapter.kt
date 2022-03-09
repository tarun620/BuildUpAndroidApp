package com.example.buildup.ui.Cart.adapters

import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.api.models.responsesAndData.cart.cartResponses.Item
import com.example.buildup.AuthViewModel
import com.example.buildup.R
import com.example.buildup.databinding.CartItemLayoutBinding
import com.example.buildup.extensions.newLoadImage
import com.example.buildup.ui.BottomNavigation.CartActivity
import kotlinx.coroutines.withContext

//private lateinit var authViewModel: AuthViewModel

class CartAdapter(val onProductFromCartClicked:(productId:String?)->Unit
                  , val increaseProductQuantity:(productId:String?)->Unit
                  , val decreaseProductQuantity:(productId:String?)->Unit
                  , val removeProductFromCart:(productId:String?)->Unit)
    : ListAdapter<Item, CartAdapter.CartViewHolder>(

    object : DiffUtil.ItemCallback<Item>(){
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.toString() == newItem.toString()
        }
    }
) {
//    private lateinit var authViewModel: AuthViewModel
//        ViewModelProviders.of(context as FragmentActivity?).get(
//            CartViewModel::class.java
//        )

    inner class CartViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        return CartViewHolder(
            parent.context.getSystemService(LayoutInflater::class.java).inflate(
//                R.layout.list_item_property,
                R.layout.cart_item_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
//        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)
        var bind=CartItemLayoutBinding.bind(holder.itemView).apply {

            val cartItem=getItem(position)
            var quantity: Int
            ivProductImage.newLoadImage(cartItem.product.image[0])
            tvProductBrand.text=cartItem.product.brand.name
            if(cartItem.product.name.length>25){
                tvProductName.text=cartItem.product.name.take(25) + ".."

            }
            else
                tvProductName.text=cartItem.product.name
            tvProductPrice.text="₹ " + cartItem.product.amount.toString()
            tvProductMRP.text="₹ " + cartItem.product.mrp.toString()
            tvProductMRP.paintFlags = tvProductMRP.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            tvQuantity.text=cartItem.quantity.toString()

//            increaseQuantityBtn.setOnClickListener {
//                quantity= (tvQuantity.text as String).toInt()
//                tvQuantity.text=(quantity+1).toString()
//                authViewModel.updateProductQuantityCart(cartItem.product.id,quantity+1)
//            }
//
//            decreaseQuantityBtn.setOnClickListener {
//                quantity= (tvQuantity.text as String).toInt()
//                if(quantity>1){
//                    tvQuantity.text=(quantity-1).toString()
//                    authViewModel.updateProductQuantityCart(cartItem.product.id,quantity-1)
//                }
//                else{
//                    Toast.makeText(holder.itemView.context,"Quantity cannot be less than 1, Please remove the item from cart using delete icon",Toast.LENGTH_SHORT).show()
//                }
//            }

//            removeBtn.setOnClickListener {
//                authViewModel.removeProductFromCart(cartItem.product.id)
//                authViewModel.
//            }

            increaseQuantityBtn.setOnClickListener{ increaseProductQuantity(cartItem.product.id) }
            decreaseQuantityBtn.setOnClickListener { decreaseProductQuantity(cartItem.product.id) }
            removeBtn.setOnClickListener { removeProductFromCart(cartItem.product.id) }
            root.setOnClickListener{ onProductFromCartClicked(cartItem.product.id) }
        }
    }


}