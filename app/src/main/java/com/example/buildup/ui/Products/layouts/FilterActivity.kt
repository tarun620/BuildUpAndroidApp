package com.example.buildup.ui.Products.layouts

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.api.models.responsesAndData.brand.Brand
import com.example.api.models.responsesAndData.brand.IsBrandSelectedData
import com.example.buildup.AuthViewModel
import com.example.buildup.databinding.ActivityFilterBinding
import com.example.buildup.ui.FilterBrandAdapter
import com.example.buildup.ui.MyApplication
import com.google.android.material.slider.LabelFormatter
import com.google.android.material.slider.RangeSlider
import java.util.*

class FilterActivity : AppCompatActivity() {
    private lateinit var _binding:ActivityFilterBinding
    private lateinit var  authViewModel:AuthViewModel
    private lateinit var filterBrandAdapter:FilterBrandAdapter
    private var brandList= mutableListOf<Brand>()
    private var tempBrandList=mutableListOf<Brand>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityFilterBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)
        setContentView(_binding.root)

        filterBrandAdapter= FilterBrandAdapter{ onBrandClicked(it) }

        _binding.listRecyclerView.layoutManager= LinearLayoutManager(this)
        _binding.listRecyclerView.adapter=filterBrandAdapter


        _binding.apply{
            brandFilter.setBackgroundColor(Color.parseColor("#FFFFFF"))
            priceRangeFilter.setBackgroundColor(Color.parseColor("#F6F7FB"))
            brandFilter.setTypeface(null,Typeface.BOLD)
            priceRangeFilter.setTypeface(null,Typeface.NORMAL)
            nestedRecyclerView1.visibility= View.VISIBLE
            nestedRecyclerView2.visibility=View.GONE
//
            brandFilter.setOnClickListener {
                brandFilter.setBackgroundColor(Color.parseColor("#FFFFFF"))
                priceRangeFilter.setBackgroundColor(Color.parseColor("#F6F7FB"))
                brandFilter.setTypeface(null,Typeface.BOLD)
                priceRangeFilter.setTypeface(null,Typeface.NORMAL)
                nestedRecyclerView1.visibility= View.VISIBLE
                nestedRecyclerView2.visibility=View.GONE
            }
//
            priceRangeFilter.setOnClickListener {
                priceRangeFilter.setBackgroundColor(Color.parseColor("#FFFFFF"))
                brandFilter.setBackgroundColor(Color.parseColor("#F6F7FB"))
                priceRangeFilter.setTypeface(null,Typeface.BOLD)
                brandFilter.setTypeface(null,Typeface.NORMAL)
                nestedRecyclerView2.visibility= View.VISIBLE
                nestedRecyclerView1.visibility=View.GONE
            }
            clearAllBtn.setOnClickListener {
                val builder = AlertDialog.Builder(this@FilterActivity)
                //set title for alert dialog
                builder.setTitle("Clear All Filters")
                //set message for alert dialog
                builder.setMessage("Do you want to clear all the filters applied?")
                builder.setIcon(android.R.drawable.ic_dialog_alert)

                //performing positive action
                builder.setPositiveButton("Yes"){dialogInterface, which ->
                    (application as MyApplication).clearList()

//                    filterBrandAdapter= FilterBrandAdapter{ onBrandClicked(it) }
//
//                    _binding.listRecyclerView.layoutManager= LinearLayoutManager(this@FilterActivity)
//                    _binding.listRecyclerView.adapter=filterBrandAdapter
//                    getBrands()
                    filterBrandAdapter.notifyDataSetChanged()
                    



                    (application as MyApplication).clearFromRange()
                    (application as MyApplication).clearToRange()
                    tvPriceRangeFrom.text="₹ 100" + " - "
                    tvPriceRangeTo.text="₹ 5000"

                    rangeSlider.setValues(100F,5000F)


                }
                //performing cancel action
                builder.setNeutralButton("Cancel"){dialogInterface , which ->

                }
                //performing negative action
                builder.setNegativeButton("No"){dialogInterface, which ->
                }
                // Create the AlertDialog
                val alertDialog: AlertDialog = builder.create()
                // Set other dialog properties
                alertDialog.setCancelable(false)
                alertDialog.show()
            }

            applyBtn.setOnClickListener {
//                startActivity(Intent(this@FilterActivity,ProductsActivity::class.java))
                finish()

            }
            closeBtn.setOnClickListener {
                finish()
            }

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                   return false
                }
                override fun onQueryTextChange(newText: String): Boolean {
                    tempBrandList.clear()
                    val searchText=newText.toLowerCase(Locale.getDefault())
                    if(searchText.isNotEmpty()){
                        brandList.forEach {
                            if(it.name.toLowerCase(Locale.getDefault()).contains(searchText)){
                                tempBrandList.add(it)
                            }
                        }
                        filterBrandAdapter.notifyDataSetChanged()
                    }
                    else{
                        tempBrandList.clear()
                        tempBrandList.addAll(brandList)
                        filterBrandAdapter.notifyDataSetChanged()
                    }
                    return false
                }
            })

//            rangeSlider.stepSize=100F
            if((application as MyApplication).getFromRange()!=null){
//                rangeSlider.values[0]=(application as MyApplication).getFromRange()?.toFloat()
                rangeSlider.setValues((application as MyApplication).getFromRange()?.toFloat(),(application as MyApplication).getToRange()?.toFloat())
//                rangeSlider.valueFrom=(application as MyApplication).getFromRange().toFloat()
                tvPriceRangeFrom.text="₹ " + (application as MyApplication).getFromRange()?.toFloat()?.toInt() + " - "
            }
//            else{
//                rangeSlider.valueFrom= 100F
//            }

            if((application as MyApplication).getToRange()!=null){
                rangeSlider.setValues((application as MyApplication).getFromRange()?.toFloat(),(application as MyApplication).getToRange()?.toFloat())
//                rangeSlider.values[1]=(application as MyApplication).getToRange()?.toFloat()
//                rangeSlider.valueTo=(application as MyApplication).getToRange().toFloat()
                tvPriceRangeTo.text="₹ " + (application as MyApplication).getToRange()?.toFloat()?.toInt()
            }
//            else{
//                rangeSlider.valueTo=10000F
//            }

            rangeSlider.labelBehavior= LabelFormatter.LABEL_GONE
            rangeSlider.addOnSliderTouchListener(object:RangeSlider.OnSliderTouchListener{
                override fun onStartTrackingTouch(slider: RangeSlider) {
                    tvPriceRangeFrom.text="₹ " + slider.values.get(0).toInt().toString() + " - "
                    tvPriceRangeTo.text="₹ " + slider.values.get(1).toInt().toString()
                    (application as MyApplication).setFromRange(slider.values[0].toInt())
                    (application as MyApplication).setToRange(slider.values[1].toInt())

                }

                override fun onStopTrackingTouch(slider: RangeSlider) {
                    tvPriceRangeFrom.text="₹ " + slider.values.get(0).toInt().toString() + " - "
                    tvPriceRangeTo.text="₹ " + slider.values.get(1).toInt().toString()
                    (application as MyApplication).setFromRange(slider.values[0].toInt())
                    (application as MyApplication).setToRange(slider.values[1].toInt())
                }

            })

            rangeSlider.addOnChangeListener(object :RangeSlider.OnChangeListener{
                override fun onValueChange(slider: RangeSlider, value: Float, fromUser: Boolean) {
                    tvPriceRangeFrom.text="₹ " + slider.values.get(0).toInt().toString() + " - "
                    tvPriceRangeTo.text="₹ " + slider.values.get(1).toInt().toString()
                    (application as MyApplication).setFromRange(slider.values[0].toInt())
                    (application as MyApplication).setToRange(slider.values[1].toInt())
                }

            })
        }

        getBrands()

    }
    private fun getBrands(){
        authViewModel.getBrands(false,null)
        authViewModel.respGetBrands.observe({lifecycle}){
            if(it?.success!!){
//                filterBrandAdapter.submitList(it.brands)
                brandList= it.brands as MutableList<Brand>
                tempBrandList.addAll(brandList)
                filterBrandAdapter.submitList(tempBrandList)
            }
            else
                Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
        }
    }
    private fun onBrandClicked(isBrandSelectedData: IsBrandSelectedData?){
        if(isBrandSelectedData?.isBrandSelected!!){
            (application as MyApplication).addElement(isBrandSelectedData.brandId)
        }
        else{
            (application as MyApplication).removeElement(isBrandSelectedData.brandId)
        }
    }
}