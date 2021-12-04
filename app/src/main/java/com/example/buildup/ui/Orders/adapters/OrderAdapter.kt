package com.example.buildup.ui.Orders.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.api.models.responsesAndData.order.Order
import com.example.api.models.responsesAndData.products.productsEntities.Products
import com.example.buildup.R
import com.example.buildup.databinding.ItemOrderBinding
import com.example.buildup.extensions.newLoadImage
import com.example.buildup.extensions.timeStamp2

class OrderAdapter(val onOrderClicked:(orderId:String?)->Unit) : ListAdapter<Order, OrderAdapter.OrderViewHolder>(
    object : DiffUtil.ItemCallback<Order>(){
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.toString() == newItem.toString()
        }

    }
) {
    inner class OrderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    val map = mapOf(1 to "Ordered", 2 to "Shipped" , 3 to "Out For Delivery" , 4 to "Delivered")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder(
            parent.context.getSystemService(LayoutInflater::class.java).inflate(
                R.layout.item_order,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        var bind= ItemOrderBinding.bind(holder.itemView).apply {
            val order=getItem(position)

            tvOrderStatus.text= map[order.shipping.tracking.status.toInt()]
            tvOrderETA.timeStamp2=order.shipping.tracking.estimatedDelivery
            tvOrderETA.text="On" + " " + tvOrderETA.text.toString()
            ivProductImage.newLoadImage(order.product.id.image[0])
            tvBrandName.text=order.product.id.brand.name
            tvProductName.text=order.product.id.name
            tvProductPrice.text="₹" + " " + ((order.product.unitCost) * (order.product.quantity)).toString()
            tvProductMrp.text="₹" + " " + ((order.product.unitMrp) * (order.product.quantity)).toString()

            root.setOnClickListener {
                Log.d("orderId ",order.id)
                onOrderClicked(order.id)
            }
        }
    }
}