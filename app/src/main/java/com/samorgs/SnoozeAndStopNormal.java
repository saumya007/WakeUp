package com.samorgs;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.saumya.wakeup.R;

public class SnoozeAndStopNormal extends AppCompatActivity {

    private int mActivePointerId =1;
    private float mlasttouchX;
    private float mlasttouchY;
    private float mPosx=0;
    private float mPosy=0;
    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor=1.f;
    private int xDelta,yDelta;
    private ImageView decider;
    private ImageView snooze;
    private  ImageView stop;
    private RelativeLayout rl;
    Uri uri;
    private int  stoporsnooze=0;// 1 for stop and 2 forsnooze
    // public SnoozeAndStopAnimation(){
    //mScaleDetector = new ScaleGestureDetector(getApplicationContext(),new ScaleListener());
    //}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snooze_and_stop_normal);
        decider = (ImageView)findViewById(R.id.imageViewdecider);
        snooze = (ImageView)findViewById(R.id.imageViewsnooze);
        stop = (ImageView)findViewById(R.id.imageViewstop);
        View v = (View)findViewById(R.id.resizableback);
        rl =(RelativeLayout)findViewById(R.id.Relative);
        final GestureDetector gdt = new GestureDetector(new GestureListener());
        rl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gdt.onTouchEvent(event);
                return true;
            }
        });
        snooze.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        stop.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        ObjectAnimator om = ObjectAnimator.ofPropertyValuesHolder(v, PropertyValuesHolder.ofFloat("scaleX",1.5f),PropertyValuesHolder.ofFloat("scaleY",1.5f));
        om.setDuration(310);
        om.setRepeatCount(ObjectAnimator.INFINITE);
        om.setRepeatMode(ObjectAnimator.REVERSE);
        om.start();
        Animation fadeOut = new AlphaAnimation(1,0);
        fadeOut.setDuration(1000);
        fadeOut.setRepeatCount(Animation.INFINITE);
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(fadeOut);
        v.startAnimation(fadeOut);
    }
    private static final int SWIPE_MIN_DISTANCE=120;
    private static final int SWIPE_THRESHOLD_VELOCITY=200;
    private class GestureListener extends GestureDetector.SimpleOnGestureListener{
        private ObjectAnimator om=ObjectAnimator.ofPropertyValuesHolder(rl, PropertyValuesHolder.ofFloat("scaleX", 0.0f), PropertyValuesHolder.ofFloat("scaleY", 0.0f)),omsnooze=ObjectAnimator.ofPropertyValuesHolder(snooze, PropertyValuesHolder.ofFloat("scaleX", 2f), PropertyValuesHolder.ofFloat("scaleY", 2f)),omstop=ObjectAnimator.ofPropertyValuesHolder(stop, PropertyValuesHolder.ofFloat("scaleX",2f),PropertyValuesHolder.ofFloat("scaleY",2f));
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(e1.getX()-e2.getX()>SWIPE_MIN_DISTANCE && Math.abs(velocityX)>SWIPE_THRESHOLD_VELOCITY){
                //rtl
                rl.setTranslationX(stop.getX());
                stoporsnooze=2;
                //Intent ints = getIntent();
                //ints.putExtra("SnoozeOrStop",stoporsnooze);
                //setResult(RESULT_OK);
                om.setDuration(310);
                om.start();
                om.setAutoCancel(true);
                omsnooze.setDuration(310);
                omsnooze.start();
                omsnooze.setAutoCancel(true);
                //Toast.makeText(getApplicationContext(), "Right TO Left", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(SnoozeAndStopNormal.this,SnoozeNonCrazy.class);
                startActivity(i);
                finish();
            }
            else if (e2.getX()-e1.getX()>SWIPE_MIN_DISTANCE && Math.abs(velocityX)>SWIPE_THRESHOLD_VELOCITY){
                //ltr
                stoporsnooze=1;
                //Intent ints = getIntent();
                //ints.putExtra("SnoozeOrStop",stoporsnooze);
                //setResult(RESULT_OK);
                rl.setTranslationX(snooze.getX()+180);
                // = ObjectAnimator.ofPropertyValuesHolder(decider, PropertyValuesHolder.ofFloat("scaleX",0.5f),PropertyValuesHolder.ofFloat("scaleY",0.5f));
                om.setDuration(310);
                om.start();
                om.setAutoCancel(true);
                omstop.setDuration(310);
                omstop.start();
                omstop.setAutoCancel(true);
                Intent i = new Intent(SnoozeAndStopNormal.this,DismissNonCrazy.class);
                startActivity(i);
                finish();
                //Toast.makeText(getApplicationContext(), "Right TO Left", Toast.LENGTH_SHORT).show();
                // Toast.makeText(getApplicationContext(), "Right from Left", Toast.LENGTH_SHORT).show();
            }
            if(e1.getY()-e2.getY()>SWIPE_MIN_DISTANCE && Math.abs(velocityY)>SWIPE_THRESHOLD_VELOCITY){
                //btt
            }
            else if(e2.getY()-e1.getY()>SWIPE_MIN_DISTANCE && Math.abs(velocityY)>SWIPE_THRESHOLD_VELOCITY){
                //ttb
            }
            return false;
        }
    }
}
