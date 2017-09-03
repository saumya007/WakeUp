package com.samorgs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.saumya.wakeup.R;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private Intromanager intromanager;
    private int layouts[];
    private TextView[] dots;
    private LinearLayout dotslayout;
    Button next,skip;
    private ViewPagerAdapter viewPagerAdapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intromanager=new Intromanager(this);

        if(!intromanager.Check())
        {
            intromanager.setFirst(false);
            Intent i =new Intent(MainActivity.this,Main2Activity.class);
            startActivity(i);
            finish();
        }
        if(Build.VERSION.SDK_INT>=21)
        {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_main);
        layouts= new int[]{R.layout.activity_intro_screen,R.layout.intro2,R.layout.intro3,R.layout.intro4,R.layout.intro5,R.layout.intro6};
        viewPager =(ViewPager)findViewById(R.id.view_pager);
        dotslayout=(LinearLayout)findViewById(R.id.layout_dots);
        skip=(Button)findViewById(R.id.btn_skip);
        next=(Button)findViewById(R.id.btn_next);
        AddButtonDots(0);
        ChangeStatusbarColor();
        viewPagerAdapter = new ViewPagerAdapter();
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(viewlistener);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intromanager.setFirst(false);
                Intent intent =new Intent(MainActivity.this,Main2Activity.class);
                startActivity(intent);
                finish();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = getItem(+1);
                if(current<layouts.length){
                    viewPager.setCurrentItem(current);
                }
                else{
                    intromanager.setFirst(false);
                    Intent intent =new Intent(MainActivity.this,Main2Activity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    private void AddButtonDots(int position){
        dots= new TextView[layouts.length];
        int [] ColorActive = getResources().getIntArray(R.array.dot_active);
        int [] ColorInactive = getResources().getIntArray(R.array.dot_inactive);
        dotslayout.removeAllViews();
        for(int i=0;i<dots.length;i++)
        {
            dots[i]=new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(ColorInactive[position]);
            dotslayout.addView(dots[i]);
        }
        if(dots.length>0)
            dots[position].setTextColor(ColorActive[position]);
    }
    private int getItem(int i){
        return viewPager.getCurrentItem()+i;
    }
    ViewPager.OnPageChangeListener viewlistener = new ViewPager.OnPageChangeListener(){
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            AddButtonDots(position);
            if(position==layouts.length-1){
                next.setText("PROCEED");
                skip.setVisibility(View.GONE);
            }
            else{
                next.setText("NEXT");
                skip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    private void ChangeStatusbarColor(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
        {
            Window window=getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
    public class ViewPagerAdapter extends PagerAdapter{

        private LayoutInflater layoutInflater;

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(layouts[position],container,false);
            container.addView(v);
            return v;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }
        public void destroyItem(ViewGroup container,int position,Object object){
            View v = (View)object;
            container.removeView(v);
        }
    }

}
