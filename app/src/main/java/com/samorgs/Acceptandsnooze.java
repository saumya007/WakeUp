package com.samorgs;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;
import java.util.Calendar;

import com.samorgs.AlarmRecieve;
import com.example.saumya.wakeup.R;
/**
 * Created by saumya on 25-11-2016.
 */
public class Acceptandsnooze extends AppCompatActivity implements SensorEventListener{

    String msg;
    private RelativeLayout.LayoutParams layoutParams;
    private SensorManager mSensorManager;
    private Sensor mProximity;
    private float timestamp;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.snoozeandstop);
                mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
                mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
                    }

            @Override
            protected void onResume() {
                super.onResume();
                mSensorManager.registerListener( this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
            }

            @Override
            protected void onPause() {
                super.onPause();
                mSensorManager.unregisterListener(this);
                finishAffinity();
            }

            public void onSensorChanged(SensorEvent event) {
                //long x=event.timestamp;
                if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {

                    if (event.values[0] >= -0.01 && event.values[0]<= 1) {
                        //long y=event.timestamp;
                        //near
                        Intent i = new Intent(Acceptandsnooze.this,SnoozeAndStopNormal.class);
                        startActivity(i);
                        finishAffinity();
                        }
                        else {
                        //far
                        Toast.makeText(getApplicationContext(), "Please move your hand closer to proximity sensor", Toast.LENGTH_SHORT).show();
                    }
                }
                //else if(event.sensor.getType()==Sensor.TYPE_)
            }

            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
    public void onStop()
    {
        super.onStop();
        finishAffinity();

    }

    public void onBackPressed() {
        // DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // super.onBackPressed();

    }

        }



