package com.example.himanshu.trial4;

public class Bookings {

    private String Court;
    private String Date;
    private String Slots;

    public Bookings(){}

    public Bookings(String court, String date, String slots) {
        this.Court = court;
        this.Date = date;
        this.Slots = slots;
    }

    public String getCourt() {
        return Court;
    }

    public String getDate() {
        return Date;
    }

    public String getSlots() {
        return Slots;
    }

    public void setCourt(String court) {
        Court = court;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setSlots(String slots) {
        Slots = slots;
    }
}
