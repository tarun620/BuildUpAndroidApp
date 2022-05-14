package com.example.buildup.ui.Products.layouts

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.buildup.R

class MyCustomDialogClearFilters: DialogFragment() {
    private val TAG = "DialogFragment"

    interface OnInputListener {
        fun sendInput(input: String?)
    }

    var mOnInputListener: OnInputListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        getDialog()!!.getWindow()?.setBackgroundDrawableResource(R.drawable.round_corner);
        val view=inflater.inflate(R.layout.layout_alert_dialog_clear_filter, container, false)
        val btnYes=view.findViewById<TextView>(R.id.btn_yes)
        val btnNo=view.findViewById<TextView>(R.id.btn_no)

        btnYes.setOnClickListener {
//            Log.d("yes","yes is clicked")
            mOnInputListener?.sendInput("yes");
            dismiss();
        }
        btnNo.setOnClickListener {
            dismiss()
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)

    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            mOnInputListener = activity as OnInputListener
        }catch (e:ClassCastException ) {
            Log.e(TAG, "onAttach: ClassCastException: ")
        }
    }


}