package com.example.mahmoud.fcisquare.model;

/**
 * Created by Mahmoud on 4/18/2016.
 */
public class Notification {
    String text, time, date, checkinID, type;

    public Notification(String text, String time, String date, String checkinID, String type) {
        this.text = text;
        this.time = time;
        this.date = date;
        this.checkinID = checkinID;
        this.type = type;
    }

    public Notification(String text, String time, String date) {
        this.text = text;
        this.time = time;
        this.date = date;
    }

    public Notification(String text, String time, String checkinID, String date) {
        this.text = text;
        this.time = time;
        this.checkinID = checkinID;
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCheckinID() {
        return checkinID;
    }

    public void setCheckinID(String checkinID) {
        this.checkinID = checkinID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
