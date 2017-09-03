package com.samorgs;

/**
 * Created by saumya on 06-10-2016.
 */
public class Itemdata implements Comparable<Itemdata> {
    private String text;
    private String ringtext;
    private Integer SwitchID;
    private Integer MultispinnerID;
    private Integer Id;

    public Itemdata(int Id, String text, String ringtext, Integer SwitchID, Integer MultispinnerID) {
        this.Id = Id;
        this.text = text;
        this.ringtext = ringtext;
        this.SwitchID = SwitchID;
        this.MultispinnerID = MultispinnerID;

    }

    public String getRingtext() {
        return ringtext;
    }

    public void setRingText(String ringtext) {
        this.ringtext = ringtext;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getSwitchID() {
        return SwitchID;
    }

    public void setSwitchID(Integer switchID) {
        SwitchID = switchID;
    }

    public Integer getMultispinnerID() {
        return MultispinnerID;
    }

    public void setMultispinnerID(Integer multispinnerID) {
        MultispinnerID = multispinnerID;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer Id) {
        this.Id = Id;
    }

    @Override
    public int compareTo(Itemdata another) {
        return this.getText().compareTo(another.getText());
    }
}
