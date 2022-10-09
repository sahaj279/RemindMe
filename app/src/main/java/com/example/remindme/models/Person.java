package com.example.remindme.models;

public class Person {
    String p_name,dob,marriage_date,contact;

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Person(String name, String dob, String marriage_date, String contact) {
        this.p_name = name;
        this.dob = dob;
        this.marriage_date = marriage_date;
        this.contact=contact;
    }
    public Person(){}

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String name) {
        this.p_name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getMarriage_date() {
        return marriage_date;
    }

    public void setMarriage_date(String marriage_date) {
        this.marriage_date = marriage_date;
    }
}
