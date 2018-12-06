package com.example.kensi.infosys1d.Menu;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.kensi.infosys1d.Product;
import com.example.kensi.infosys1d.R;

import java.util.List;

public class MenuPop extends AppCompatActivity {
    private WCViewPager viewPager;
    private SlideAdapter myadapter;

    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;

    private ImageButton removebutton;
    private RelativeLayout relativeLayout;
    //private LinearLayout linearLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.menu_pop);
        //linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);


        final DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .80), (int) (height * .80));

        int position = 0;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            position = extras.getInt("viewpager_position");
        }


        viewPager = (WCViewPager) findViewById(R.id.viewpager);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            viewPager.setClipToOutline(true);
        }

        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);

        myadapter = new SlideAdapter(this);
        viewPager.setAdapter(myadapter);

        viewPager.setCurrentItem(position);
        viewPager.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        removebutton = findViewById(R.id.removeButton);
        removebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        // DOTS

        dotscount = myadapter.getCount();
        dots = new ImageView[dotscount];

        for (int i = 0; i < dotscount; i++) {

            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);
        }

        dots[viewPager.getCurrentItem()].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {


            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }
}
