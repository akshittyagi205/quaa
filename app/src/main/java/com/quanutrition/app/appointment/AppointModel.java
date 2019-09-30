package com.quanutrition.app.appointment;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public class AppointModel {
    private String daySection;
    private Drawable image;
    private ArrayList<ChildListAppoint> childListAppoint;

    public AppointModel(String daySection, Drawable image, ArrayList<ChildListAppoint> childListAppoint) {
        this.daySection = daySection;
        this.childListAppoint = childListAppoint;
        this.image = image;
    }

    public String getDaySection() {
        return daySection;
    }

    public void setDaySection(String daySection) {
        this.daySection = daySection;
    }

    public ArrayList<ChildListAppoint> getChildListAppoint() {
        return childListAppoint;
    }

    public void setChildListAppoint(ArrayList<ChildListAppoint> childListAppoint) {
        this.childListAppoint = childListAppoint;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    /**
     * Child Or the time list
     */
    public static class ChildListAppoint{
        private String time;
        private boolean availabilty;

        public ChildListAppoint(String time, boolean availabilty) {
            this.time = time;
            this.availabilty = availabilty;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public boolean isAvailabilty() {
            return availabilty;
        }

        public void setAvailabilty(boolean availabilty) {
            this.availabilty = availabilty;
        }

    }
}
