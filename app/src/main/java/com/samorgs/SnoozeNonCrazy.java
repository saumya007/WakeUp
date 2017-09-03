package com.samorgs;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;

import com.example.saumya.wakeup.R;

public class SnoozeNonCrazy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snooze_non_crazy);
        Intent intent = new Intent(SnoozeNonCrazy.this, AlarmRecieve.class);
        PendingIntent pending = PendingIntent.getBroadcast(getBaseContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long CurrentTimeInMillis = System.currentTimeMillis();
        long NextUpdateTimeInMillis = CurrentTimeInMillis + 10* DateUtils.MINUTE_IN_MILLIS;
        android.text.format.Time NextUpdateTime  = new android.text.format.Time();
        NextUpdateTime.set(NextUpdateTimeInMillis);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,NextUpdateTimeInMillis,pending);
        //Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
        Handler handler = new Handler();
        Runnable x=new Runnable() {
            @Override
            public void run() {
                finishAffinity();
            }
        };
        handler.postDelayed(x,6000);
    }
    }

