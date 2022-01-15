package com.example.buildup.ui.filter

import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.api.models.responsesAndData.brand.Brand
import com.example.api.models.responsesAndData.brand.IsBrandSelectedData
import com.example.buildup.R
import com.example.buildup.databinding.ItemFilterBrandBinding
import com.example.buildup.MyApplication

class FilterBrandAdapter(val onBrandClicked:(isBrandSelectedData:IsBrandSelectedData?)->Unit) : ListAdapter<Brand, FilterBrandAdapter.BrandViewHolder>(
    object : DiffUtil.ItemCallback<Brand>(){
        override fun areItemsTheSame(oldItem: Brand, newItem: Brand): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Brand, newItem: Brand): Boolean {
            return oldItem.toString() == newItem.toString()
        }

    }
) {
    inner class BrandViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandViewHolder {
        return BrandViewHolder(
            parent.context.getSystemService(LayoutInflater::class.java).inflate(
//                R.layout.list_item_property,
                R.layout.item_filter_brand,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BrandViewHolder, position: Int) {
        var checkedBrandList= MyApplication.getInstance().getList()
        var bind=ItemFilterBrandBinding.bind(holder.itemView).apply {
            val brand=getItem(position)

            tvBrandName.text=brand.name

            var isBrandSelected=false
            if(checkedBrandList.contains(brand.id)){
                isBrandSelected=!isBrandSelected
                tvBrandName.setTextColor(Color.parseColor("#000000"))
                tvBrandName.setTypeface(null,Typeface.BOLD)
                ivCheckbox.setImageResource(R.drawable.ic_check)

            }
            else{
                tvBrandName.setTextColor(Color.parseColor("#9D9C9C"))
                tvBrandName.setTypeface(null,Typeface.NORMAL)
                ivCheckbox.setImageResource(R.drawable.ic_check_unselected)
            }
            root.setOnClickListener {
                if(isBrandSelected){
                    tvBrandName.setTextColor(Color.parseColor("#9D9C9C"))
                    tvBrandName.setTypeface(null,Typeface.NORMAL)
                    ivCheckbox.setImageResource(R.drawable.ic_check_unselected)
                }
                else{
                    tvBrandName.setTextColor(Color.parseColor("#000000"))
                    tvBrandName.setTypeface(null,Typeface.BOLD)
                    ivCheckbox.setImageResource(R.drawable.ic_check)
                }
                isBrandSelected=!isBrandSelected
                onBrandClicked(IsBrandSelectedData(isBrandSelected,brand.id))
            }
        }
    }


}