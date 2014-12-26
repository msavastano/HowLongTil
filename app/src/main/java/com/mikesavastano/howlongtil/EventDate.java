package com.mikesavastano.howlongtil;

import java.util.Date;

/**
 * Created by michael on 12/18/14.
 */
public class EventDate {

    private int event_id;
    private Date event_date;
    private String event_name;

    public EventDate(){
    }

    public EventDate(String event_name, Date event_date){
        this.event_name = event_name;
        this.event_date = event_date;
    }

    public EventDate(int event_id, String event_name, Date event_date){
        this.event_id = event_id;
        this.event_name = event_name;
        this.event_date = event_date;
    }

    public void setID(int event_id) {
        this.event_id = event_id;
    }

    public int getID() {
        return this.event_id;
    }

    public void setProductName(String event_name) {
        this.event_name = event_name;
    }

    public String getEventName() {
        return this.event_name;
    }

    public void setDate(Date event_date) {
        this.event_date = event_date;
    }

    public Date getDate() {
        return this.event_date;
    }
}
