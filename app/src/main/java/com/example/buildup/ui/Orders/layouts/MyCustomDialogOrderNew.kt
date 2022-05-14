package com.example.buildup.ui.Orders.layouts

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.buildup.R

class MyCustomDialogOrderNew: DialogFragment() {
    private val TAG = "DialogFragment"

    interface OnInputListenerNew {
        fun sendInputNew(input: String?)
    }

    var mOnInputListener: OnInputListenerNew? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getDialog()!!.getWindow()?.setBackgroundDrawableResource(R.drawable.round_corner);
        val view=inflater.inflate(R.layout.layout_alert_cancel_all_orders_new, container, false)
        val btnYes=view.findViewById<TextView>(R.id.btn_yes)
        val btnNo=view.findViewById<TextView>(R.id.btn_no)
        btnYes.setOnClickListener {
//            Log.d("yes","yes is clicked")
            mOnInputListener?.sendInputNew("yes");
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
            mOnInputListener = activity as OnInputListenerNew
        }catch (e:ClassCastException ) {
            Log.e(TAG, "onAttach: ClassCastException: ")
        }
    }
}