package com.example.tariqi;


public class Trip {
     String name;
     String location;
     String date;
     String time;
     String type;
     String startPoint;
    String email,password,uid,upcomingid,doneid;
    String note;
    long cal;
    public Trip(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Trip() {}

    public Trip(String name) {
        this.name = name;
    }

    public Trip(String email, String password, String uid) {
        this.email = email;
        this.password = password;
        this.uid = uid;
    }

    public Trip(String name, String location, String date, String time, String type, String startPoint, String note) {
        this.name = name;
        this.location = location;
        this.date = date;
        this.time = time;
        this.type = type;
        this.startPoint = startPoint;
        this.note = note;
    }

    public Trip(String name, String location, String date, String time, String type, String uid, String email, String password,String upcomingid,String doneid ) {
        this.name = name;
        this.location = location;
        this.date = date;
        this.time = time;
        this.type = type;
        this.uid=uid;
        this.email = email;
        this.password = password;
        this.upcomingid=upcomingid;
        this.doneid=doneid;
        note = "";
    }

    public long getCal() {
        return cal;
    }

    public void setCal(long cal) {
        this.cal = cal;
    }

    public String getUpcomingid() {
        return upcomingid;
    }

    public void setUpcomingid(String upcomingid) {
        this.upcomingid = upcomingid;
    }

    public String getDoneid() {
        return doneid;
    }

    public void setDoneid(String doneid) {
        this.doneid = doneid;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
