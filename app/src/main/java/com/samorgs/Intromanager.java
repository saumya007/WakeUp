package com.samorgs;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by saumya on 17-12-2016.
 */
public class Intromanager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    public Intromanager(Context context){
        this.context=context;
        pref=context.getSharedPreferences("first",0);
        editor=pref.edit();
    }
    public void setFirst(Boolean isFirst){
        editor.putBoolean("check",isFirst);
        editor.commit();
    }
    public boolean Check(){
        return pref.getBoolean("check",true);

    }
}
