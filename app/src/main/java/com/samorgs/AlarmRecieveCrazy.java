package com.samorgs;

/**
 * Created by saumya & Jay on 04-01-2017.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import java.util.Calendar;

public class AlarmRecieveCrazy extends BroadcastReceiver {
    Uri uri;
    static Ringtone ringtone;
    Calendar cal = Calendar.getInstance();
    public void onReceive(Context context, Intent intent) {
        intent.getExtras();
//        Log.e("Intent data",Uri.parse(intent.getStringExtra("uri"))+"");
        String x=(intent.getExtras().getString("uri"));
        uri=Uri.parse(x);
        ringtone =RingtoneManager.getRingtone(context,uri);
        ringtone.play();
        Intent i = new Intent(context,Mathproblems.class);
        i.putExtra("Uri",uri);
        context.startActivity(i);
    }
    public static void stopRingtone(){
        ringtone.stop();
    }}
