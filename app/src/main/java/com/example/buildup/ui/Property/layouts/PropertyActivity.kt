package com.example.buildup.ui.Property.layouts

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.baoyachi.stepview.bean.StepBean
import com.example.buildup.AuthViewModel
import com.example.buildup.R
import com.example.buildup.databinding.ActivityUpdatesBinding
import com.example.buildup.databinding.ActivityPropertyBinding
import com.example.buildup.ui.ExpenditureActivity
import com.example.buildup.ui.Products.layouts.ProductCategoryActivity
import com.example.buildup.ui.Updates.UpdatesActivity
import com.example.buildup.ui.Updates.UpdatesAdapter
import com.example.buildup.ui.UpdatesBottomSheetFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.shuhart.stepview.StepView
import java.util.*


class PropertyActivity : AppCompatActivity() {

    var helpArray =
        arrayOf<String>("Layout", "Structure", "Fitting", "Flooring", "Touching", "Done")
    val stepsBeanList: MutableList<StepBean> = ArrayList()

    //    private lateinit var _binding:ActivityPropertyBinding
    private lateinit var _binding: ActivityPropertyBinding
    private lateinit var _bindingUpdates: ActivityUpdatesBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var updatesAdapter: UpdatesAdapter

    //    private lateinit var horizontalStepView: HorizontalStepView
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_property)

        _binding = ActivityPropertyBinding.inflate(layoutInflater)
        _bindingUpdates = ActivityUpdatesBinding.inflate(layoutInflater)
        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        updatesAdapter = UpdatesAdapter()

        _bindingUpdates.updatesRecyclerView.layoutManager = LinearLayoutManager(this)
        _bindingUpdates.updatesRecyclerView.adapter = updatesAdapter
        _bindingUpdates.updatesRecyclerView.isNestedScrollingEnabled = true

        setContentView(_binding?.root)



        if (getSupportActionBar() != null) {
            getSupportActionBar()?.hide();
        }

//        setContentView(_bindingUpdates?.root)

        val propertyId: String? = intent.getStringExtra("propertyId")

        supportFragmentManager.beginTransaction().replace(R.id.updateBottomFrame,UpdatesBottomSheetFragment()).commit()

        //PERSISTENT BOTTOM SHEET
        BottomSheetBehavior.from(_binding.updateBottomFrame).apply {
            peekHeight = 350
            this.state = BottomSheetBehavior.STATE_COLLAPSED
//            setPeekHeight(800)
        }


//        getUpdates(propertyId!!)
//        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
//
//        bottomSheetBehavior.addBottomSheetCallback(object :
//                BottomSheetBehavior.BottomSheetCallback(){
//
//            override fun onSlide(bottomSheet: View, slideOffset: Float) {
////                Toast.makeText(this@PropertyActivity,"slided",Toast.LENGTH_SHORT).show()
////                getUpdates(propertyId!!)
////                updateUI()
//            }
//
//            override fun onStateChanged(bottomSheet: View, newState: Int) {
//                when (newState) {
////                    BottomSheetBehavior.STATE_COLLAPSED -> getUpdates(propertyId!!)
////                    BottomSheetBehavior.STATE_EXPANDED -> getUpdates(propertyId!!)
////                    BottomSheetBehavior.STATE_DRAGGING -> getUpdates(propertyId!!)
////                    BottomSheetBehavior.STATE_SETTLING -> getUpdates(propertyId!!)
////                    BottomSheetBehavior.STATE_HIDDEN -> getUpdates(propertyId!!)
////                    else -> getUpdates(propertyId!!)
//                }
//            }
//
//        })
//        horizontalStepView=findViewById<HorizontalStepView>(R.id.stepsView)


        //MODAL BOTTOM SHEET
        // Creating the new Fragment with the name passed in.
//        val fragment = MyDialogBottomSheet.newInstance(propertyId!!)
//        fragment.show(supportFragmentManager,"")
//        MyDialogBottomSheet().show(supportFragmentManager,"")


        authViewModel.getProperty(propertyId!!)

        authViewModel.respProperty.observe({ lifecycle }) {
            if (it?.success!!) {
                _binding.propertyName.text = it.property?.name
//                _binding.completedStatus.text="Completed Stage : ${it.property.completed.toString()}"
            } else {
                Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                Log.d("errorProperty", it.error.toString())
            }
        }

//        _binding.getUpdatesButton.setOnClickListener {
//            val intent=Intent(this, UpdatesActivity::class.java)
//            intent.putExtra("propertyId",propertyId)
//            startActivity(intent)
//        }

        _binding.expensesButton.setOnClickListener {
            val intent = Intent(
                this,
                ExpenditureActivity::class.java
            )
            intent.putExtra("propertyId", propertyId)
            startActivity(intent)
        }

        _binding.productsCategoryButton.setOnClickListener {
            val intent = Intent(
                this,
                ProductCategoryActivity::class.java
            )
            intent.putExtra("propertyId", propertyId)
            startActivity(intent)
        }

    }

    fun updateUI() {
        _bindingUpdates.updatesRecyclerView.layoutManager = LinearLayoutManager(this)
        _bindingUpdates.updatesRecyclerView.adapter = updatesAdapter
        updatesAdapter.notifyDataSetChanged()
    }

    fun getUpdates(propertyId: String) {
        authViewModel.getUpdates(propertyId)
        authViewModel.respUpdatesArray.observe({ lifecycle }) {
            if (it?.success!!) {
                Toast.makeText(this, "updates fetching successful", Toast.LENGTH_SHORT).show()
                updatesAdapter.submitList(it.updates)

            } else {
                Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                Log.d("errorUpdates", it.error.toString())
            }
        }
    }
}