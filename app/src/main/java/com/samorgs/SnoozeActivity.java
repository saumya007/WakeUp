package com.samorgs;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.widget.Toast;

import com.example.saumya.wakeup.R;

public class SnoozeActivity extends AppCompatActivity {
    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_snooze);
        Intent  i= getIntent();
        uri = i.getParcelableExtra("Uri");
        Intent intent = new Intent(SnoozeActivity.this, AlarmRecieveCrazy.class);
        intent.putExtra("uri",uri);
        PendingIntent pending = PendingIntent.getBroadcast(getBaseContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long CurrentTimeInMillis = System.currentTimeMillis();
        long NextUpdateTimeInMillis = CurrentTimeInMillis + 1* DateUtils.MINUTE_IN_MILLIS;
        android.text.format.Time NextUpdateTime  = new android.text.format.Time();
        NextUpdateTime.set(NextUpdateTimeInMillis);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,NextUpdateTimeInMillis,pending);
        Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
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
