package com.gisfy.unauthorizedlayouts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gisfy.unauthorizedlayouts.Authentication.LoginActivity;
import com.gisfy.unauthorizedlayouts.Util.MyPagerAdapter;

public class welcome extends AppCompatActivity {
    MyPagerAdapter adapterViewPager;
    ViewPager vpPager;

    Fragment frag[];
    FirstFragment FirstFragment;
    SecondFragment SecondFragment;
    ThirdFragment ThirdFragment;
    private Button btnSkip, btnNext;
    private PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
        prefManager = new PrefManager(this);
        btnSkip = (Button) findViewById(R.id.btn_skip);
        btnNext = (Button) findViewById(R.id.btn_next);
        frag = new Fragment[]{
                FirstFragment,
                SecondFragment,
                ThirdFragment};
        addBottomDots(0);
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
                if (current < frag.length) {
                    // move to next screen
                    vpPager.setCurrentItem(current);
                } else {
                    launchHomeScreen();
                }
            }
        });




        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }
    }
    private void addBottomDots(int currentPage) {


        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);




    }
    private int getItem(int i) {
        return vpPager.getCurrentItem() + i;
    }
    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(welcome.this, LoginActivity.class));
        finish();
    }
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);


            // changing the next button text 'NEXT' / 'GOT IT'
            if (position ==frag.length -1) {
                // last page. make button text to GOT IT
                btnNext.setText(getString(R.string.start));
                btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                btnNext.setText(getString(R.string.next));
                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };
}
