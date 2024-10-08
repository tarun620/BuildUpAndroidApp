package com.example.buildup.ui.Products.adapters

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.api.models.responsesAndData.products.productsEntities.ProductCategory
import com.example.buildup.R
import com.squareup.picasso.Picasso
import java.util.*


class ProductViewPagerAdapter(private val images: ArrayList<String>, val context: Context) :
    RecyclerView.Adapter<ProductViewPagerAdapter.ViewPagerHolder>() {

    inner class ViewPagerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById<ImageView>(R.id.productImage)

        fun bindView(image: String) {
//            Picasso.with(context)
//                .load(image)
//                .placeholder(R.drawable.product_placeholder)
//                .into(imgView);
            Glide.with(context).load(image).placeholder(R.drawable.product_placeholder).into(imageView)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product_view_pager, parent, false)
        return ViewPagerHolder(view)
    }

    override fun onBindViewHolder(holder: ViewPagerHolder, position: Int) {
        holder.bindView(images[position])
    }

    override fun getItemCount(): Int {
        return images.size
    }


}