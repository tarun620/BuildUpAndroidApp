package com.example.buildup.ui.Products.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes

class AutoCompleteQueryAdapter(context: Context, @LayoutRes private val layoutResource: Int, @IdRes private val textViewResourceId: Int = 0, private val queries: List<String?>?) : ArrayAdapter<String>(context,layoutResource,queries!!) {
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
////        return super.getView(position, convertView, parent)
//        val view = createViewFromResource(convertView, parent, layoutResource)
//
//        return bindData(getItem(position)!!, view)
//    }

//    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
//        return createViewFromResource(position, convertView, parent)
//    }
//    private fun createViewFromResource(convertView: View?, parent: ViewGroup, layoutResource: Int): TextView{
////        val view: TextView = convertView as TextView? ?: LayoutInflater.from(context).inflate(layoutResource, parent, false) as TextView
////        view.text = queries!![position]
////        return view
//
//        val context = parent.context
//        val view = convertView ?: LayoutInflater.from(context).inflate(layoutResource, parent, false)
//
//        return try {
//            if (textViewResourceId == 0) view as TextView
//            else {
//                view.findViewById(textViewResourceId) ?:
//                throw RuntimeException("Failed to find view with ID " +
//                        "${context.resources.getResourceName(textViewResourceId)} in item layout")
//            }
//        } catch (ex: ClassCastException){
//            Log.e("CustomArrayAdapter", "You must supply a resource ID for a TextView")
//            throw IllegalStateException(
//                "ArrayAdapter requires the resource ID to be a TextView", ex)
//        }
//    }
//    private fun bindData(value: String, view: TextView): TextView {
//        view.text = value
//        return view
//    }
}