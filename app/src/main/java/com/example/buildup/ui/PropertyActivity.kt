package com.example.buildup.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.baoyachi.stepview.HorizontalStepView
import com.baoyachi.stepview.bean.StepBean
import com.example.buildup.AuthViewModel
import com.example.buildup.R
import com.example.buildup.databinding.ActivityPropertyBinding
import com.shuhart.stepview.StepView


class PropertyActivity : AppCompatActivity() {

    var helpArray= arrayOf<String>("Layout","Structure","Fitting","Flooring","Touching","Done")
    val stepsBeanList: MutableList<StepBean> = ArrayList()
    private lateinit var _binding:ActivityPropertyBinding
    private lateinit var authViewModel: AuthViewModel
//    private lateinit var horizontalStepView: HorizontalStepView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_property)


        _binding= ActivityPropertyBinding.inflate(layoutInflater)
        authViewModel= ViewModelProvider(this).get(AuthViewModel::class.java)

        setContentView(_binding?.root)

//        horizontalStepView=findViewById<HorizontalStepView>(R.id.stepsView)
        val propertyId:String?=intent.getStringExtra("propertyId")


        authViewModel.getProperty(propertyId!!)

        authViewModel.respProperty.observe({lifecycle}){
            if(it?.success!!){
                _binding.propertyName.text=it.property.name
                setStepView(it.property.completed)
//                _binding.completedStatus.text="Completed Stage : ${it.property.completed.toString()}"
            }
        }

        _binding.getUpdatesButton.setOnClickListener {
            val intent=Intent(this,UpdatesActivity::class.java)
            intent.putExtra("propertyId",propertyId)
            startActivity(intent)
        }

        _binding.expensesButton.setOnClickListener {
            val intent=Intent(this,ExpenditureActivity::class.java)
            intent.putExtra("propertyId",propertyId)
            startActivity(intent)
        }

        _binding.productsCategoryButton.setOnClickListener {
            val intent=Intent(this,ProductCategoryActivity::class.java)
            intent.putExtra("propertyId",propertyId)
            startActivity(intent)
        }

    }

    fun setStepView(completed:Int){
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

        _binding.stepsView.go(completed,true)

    }

    fun helpStepView(completed: Int){
        for(i in 0..completed-1){
            val stepBean=StepBean(helpArray[i],1)
            stepsBeanList.add(stepBean)
        }

        for(i in completed..helpArray.size-1){
            val stepBean=StepBean(helpArray[i],-1)
            stepsBeanList.add(stepBean)
        }
//        val stepBean0 = StepBean("Layout", 1)
//        val stepBean1 = StepBean("Structure", 1)
//        val stepBean2 = StepBean("Fitting", 1)
//        val stepBean3 = StepBean("Flooring", -1)
//        val stepBean4 = StepBean("Touching", -1)
//        val stepBean5 = StepBean("Done", -1)
//
//        stepsBeanList.add(stepBean0)
//        stepsBeanList.add(stepBean1)
//        stepsBeanList.add(stepBean2)
//        stepsBeanList.add(stepBean3)
//        stepsBeanList.add(stepBean4)
//        stepsBeanList.add(stepBean5)
    }
}