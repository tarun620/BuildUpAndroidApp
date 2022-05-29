package com.example.buildup.ui.BottomNavigation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.buildup.R

class MyCustomDialogDelivery: DialogFragment() {
    private val TAG = "DialogFragment"

    interface OnInputListenerDelivery {
        fun sendInputDelivery(input: String?)
    }

    var mOnInputListenerDelivery: OnInputListenerDelivery? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getDialog()!!.getWindow()?.setBackgroundDrawableResource(R.drawable.round_corner);
        isCancelable=false
        val view=inflater.inflate(R.layout.layout_alert_delivery, container, false)
        val btnOk=view.findViewById<TextView>(R.id.btn_ok)
        btnOk.setOnClickListener {
            mOnInputListenerDelivery?.sendInputDelivery("yes");
            dismiss();
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
            mOnInputListenerDelivery = activity as OnInputListenerDelivery
        }catch (e:ClassCastException ) {
            Log.e(TAG, "onAttach: ClassCastException: ")
        }
    }
}