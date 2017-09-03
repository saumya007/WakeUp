package com.samorgs;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.saumya.wakeup.R;
import android.widget.Toast;

import java.sql.Time;
import java.util.Calendar;

public class MathActivity extends AppCompatActivity {
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Shakedetector mShakeDetector;
    private ProgressBar progressBar;
    private int progressStatus = 0;
    private int flags;
    Uri uri;
    private TextView textView;
    Mathproblems mp = new Mathproblems();
    Calendar cal = Calendar.getInstance();
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math);
        Intent i = getIntent();
        final String str = i.getStringExtra("answer");
        uri =i.getParcelableExtra("Uri");
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(Integer.parseInt(str));
        textView = (TextView) findViewById(R.id.textView2);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        TextView texxt = (TextView) findViewById(R.id.textView3);
        texxt.setText("Shake Your device " + str + " times");
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new Shakedetector();
        mShakeDetector.setOnShakeListener(new Shakedetector.OnShakeListener() {

            @Override
            public void onShake(final int count) {
                progressStatus = 0;
                if (count > Integer.parseInt(str)) {
                    progressStatus = Integer.parseInt(str);
                    Intent i = new Intent(MathActivity.this,SnoozeAndStopAnimation.class);
                    i.putExtra("Uri",uri);
                    startActivityForResult(i,1);
                        finish();
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (count < Integer.parseInt(str)) {
                            progressStatus = count;
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(progressStatus);
                                    textView.setText("Progress: " + progressStatus + "/" + progressBar.getMax());
                                }
                            });
                            try {
                                Thread.sleep(17000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
                //handleShakeEvent(count);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        progressBar.setProgress(progressStatus);
        textView.setText("Progress: " + progressStatus + "/" + progressBar.getMax());
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        progressBar.setProgress(progressStatus);
        textView.setText("Progress: " + progressStatus + "/" + progressBar.getMax());
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }
    public void onBackPressed() {
        // DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // super.onBackPressed();

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==1&&resultCode==RESULT_OK){
            flags=Integer.parseInt(data.getStringExtra("SnoozeOrStop"));
            Log.e("flagvalue",flags+"");
        }
    }
}