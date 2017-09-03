package com.samorgs;

/**
 * Created by saumya on 06-01-2017.
 */
public class ViewModel {
    private int itemcount;
    protected String time;
    public ViewModel(){

    }
    public ViewModel(String time){
        this.time=time;
    }
    public String getTime(){
        return time;
    }
    public void setTime(String time){
        this.time=time;
    }
}
