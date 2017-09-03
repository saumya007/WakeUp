package com.samorgs;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import java.util.Calendar;

public class AlarmRecieve extends BroadcastReceiver {
static Ringtone ringtone;
    Uri uri;
    Calendar cal = Calendar.getInstance();
    public void onReceive(Context context, Intent intent) {
        intent.getExtras();
//        Log.e("Intent data",Uri.parse(intent.getStringExtra("uri"))+"");
        String x=(intent.getExtras().getString("uri"));
        uri=Uri.parse(x);
        //Uri uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
        ringtone =RingtoneManager.getRingtone(context,uri);
        ringtone.play();
        Intent i =new Intent(context,Acceptandsnooze.class);
        context.startActivity(i);

    }
    public static void stopRingtone(){
    ringtone.stop();
    }
/*MediaPlayer mediaPlayer;
@Override
public void onReceive(Context arg0, Intent arg1) {
        Toast.makeText(arg0, "Alarm received!", Toast.LENGTH_LONG).show();
        mediaPlayer = MediaPlayer.create(arg0, R.raw.alert);
        for(int i=0;i<10;i++) {
        i++;
        mediaPlayer.start();
        }


        }*/

}
