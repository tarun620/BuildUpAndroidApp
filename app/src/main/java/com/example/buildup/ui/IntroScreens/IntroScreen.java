package com.example.buildup.ui.IntroScreens;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.api.models.responsesAndData.appData.IntroData;
import com.example.buildup.AuthViewModel;
import com.example.buildup.R;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.ArrayList;

public class IntroScreen extends AppCompatActivity {

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private int[] layouts;
    private ImageView btnSkip, btnNext, btnPrev, backArrow;
    private TextView textSkip,heading1,heading2,heading3,text1,text2,text3;
    private PrefManager prefManager;
    private WormDotsIndicator dotsIndicator;
    private Boolean doubleBackToExitPressedOnce = false;
    private ArrayList<String> introAppDataHeadingList=new ArrayList<>();
    private ArrayList<String> introAppDataTextList=new ArrayList<>();


    //protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }

//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        introAppDataHeadingList=(ArrayList<String>)getIntent().getSerializableExtra("introAppDataHeadingList");
        introAppDataTextList=(ArrayList<String>)getIntent().getSerializableExtra("introAppDataTextList");
        String hello=getIntent().getStringExtra("hello");

        Log.d("appListHeading1",introAppDataHeadingList.toString());
        Log.d("appListText1",introAppDataTextList.toString());

        setContentView(R.layout.intro_screen);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        btnSkip = (ImageView) findViewById(R.id.btn_skip);
        btnNext = (ImageView) findViewById(R.id.btn_next);
        btnPrev = (ImageView) findViewById(R.id.btn_prev);
        backArrow=(ImageView) findViewById(R.id.back_arrow);
        textSkip=(TextView) findViewById(R.id.text_skip);
        heading1=(TextView) findViewById(R.id.heading_screen_1);
        text1=(TextView) findViewById(R.id.text_screen_1);


        dotsIndicator = (WormDotsIndicator) findViewById(R.id.dots_indicator);

        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.intro_screen_1,
                R.layout.intro_screen_2,
                R.layout.intro_screen_3};

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        dotsIndicator.setViewPager(viewPager);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        btnPrev.setVisibility(View.GONE);
        backArrow.setVisibility(View.GONE);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else {
                    launchHomeScreen();
                }
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int current=getItem(-1);
                if(current<layouts.length && current>=0){
                    viewPager.setCurrentItem(current);
                }
            }
        });
    }


    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }
    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(IntroScreen.this, GetStartedActivity.class));
        finish();
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener(){

        @Override
        public void onPageSelected(int position) {
            if(position==0){
                btnPrev.setVisibility(View.GONE);
                backArrow.setVisibility(View.GONE);
            }
            else if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                btnSkip.setVisibility(View.GONE);
                textSkip.setVisibility(View.GONE);
                btnPrev.setVisibility(View.VISIBLE);
                backArrow.setVisibility(View.VISIBLE);

            } else {
                // still pages are left
                btnSkip.setVisibility(View.VISIBLE);
                textSkip.setVisibility(View.VISIBLE);
                btnPrev.setVisibility(View.VISIBLE);
                backArrow.setVisibility(View.VISIBLE);

            }

        }
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            TextView heading1 = (TextView) view.findViewById(R.id.heading_screen_1);
            TextView text1 = (TextView) view.findViewById(R.id.text_screen_1);

            switch (position){
                case 0:
                    heading1.setText(introAppDataHeadingList.get(0));
                    text1.setText(introAppDataTextList.get(0));
                    break;

                case 1:
                    heading1.setText(introAppDataHeadingList.get(1));
                    text1.setText(introAppDataTextList.get(1));
                    break;

                case 2:
                    heading1.setText(introAppDataHeadingList.get(2));
                    text1.setText(introAppDataTextList.get(2));
                    break;

            }
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    @Override
    public void onBackPressed() {
        if(doubleBackToExitPressedOnce){
            super.onBackPressed();
            //            return  //closes the current activity only
            this.finishAffinity();   //closes the entire application
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    //    override fun onBackPressed() {
//        if (doubleBackToExitPressedOnce) {
//            super.onBackPressed()
////            return  //closes the current activity only
//            this.finishAffinity();   //closes the entire application
//        }
//
//        this.doubleBackToExitPressedOnce = true
//        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
//
//        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
//    }
}
