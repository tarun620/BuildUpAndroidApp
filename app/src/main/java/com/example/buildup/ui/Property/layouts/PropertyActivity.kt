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
import com.example.buildup.databinding.ActivityPropertyBinding
import com.example.buildup.databinding.ActivityUpdatesBinding
import com.example.buildup.ui.ExpenditureActivity
import com.example.buildup.ui.Products.layouts.ProductCategoryActivity
import com.example.buildup.ui.Updates.UpdatesActivity
import com.example.buildup.ui.Updates.UpdatesAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.shuhart.stepview.StepView


class PropertyActivity : AppCompatActivity() {

    var helpArray= arrayOf<String>("Layout","Structure","Fitting","Flooring","Touching","Done")
    val stepsBeanList: MutableList<StepBean> = ArrayList()
    private lateinit var _binding:ActivityPropertyBinding
    private lateinit var _bindingUpdates:ActivityUpdatesBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var updatesAdapter: UpdatesAdapter
//    private lateinit var horizontalStepView: HorizontalStepView
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_property)


        _binding= ActivityPropertyBinding.inflate(layoutInflater)
        _bindingUpdates=ActivityUpdatesBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)
        updatesAdapter= UpdatesAdapter()

        _bindingUpdates.updatesRecyclerView.layoutManager= LinearLayoutManager(this)
        _bindingUpdates.updatesRecyclerView.adapter=updatesAdapter
        _bindingUpdates.updatesRecyclerView.isNestedScrollingEnabled=true

        setContentView(_binding?.root)
//        setContentView(_bindingUpdates?.root)

        val propertyId:String?=intent.getStringExtra("propertyId")

                //PERSISTENT BOTTOM SHEET
        val bottomSheet = findViewById<ConstraintLayout>(R.id.activity_updates)

        getUpdates(propertyId!!)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)

        bottomSheetBehavior.addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback(){

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
//                Toast.makeText(this@PropertyActivity,"slided",Toast.LENGTH_SHORT).show()
//                getUpdates(propertyId!!)
//                updateUI()
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
//                    BottomSheetBehavior.STATE_COLLAPSED -> getUpdates(propertyId!!)
//                    BottomSheetBehavior.STATE_EXPANDED -> getUpdates(propertyId!!)
//                    BottomSheetBehavior.STATE_DRAGGING -> getUpdates(propertyId!!)
//                    BottomSheetBehavior.STATE_SETTLING -> getUpdates(propertyId!!)
//                    BottomSheetBehavior.STATE_HIDDEN -> getUpdates(propertyId!!)
//                    else -> getUpdates(propertyId!!)
                }
            }

        })
//        horizontalStepView=findViewById<HorizontalStepView>(R.id.stepsView)



        //MODAL BOTTOM SHEET
        // Creating the new Fragment with the name passed in.
//        val fragment = MyDialogBottomSheet.newInstance(propertyId!!)
//        fragment.show(supportFragmentManager,"")
//        MyDialogBottomSheet().show(supportFragmentManager,"")



        authViewModel.getProperty(propertyId!!)

        authViewModel.respProperty.observe({lifecycle}){
            if(it?.success!!){
                _binding.propertyName.text=it.property?.name
                setStepView(it.property?.completed)
//                _binding.completedStatus.text="Completed Stage : ${it.property.completed.toString()}"
            }
            else{
                Toast.makeText(this,it.error, Toast.LENGTH_SHORT).show()
                Log.d("errorProperty",it.error.toString())
            }
        }

        _binding.getUpdatesButton.setOnClickListener {
            val intent=Intent(this, UpdatesActivity::class.java)
            intent.putExtra("propertyId",propertyId)
            startActivity(intent)
        }

        _binding.expensesButton.setOnClickListener {
            val intent=Intent(this, ExpenditureActivity::class.java)
            intent.putExtra("propertyId",propertyId)
            startActivity(intent)
        }

        _binding.productsCategoryButton.setOnClickListener {
            val intent=Intent(this, ProductCategoryActivity::class.java)
            intent.putExtra("propertyId",propertyId)
            startActivity(intent)
        }

    }

    fun updateUI(){
        _bindingUpdates.updatesRecyclerView.layoutManager= LinearLayoutManager(this)
        _bindingUpdates.updatesRecyclerView.adapter=updatesAdapter
        updatesAdapter.notifyDataSetChanged()
    }

    fun setStepView(completed:Int?){
//        helpStepView(completed)
//        horizontalStepView
//            .setStepViewTexts(stepsBeanList)
//            .setTextSize(12)
//            .setStepsViewIndicatorCompletedLineColor(getColor(R.color.stepView_green))
//            .setStepsViewIndicatorUnCompletedLineColor(getColor(R.color.stepView_grey))
//            .setStepViewComplectedTextColor(ContextCompat.getColor(this,R.color.stepView_grey))
//            .setStepViewUnComplectedTextColor(ContextCompat.getColor(this,R.color.stepView_grey))
//            .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(this,R.drawable.completed))
//            .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(this,R.drawable.default_icon))
//            .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(this,R.drawable.attention))



        _binding.stepsView.getState()
            .selectedTextColor(ContextCompat.getColor(this, R.color.stepView_green))
            .animationType(StepView.ANIMATION_NONE)
            .selectedCircleColor(ContextCompat.getColor(this, R.color.stepView_green))
            .selectedStepNumberColor(
                ContextCompat.getColor(
                    this,
                    R.color.stepView_green
                )
            ) // You should specify only stepsNumber or steps array of strings.
            // In case you specify both steps array is chosen.
            .steps(object : ArrayList<String?>() {
                init {
                    add("Layout")
                    add("Structure")
                    add("Fitting")
                    add("Flooring")
                    add("Touching")
                    add("Done")
                }
            }) // You should specify only steps number or steps array of strings.
            // In case you specify both steps array is chosen.
//            .stepsNumber(4)
            .animationDuration(resources.getInteger(android.R.integer.config_shortAnimTime))
//            .stepLineWidth(resources.getDimensionPixelSize(R.dimen.dp1))
//            .textSize(resources.getDimensionPixelSize(R.dimen.sp14))
//            .stepNumberTextSize(resources.getDimensionPixelSize(R.dimen.sp16))
//            .typeface(
//                ResourcesCompat.getFont(
//                    context,
//                    R.font.roboto_italic
//                )
//            ) // other state methods are equal to the corresponding xml attributes
            .commit()

        _binding.stepsView.go(completed!!,true)

    }

//    fun helpStepView(completed: Int){
//        for(i in 0..completed-1){
//            val stepBean=StepBean(helpArray[i],1)
//            stepsBeanList.add(stepBean)
//        }
//
//        for(i in completed..helpArray.size-1){
//            val stepBean=StepBean(helpArray[i],-1)
//            stepsBeanList.add(stepBean)
//        }
////        val stepBean0 = StepBean("Layout", 1)
////        val stepBean1 = StepBean("Structure", 1)
////        val stepBean2 = StepBean("Fitting", 1)
////        val stepBean3 = StepBean("Flooring", -1)
////        val stepBean4 = StepBean("Touching", -1)
////        val stepBean5 = StepBean("Done", -1)
////
////        stepsBeanList.add(stepBean0)
////        stepsBeanList.add(stepBean1)
////        stepsBeanList.add(stepBean2)
////        stepsBeanList.add(stepBean3)
////        stepsBeanList.add(stepBean4)
////        stepsBeanList.add(stepBean5)
//    }
    fun getUpdates(propertyId:String){
        authViewModel.getUpdates(propertyId)
        authViewModel.respUpdatesArray.observe({lifecycle}){
            if(it?.success!!){
                Toast.makeText(this,"updates fetching successful", Toast.LENGTH_SHORT).show()
                updatesAdapter.submitList(it.updates)

            }
            else{
                Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
                Log.d("errorUpdates",it.error.toString())
            }
        }
    }
}