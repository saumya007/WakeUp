package com.samorgs;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by saumya on 06-04-2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION= 1;
    private static final String DATABASE_NAME="Alarm Manager",
    TABLE_ALARMS="alarms",
    KEY_ID="id",
    KEY_TEXT1="timetext",
    KEY_TEXT2="ringtonetext",
    KEY_SPINNER="spinners",
    KEY_SWITCH="switches";
    public DatabaseHandler(Context context)
    {
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
       // db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
            db.execSQL("CREATE TABLE " + TABLE_ALARMS + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_TEXT1 + " INTEGER," + KEY_TEXT2 + " INTEGER," + KEY_SPINNER + " INTEGER," + KEY_SWITCH + " INTEGER)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARMS);
        onCreate(db);
    }
    public void createalarm(Itemdata alarmdata)
    {
        SQLiteDatabase db =getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put( KEY_TEXT1, alarmdata.getText());
        values.put( KEY_TEXT2, alarmdata.getRingtext());
        values.put( KEY_SPINNER, alarmdata.getMultispinnerID());
        values.put( KEY_SWITCH, alarmdata.getSwitchID());
       db.insert(TABLE_ALARMS, null, values);
        db.close();
    }
    public Itemdata getAlarm(int id)
    {
        SQLiteDatabase db =getReadableDatabase();
        Cursor cursor = db.query(TABLE_ALARMS,new String [] {KEY_ID,KEY_TEXT1,KEY_TEXT2,KEY_SPINNER,KEY_SWITCH },KEY_ID + "=?",new String[] { String.valueOf(id)},null,null,null,null);
            if(cursor !=null)
                cursor.moveToFirst();
        Itemdata alarmdata = new Itemdata(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), Integer.parseInt(cursor.getString(3)), Integer.parseInt(cursor.getString(4)));
                db.close();
                cursor.close();
        return alarmdata;
    }
    public void deletealarms(Itemdata alarmdata)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_ALARMS,KEY_ID + "=?",new String[]{String.valueOf(alarmdata.getId())});
        db.close();
    }
    public int getAlarmscount()
    {
        SQLiteDatabase db =getReadableDatabase();
        Cursor cursor =db.rawQuery(" SELECT * FROM " + TABLE_ALARMS, null);
        int count=cursor.getCount();
        db.close();
        cursor.close();
        return count;
    }
    public int updatealarms(Itemdata alarmdata)
    {
        SQLiteDatabase db =getWritableDatabase();
        ContentValues values= new ContentValues();
        values.put( KEY_TEXT1, alarmdata.getText());
        values.put( KEY_TEXT2, alarmdata.getRingtext());
        values.put(KEY_SPINNER, alarmdata.getMultispinnerID());
        values.put(KEY_SWITCH, alarmdata.getSwitchID());
        int rowsAffected = db.update(TABLE_ALARMS, values, KEY_ID + "=?", new String[]{String.valueOf(alarmdata.getId())});
        db.close();
        return rowsAffected;
    }
    public List<Itemdata> getAllAlarms()
    {
        List<Itemdata>  Alarms= new ArrayList<Itemdata>();
        SQLiteDatabase db=getWritableDatabase();
        Cursor cursor =db.rawQuery(" SELECT * FROM "+TABLE_ALARMS,null);
        if(cursor.moveToFirst())
        {
            do {
                Alarms.add(new Itemdata(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), Integer.parseInt(cursor.getString(3)), Integer.parseInt(cursor.getString(4))));
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return Alarms;
    }

    }




