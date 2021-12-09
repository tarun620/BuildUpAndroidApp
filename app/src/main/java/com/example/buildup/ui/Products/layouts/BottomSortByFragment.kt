package com.example.buildup.ui.Products.layouts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.buildup.R
import com.example.buildup.databinding.FragmentBottomSortByBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class   BottomSortByFragment : BottomSheetDialogFragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_bottom_sort_by, container, false)
    }


}