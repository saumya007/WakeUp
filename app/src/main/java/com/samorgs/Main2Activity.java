package com.samorgs;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.DataSetObserver;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.samorgs.Acceptandsnooze;
import com.example.saumya.wakeup.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import com.samorgs.AlarmRecieve;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.READ_CALENDAR;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.VIBRATE;

public class Main2Activity extends AppCompatActivity {
    RecyclerView recyclerView;
    //private RemoteViews remoteview;
    int hour;
    int minutes;
    public Uri uri;
    Ringtone ringtone;
    Calendar cal = Calendar.getInstance();
    PendingIntent pendingIntent;
    Context context;
    int longClickedItemIndexx;
    int counterr =0;
    final int RQS_RINGTONEPICKER=1;
    ListView AlarmsList;
    DatabaseHandler dbHandler;
    ArrayList<Itemdata> alarmslist =new ArrayList<Itemdata>();
    int longClickedItemIndex;
    ArrayAdapter<Itemdata> AlarmsAdapter;
    boolean permissiongranted = false;
    private int notification_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getSharedPreferences("My preference", MODE_PRIVATE);
        Boolean restoredText = prefs.getBoolean("b", Boolean.parseBoolean(null));
        if (restoredText != false) {
            prefs.getString("ClassLabel",this.getLocalClassName());
            //"No name defined" is the default value.
            // int idName = prefs.getInt("idName", 0); //0 is the default value.
        }
//        savedInstanceState.putBoolean("MyBoolean", true);
        // WebView myWebView = null;
            setContentView(R.layout.activity_main2list);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Main2Activity.this, new String[]{VIBRATE, READ_EXTERNAL_STORAGE, READ_CALENDAR}, 1);

            }

        }
        else{
            permissiongranted = true;
        }
            AlarmsList=(ListView)findViewById(R.id.listview);
            dbHandler=new DatabaseHandler(getApplicationContext());
            final FloatingActionButton addBtn =(FloatingActionButton)findViewById(R.id.fab1);
            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Itemdata Alarms= new Itemdata(dbHandler.getAlarmscount(), "9 : 30 AM","Default Ringtone",1,1);
                        dbHandler.createalarm(Alarms);
                        alarmslist.add(Alarms);
                        AlarmsAdapter.notifyDataSetChanged();
                        Collections.sort(alarmslist);
                        //Toast.makeText(getApplicationContext(), nametxt.getText().toString() + "Has Been Added To Your Contacts", Toast.LENGTH_SHORT).show();
                        return;


                }
            });
            if(dbHandler.getAlarmscount()!=0)
                alarmslist.addAll(dbHandler.getAllAlarms());
            populateList();
           Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
        this.context = this;
    }
    private  boolean AlarmExists(Itemdata alarmdata)
    {
        String text=alarmdata.getText();
        int count= alarmslist.size();
        for(int i=0;i<count;i++)
        {
            String alarmtext = alarmslist.get(i).getText();
            if(alarmtext.equalsIgnoreCase(text))
                return true;
        }
        return false;
    }
    private void populateList()
    {
        Collections.sort(alarmslist);
        AlarmsAdapter=new AlarmsAdapter();
        AlarmsList.setAdapter(AlarmsAdapter);
        // contactlistview.setOnClickListener(new View.OnClickListener() {
        //   @Override
        // public void onClick(View v) {

        // }
        //});
    }
    private class AlarmsAdapter extends ArrayAdapter<Itemdata>
    {
        public AlarmsAdapter()
        {
            super(Main2Activity.this, R.layout.list_child, alarmslist);
        }
        private  void startRingtonePicker(){
            Intent intent= new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,RingtoneManager.TYPE_ALARM);
            startActivityForResult(intent,2);


        }

        @Override
        public View getView(int position, View view, ViewGroup parent)
        {
            if(view==null){
                view=getLayoutInflater().inflate(R.layout.list_child,parent,false);}
            final List<String> list1 = Arrays.asList(getResources().getStringArray(R.array.alarm_list));
            final List<KeyPairBoolData> listArray = new ArrayList<>();
            for(int j = 0; j < list1.size(); j++) {
                KeyPairBoolData h = new KeyPairBoolData();
                h.setId(j + 1);
                h.setName(list1.get(j));
                h.setSelected(false);
                listArray.add(h);
            }
            final List<KeyPairBoolData> listArray2 = new ArrayList<>();

            for (int j = 0; j < list1.size(); j++) {
                KeyPairBoolData h = new KeyPairBoolData();
                h.setId(j + 1);
                h.setName(list1.get(j));
                h.setSelected(false);
                listArray2.add(h);
            }
            final List<KeyPairBoolData> listArray3 = new ArrayList<>();

            for (int j = 0; j < list1.size(); j++) {
                KeyPairBoolData h = new KeyPairBoolData();
                h.setId(j + 1);
                h.setName(list1.get(j));
                h.setSelected(false);
                listArray3.add(h);
            }
            final TextView timetext = (TextView)view.findViewById(R.id.textviewlist1);
            final TextView Ringtext = (TextView)view.findViewById(R.id.textViewlist7);
            final Switch switch1 = (Switch)view.findViewById(R.id.switchlist1);
            MultiSpinnerSearch searchSpinner = (MultiSpinnerSearch)view.findViewById(R.id.searchMultiSpinnerlist);
            Ringtext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Ringtext.setPressed(true);
                    startRingtonePicker();
                }
            });
            searchSpinner.setItems(listArray, -1, new SpinnerListener() {

                @Override
                public void onItemsSelected(List<KeyPairBoolData> items) {
                    // final CheckBox cb =(CheckBox)view.findViewById(R.id.alertCheckbox);
                    for (int i = 0; i < items.size(); i++) {
                        if (items.get(i).isSelected()) {
                            Log.i("TAG", i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());
                            items.get(i).setSelected(true);
                            if (i == 0 && switch1.isChecked()) {
                                Intent intentc = new Intent(Main2Activity.this, AlarmRecieveCrazy.class);
                                if(!Ringtext.isPressed()){
                                    Log.e("Uri",uri+"");
                                    intentc.putExtra("uri",uri.toString());
                                    //intent.putExtra("ringtone",ringtone);
                                }
                                else{

                                }
                                final int this_id = (int)System.currentTimeMillis();
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), this_id, intentc,PendingIntent.FLAG_ONE_SHOT);
                                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                                alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);


                            }
                        }
                        else {
                            if (switch1.isChecked()) {
                                Intent intent = new Intent(Main2Activity.this, AlarmRecieve.class);
                                //intent.putExtra("Hours",hrs).putExtra("Minutes",mins);
                                if(!Ringtext.isPressed()){
                                    Log.e("Uri",uri+"");
                                    intent.putExtra("uri",uri.toString());
                                    //intent.putExtra("ringtone",ringtone);
                                }
                                else{

                                }
                                final int this_id = (int)System.currentTimeMillis();
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), this_id, intent,PendingIntent.FLAG_ONE_SHOT);
                                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                                alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                            }
                        }

                    }
                }
            });
            timetext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.textviewlist1:
                            new TimePickerDialog(Main2Activity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, KTimeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show();

                    }
                }

                protected TimePickerDialog.OnTimeSetListener KTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        int hour = hourOfDay;
                        String hrss = "";
                        int minutes = minute;
                        String mins = "";
                        int hrs = 0;
                        String timeset;
                        if (hour > 12) {
                            hrs = hour - 12;
                            timeset = "PM";
                        } else if (hour == 0) {
                            hrs = hour + 12;
                            timeset = "AM";
                        } else if (hour == 12) {
                            hrs = hour;
                            timeset = "PM";
                        } else {
                            hrs = hour;
                            hrss = "0" + String.valueOf(hrs);
                            hrs = Integer.parseInt(hrss);
                            timeset = "AM";
                        }
                        if (minutes < 10) {
                            mins = "0" + minutes;
                            minutes = Integer.parseInt(mins);
                        } else {
                            if (minutes > 60) {
                                minutes = minutes - 60;
                                hrs++;
                            }
                            mins = "" + minutes;
                        }

                        //minutes=minute;
                        timetext.setText(hrs + " : " + mins + "  " + timeset);
                        timetext.setPressed(true);
                        Calendar calNow = (Calendar)cal.clone();
                        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        cal.set(Calendar.MINUTE, minute);
                        cal.set(Calendar.SECOND,0);
                        cal.set(Calendar.MILLISECOND,0);
                        switch1.setChecked(true);

                        if(cal.compareTo(calNow)>0) {
                            Log.e("Current",System.currentTimeMillis()+"");
                            Intent intents = new Intent(Main2Activity.this, Main2Activity.class);
                            //intents.putExtra("TextView",textView.getText().toString());
                            intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
// Adds the back stack
                            stackBuilder.addParentStack(Main2Activity.class);
// Adds the Intent to the top of the stack
                            stackBuilder.addNextIntent(intents);
// Gets a PendingIntent containing the entire back stack
                        /*PendingIntent pendingIntents = PendingIntent.getActivity(
                                context,
                                0,
                                intents,PendingIntent.FLAG_UPDATE_CURRENT);*/
                            //PendingIntent pendingIntents =
                            //      stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                            int days = cal.get(Calendar.DAY_OF_WEEK);
                            String parsing = null;
                            //days=days+1;
                            if (days > 7) {
                                days = 1;
                            }
                            if (days == 1) {
                                parsing = "Sun";
                            }
                            if (days == 2) {
                                parsing = "Mon";
                            }
                            if (days == 3) {
                                parsing = "Tue";
                            }
                            if (days == 4) {
                                parsing = "Wed";
                            }
                            if (days == 5) {
                                parsing = "Thurs";
                            }
                            if (days == 6) {
                                parsing = "Fri";
                            }
                            if (days == 7) {
                                parsing = "Sat";
                            }

                            PendingIntent pendingIntents = PendingIntent.getActivity(getApplicationContext(), 0, intents, 0);
                            final Notification notification = new Notification.Builder(Main2Activity.this)
                                    .setTicker("TickerTitle")
                                    .setContentTitle("Upcoming Alarm" + hrs + " " + ":" + " " + cal.get(Calendar.MINUTE) + " " + timeset + " " + parsing)
                                    //.addAction(R.drawable.ic_clear_black_24dp, hrs + " " + ":" + " " + cal.get(Calendar.MINUTE) + " " + timeset +" "+parsing, pendingIntent)
                                    .setSmallIcon(R.drawable.ic_alarm_on_black_24dp)
                                    .setContentIntent(pendingIntents).getNotification();
                            notification.flags = Notification.FLAG_AUTO_CANCEL;
                            notification.defaults |= Notification.DEFAULT_LIGHTS;

                            final NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            notificationManager.notify(0, notification);

                            //Intent inte = new Intent(getApplicationContext(), Mathproblems.class);

                            //Intent ints = getIntent();
                            //String x=ints.getStringExtra("TextView");
                            // textView.setText(someVarB);
                            //pendingIntent = PendingIntent.getBroadcast(getBaseContext(),0,intent,0);
                            //startActivity(intent);





                        }
                        else if(cal.compareTo(calNow)<0) {
                            int days = cal.get(Calendar.DAY_OF_WEEK);
                            String parsing = null;
                            days = days + 1;
                            if (days > 7) {
                                days = 1;
                            }
                            if (days == 1) {
                                parsing = "Sun";
                            }
                            if (days == 2) {
                                parsing = "Mon";
                            }
                            if (days == 3) {
                                parsing = "Tue";
                            }
                            if (days == 4) {
                                parsing = "Wed";
                            }
                            if (days == 5) {
                                parsing = "Thurs";
                            }
                            if (days == 6) {
                                parsing = "Fri";
                            }
                            if (days == 7) {
                                parsing = "Sat";
                            }
                            cal.set(Calendar.DAY_OF_WEEK, days);
                            cal.set(Calendar.HOUR_OF_DAY, hour);
                            cal.set(Calendar.MINUTE, minutes);
                            Intent intents = new Intent(Main2Activity.this, Main2Activity.class);
                            //intents.putExtra("TextView",textView.getText().toString());
                            intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
// Adds the back stack
                            stackBuilder.addParentStack(Main2Activity.class);
// Adds the Intent to the top of the stack
                            stackBuilder.addNextIntent(intents);
// Gets a PendingIntent containing the entire back stack
                        /*PendingIntent pendingIntents = PendingIntent.getActivity(
                                context,
                                0,
                                intents,PendingIntent.FLAG_UPDATE_CURRENT);*/
                            //PendingIntent pendingIntents =
                            //      stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                            PendingIntent pendingIntents = PendingIntent.getActivity(getApplicationContext(), 0, intents, 0);
                            Notification notification = new Notification.Builder(Main2Activity.this)
                                    .setTicker("TickerTitle")
                                    .setContentTitle("Upcoming Alarm" + hrs + " " + ":" + " " + cal.get(Calendar.MINUTE) + " " + timeset + " " + parsing)
                                    //.addAction(R.drawable.ic_clear_black_24dp, hrs + " " + ":" + " " + cal.get(Calendar.MINUTE) + " " + timeset+" "+parsing, pendingIntent)
                                    .setSmallIcon(R.drawable.ic_alarm_on_black_24dp)
                                    .setContentIntent(pendingIntents).getNotification();
                            notification.flags = Notification.FLAG_AUTO_CANCEL;
                            notification.defaults |= Notification.DEFAULT_LIGHTS;
                            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            notificationManager.notify(0, notification);
                            onPause();

                        }
                    }
                };
            });
            //Itemdata Currentalarm=alarmslist.get(position);
            switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (!timetext.isPressed()) {
                            cal.setTimeInMillis(System.currentTimeMillis());
                            cal.set(Calendar.HOUR_OF_DAY, 9);
                            cal.set(Calendar.MINUTE, 10);
                            if ( (cal.getTimeInMillis() - System.currentTimeMillis()) > 10|(cal.getTimeInMillis() - System.currentTimeMillis())==0) {
                                //if (switchchecked  && !itemselect ) {
                                //  Intent intent = new Intent(Main2Activity.this, AlarmRecieve.class);
                                //intent.putExtra("Hours",hrs).putExtra("Minutes",mins);
                                //PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                //AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                                //alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);

                                Intent intents = new Intent(Main2Activity.this, Main2Activity.class);
                                //intents.putExtra("TextView",textView.getText().toString());
                                intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
// Adds the back stack
                                stackBuilder.addParentStack(Main2Activity.class);
// Adds the Intent to the top of the stack
                                stackBuilder.addNextIntent(intents);
// Gets a PendingIntent containing the entire back stack
                        /*PendingIntent pendingIntents = PendingIntent.getActivity(
                                context,
                                0,
                                intents,PendingIntent.FLAG_UPDATE_CURRENT);*/
                                //PendingIntent pendingIntents =
                                //      stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                                int days = cal.get(Calendar.DAY_OF_WEEK);
                                String parsing = null;
                                //days=days+1;
                                if (days > 7) {
                                    days = 1;
                                }
                                if (days == 1) {
                                    parsing = "Sun";
                                }
                                if (days == 2) {
                                    parsing = "Mon";
                                }
                                if (days == 3) {
                                    parsing = "Tue";
                                }
                                if (days == 4) {
                                    parsing = "Wed";
                                }
                                if (days == 5) {
                                    parsing = "Thurs";
                                }
                                if (days == 6) {
                                    parsing = "Fri";
                                }
                                if (days == 7) {
                                    parsing = "Sat";
                                }
                                PendingIntent pendingIntents = PendingIntent.getActivity(getApplicationContext(), 0, intents, 0);
                                Notification notification = new Notification.Builder(Main2Activity.this)
                                        .setTicker("TickerTitle")
                                        .setContentTitle("Upcoming Alarm" + 9 + " " + ":" + " " + 10 + " " + "AM" + " " + parsing)
                                        //.addAction(R.drawable.ic_clear_black_24dp, 9 + " " + ":" + " " + 10 + " " + "AM" + " " + parsing, pendingIntent)
                                        .setSmallIcon(R.drawable.ic_alarm_on_black_24dp)
                                        .setContentIntent(pendingIntents).getNotification();
                                notification.flags = Notification.FLAG_AUTO_CANCEL;
                                notification.defaults |= Notification.DEFAULT_LIGHTS;
                                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                notificationManager.notify(0, notification);
                                //Intent ints = getIntent();
                                //String x=ints.getStringExtra("TextView");
                                // textView.setText(someVarB);
                                //pendingIntent = PendingIntent.getBroadcast(getBaseContext(),0,intent,0);
                                //startActivity(intent);
                                //}

                            }else if ((cal.getTimeInMillis() - System.currentTimeMillis()) < 0) {
                                int days = cal.get(Calendar.DAY_OF_WEEK);
                                String parsing = null;
                                days = days + 1;
                                if (days > 7) {
                                    days = 1;
                                }
                                if (days == 1) {
                                    parsing = "Sun";
                                }
                                if (days == 2) {
                                    parsing = "Mon";
                                }
                                if (days == 3) {
                                    parsing = "Tue";
                                }
                                if (days == 4) {
                                    parsing = "Wed";
                                }
                                if (days == 5) {
                                    parsing = "Thurs";
                                }
                                if (days == 6) {
                                    parsing = "Fri";
                                }
                                if (days == 7) {
                                    parsing = "Sat";
                                }
                                cal.set(Calendar.DAY_OF_WEEK, days);
                                cal.set(Calendar.HOUR_OF_DAY, hour);
                                cal.set(Calendar.MINUTE, minutes);
                                Intent intents = new Intent(Main2Activity.this, Main2Activity.class);
                                //intents.putExtra("TextView",textView.getText().toString());
                                intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
// Adds the back stack
                                stackBuilder.addParentStack(Main2Activity.class);
// Adds the Intent to the top of the stack
                                stackBuilder.addNextIntent(intents);
// Gets a PendingIntent containing the entire back stack
                        /*PendingIntent pendingIntents = PendingIntent.getActivity(
                                context,
                                0,
                                intents,PendingIntent.FLAG_UPDATE_CURRENT);*/
                                //PendingIntent pendingIntents =
                                //      stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                                PendingIntent pendingIntents = PendingIntent.getActivity(getApplicationContext(), 0, intents, 0);
                                Notification notification = new Notification.Builder(Main2Activity.this)
                                        .setTicker("TickerTitle")
                                        .setContentTitle("Upcoming Alarm" + 9 + " " + ":" + " " + 10 + " " + "AM" + " " + parsing)
                                        //.addAction(R.drawable.ic_clear_black_24dp, 9 + " " + ":" + " " + 10 + " " + "AM" + " " + parsing, pendingIntent)
                                        .setSmallIcon(R.drawable.ic_alarm_on_black_24dp)
                                        .setContentIntent(pendingIntents).getNotification();
                                notification.flags = Notification.FLAG_AUTO_CANCEL;
                                notification.defaults |= Notification.DEFAULT_LIGHTS;
                                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                notificationManager.notify(0, notification);


                            } else {

                            }

                        }
                    }else {
                        /* Create the PendingIntent that would have launched the BroadcastReceiver */
                        Intent intent = new Intent(Main2Activity.this, AlarmRecieve.class);
                        PendingIntent pending = PendingIntent.getBroadcast(getBaseContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                        try {
                            AlarmRecieve.stopRingtone();
                        } catch (NullPointerException e) {
                            //Toast.makeText(getApplicationContext(),"The application cant stop ringtone manager as it is not set",Toast.LENGTH_SHORT).show();
                        }
                        pending.cancel();
                        alarmManager.cancel(pending);
        /* Cancel the alarm associated with that PendingIntent */
                        Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        notificationManager.cancelAll();
                        Intent intentc = new Intent(Main2Activity.this, AlarmRecieveCrazy.class);
                        PendingIntent pendings = PendingIntent.getBroadcast(getBaseContext(), 0, intentc, PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager alarmManagers = (AlarmManager) getSystemService(ALARM_SERVICE);
                        try {
                            AlarmRecieveCrazy.stopRingtone();
                        } catch (NullPointerException e) {
                            //Toast.makeText(getApplicationContext(),"The application cant stop ringtone manager as it is not set",Toast.LENGTH_SHORT).show();
                        }
                        pending.cancel();
                        alarmManagers.cancel(pending);
        /* Cancel the alarm associated with that PendingIntent */
                        Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                        NotificationManager notificationManagers = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        notificationManagers.cancelAll();

                    }
                }
            });

          /*  TextView phone=(TextView) view.findViewById(R.id.phone_textview);
            phone.setText(Currentcontact.getphone());
            TextView email=(TextView)view.findViewById(R.id.email_textview);
            email.setText(Currentcontact.getEmail());
            TextView address=(TextView) view.findViewById(R.id.addresstextview);
            address.setText(Currentcontact.getAddress());*/
            return view;
        }


    }

    @Override
    public void onBackPressed() {
        // DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // super.onBackPressed();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
      //  getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        return  super.onContextItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
          //  return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();


    }
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState){
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    }

    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences preferences = getSharedPreferences("My preference",0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ClassLabel",this.getLocalClassName());
        editor.putBoolean("b",true);
        editor.commit();
        Log.e("yoo", "onStop: ");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences preferences = getSharedPreferences("My preference",0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ClassLabel",this.getLocalClassName());
        editor.putBoolean("b",true);
        editor.commit();
    }

    public void onPause(){
        super.onPause();
        SharedPreferences.Editor editor = getSharedPreferences("My preference", MODE_PRIVATE).edit();
        editor.putBoolean("b",true);
        editor.putString("ClassLabel",this.getLocalClassName());
        editor.commit();
        Log.e("yoo", "onPause: ");
    }
    public void onResume(){

        SharedPreferences prefs = getSharedPreferences("My preference", MODE_PRIVATE);
        Boolean restoredText = prefs.getBoolean("b", Boolean.parseBoolean(null));
        if (restoredText != false) {
            prefs.getString("ClassLabel",this.getLocalClassName());
            //"No name defined" is the default value.
            // int idName = prefs.getInt("idName", 0); //0 is the default value.
        }
        super.onResume();
        counterr++;
        Log.e("loll", "onResume:");
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //TextView textView = (TextView) findViewById(R.id.textviewlist1);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) //check if the request code is the one you've sent
        {
               // String y = data.getStringExtra("TextviewText");
                //textView.setText(y);
                uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                ringtone=RingtoneManager.getRingtone(getApplicationContext(),uri);
                Toast.makeText(getApplicationContext(),ringtone+"",Toast.LENGTH_SHORT).show();
            }
       else if (requestCode == 2 && resultCode == RESULT_OK) //check if the request code is the one you've sent
        {
            // String y = data.getStringExtra("TextviewText");
            //textView.setText(y);
            uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            ringtone=RingtoneManager.getRingtone(getApplicationContext(),uri);
            Toast.makeText(getApplicationContext(),ringtone+"",Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 1:
            {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED  ){
                    Toast.makeText(getApplicationContext(),"Permission granted",Toast.LENGTH_SHORT).show();

                    permissiongranted = true;
                }
                else{
                    permissiongranted = false;
                    Toast.makeText(Main2Activity.this, "Need this permission", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}
