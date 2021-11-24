package com.example.buildup.ui.Property.layouts

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.buildup.AuthViewModel
import com.example.buildup.R
import com.example.buildup.databinding.ActivityPropertyBinding
import com.example.buildup.ui.BottomNavigation.ProfileActivity
import com.example.buildup.ui.Expenditure.ExpenditureActivity
import com.example.buildup.ui.LoginSignup.loginSignupMobileGoogleHomePage.LoginSignupActivity
import com.example.buildup.ui.Products.layouts.ProductCategoryActivity
import com.example.buildup.ui.Updates.UpdatesBottomSheetFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior


class PropertyActivity : AppCompatActivity() {
    companion object {
        var PREFS_FILE_AUTH = "prefs_property"
        var PREFS_KEY_TOKEN = "propertyId"
    }

    private lateinit var _binding: ActivityPropertyBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var sharedPrefrences: SharedPreferences
//    private val face = ResourcesCompat.getFont(this, R.font.overpass_extrabold)

//    @RequiresApi(Build.VERSION_CODES.O)
//    private var face = resources.getFont(R.font.overpass_bold)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_property)

        _binding = ActivityPropertyBinding.inflate(layoutInflater)
        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        sharedPrefrences = getSharedPreferences(PREFS_FILE_AUTH, Context.MODE_PRIVATE)


        setContentView(_binding?.root)



        if (getSupportActionBar() != null) {
            getSupportActionBar()?.hide();
        }

        val propertyId: String? = intent.getStringExtra("propertyId")

        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val myFragment = UpdatesBottomSheetFragment()

        //PERSISTENT BOTTOM SHEET
        BottomSheetBehavior.from(_binding.updateBottomFrame).apply {
            peekHeight = 320
            this.state = BottomSheetBehavior.STATE_COLLAPSED
//            setPeekHeight(800)
        }
        val bundle = Bundle()
        bundle.putString("propertyId", propertyId)
        Log.d("propertyId",bundle.getString("propertyId")!!)
        myFragment.arguments = bundle

        fragmentTransaction.add(R.id.updateBottomFrame,
            UpdatesBottomSheetFragment()
        ).commit()


        authViewModel.getProperty(propertyId!!)

        authViewModel.respProperty.observe({ lifecycle }) {
            if (it?.property?.id != null && it.success!!) {
                it.property?.id.let { t->
                    sharedPrefrences.edit {
                        putString("propertyId",t)
                    }
                }
                _binding.propertyName.text = it.property?.name
                timeLineHandling(it.property?.completed)
//                _binding.completedStatus.text="Completed Stage : ${it.property.completed.toString()}"
            } else {
                Toast.makeText(this, it?.error, Toast.LENGTH_SHORT).show()
                Log.d("errorProperty", it?.error.toString())
            }
        }

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

        _binding.backBtn.setOnClickListener {
            finish()
        }
        _binding.btnProfile.setOnClickListener {
            val intent=Intent(this,ProfileActivity::class.java)
            startActivity(intent)
        }

    }

    fun timeLineHandling(completed:Int?){
        if(completed==null)
            return

        _binding.apply {
            var face = Typeface.createFromAsset(
                assets,
                "fonts/overpass_bold.ttf"
            )

           if(completed==1){
               completedEqualsOne(face)
           }
            else if(completed==2){
                completedEqualsTwo(face)

           }
           else if(completed==3){
               completedEqualsThree(face)

           }
            else if(completed==4){
               completedEqualsFour(face)
           }

           else if(completed==5){
               completedEqualsFive(face)
           }
           else if(completed==6){
               completedEqualsSix(face)
           }
           else if(completed==7){
               completedEqualsSeven(face)
           }

        }
    }

    fun completedEqualsOne(face:Typeface){
        _binding.apply {
            var face = Typeface.createFromAsset(
                assets,
                "fonts/overpass_bold.ttf"
            )
            ellipse1.setImageResource(R.drawable.ic_colored_ellipse)
            timelineText1.setTextColor(getColor(R.color.white))
            timeLineDesc1.typeface=face
            timeLineDesc1.setTextColor(getColor(R.color.timeline_blue))
        }
    }

    fun completedEqualsTwo(face:Typeface){
        _binding.apply {
            completedEqualsOne(face)
            line1.setBackgroundColor(getColor(R.color.timeline_blue))
            ellipse2.setImageResource(R.drawable.ic_colored_ellipse)
            timelineText2.setTextColor(getColor(R.color.white))
            timeLineDesc2.typeface=face
            timeLineDesc2.setTextColor(getColor(R.color.timeline_blue))


        }
    }
    fun completedEqualsThree(face:Typeface){
        _binding.apply {
            completedEqualsTwo(face)
            line2.setBackgroundColor(getColor(R.color.timeline_blue))
            ellipse3.setImageResource(R.drawable.ic_colored_ellipse)
            timelineText3.setTextColor(getColor(R.color.white))
            timeLineDesc3.typeface=face
            timeLineDesc3.setTextColor(getColor(R.color.timeline_blue))

        }
    }

    fun completedEqualsFour(face:Typeface){
        _binding.apply {
            completedEqualsThree(face)
            line3.setBackgroundColor(getColor(R.color.timeline_blue))
            ellipse4.setImageResource(R.drawable.ic_colored_ellipse)
            timelineText4.setTextColor(getColor(R.color.white))
            timeLineDesc4.typeface=face
            timeLineDesc4.setTextColor(getColor(R.color.timeline_blue))
        }
    }

    fun completedEqualsFive(face:Typeface){
        _binding.apply {
            completedEqualsFour(face)
            line4.setBackgroundColor(getColor(R.color.timeline_blue))
            ellipse5.setImageResource(R.drawable.ic_colored_ellipse)
            timelineText5.setTextColor(getColor(R.color.white))
            timeLineDesc5.typeface=face
            timeLineDesc5.setTextColor(getColor(R.color.timeline_blue))
        }
    }
    fun completedEqualsSix(face:Typeface){
        _binding.apply {
            completedEqualsFive(face)
            line5.setBackgroundColor(getColor(R.color.timeline_blue))
            ellipse6.setImageResource(R.drawable.ic_colored_ellipse)
            timelineText6.setTextColor(getColor(R.color.white))
            timeLineDesc6.typeface=face
            timeLineDesc6.setTextColor(getColor(R.color.timeline_blue))
        }
    }
    fun completedEqualsSeven(face:Typeface){
        _binding.apply {
            completedEqualsSix(face)
            line6.setBackgroundColor(getColor(R.color.timeline_blue))
            timeLineDesc7.typeface=face
            timeLineDesc7.setTextColor(getColor(R.color.timeline_blue))
        }
    }
}