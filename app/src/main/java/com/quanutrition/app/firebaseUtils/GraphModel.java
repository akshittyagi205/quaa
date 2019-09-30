package com.quanutrition.app.firebaseUtils;

import com.quanutrition.app.Utils.Tools;

public class GraphModel implements Comparable {
    String date,value;

    public GraphModel(String date, String value) {
        this.date = date;
        this.value = value;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int compareTo(Object compareTu) {
        long compareage= Tools.getTimeInMillis(((GraphModel)compareTu).getDate());
        /* For Ascending order*/
        return (int)(Tools.getTimeInMillis(this.date)-compareage);
    }



    /*@Override
    public int compareTo(GraphModel comparestu) {
        int compareage=((GraphModel)comparestu).getStudentage();
        *//* For Ascending order*//*
        return this.studentage-compareage;

        *//* For Descending order do like this *//*
        //return compareage-this.studentage;
    }*/
}
