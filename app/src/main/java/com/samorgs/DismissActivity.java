package com.samorgs;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.saumya.wakeup.R;

public class DismissActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dismiss);
        Intent intent = new Intent(DismissActivity.this, AlarmRecieveCrazy.class);
        PendingIntent pending = PendingIntent.getBroadcast(getBaseContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        try {
            AlarmRecieveCrazy.stopRingtone();
        }
        catch (NullPointerException e){
            //  Toast.makeText(getApplicationContext(),"Ringtone isnt set",Toast.LENGTH_SHORT).show();
        }
        pending.cancel();
        alarmManager.cancel(pending);
        Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        Handler handler = new Handler();
        Runnable x=new Runnable() {
            @Override
            public void run() {
                finishAffinity();
            }
        };
        handler.postDelayed(x,6000);
    }

    public void onBackPressed() {


    }
}
