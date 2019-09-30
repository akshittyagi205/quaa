package com.quanutrition.app.profile;

public class MeasurementsModel {
    private String date,id,chest,waist,abd,thigh,hips,forearm;

    public MeasurementsModel(String date, String id, String chest, String waist, String abd, String thigh, String hips, String forearm) {
        this.date = date;
        this.id = id;
        this.chest = chest;
        this.waist = waist;
        this.abd = abd;
        this.thigh = thigh;
        this.hips = hips;
        this.forearm = forearm;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChest() {
        return chest;
    }

    public void setChest(String chest) {
        this.chest = chest;
    }

    public String getWaist() {
        return waist;
    }

    public void setWaist(String waist) {
        this.waist = waist;
    }

    public String getAbd() {
        return abd;
    }

    public void setAbd(String abd) {
        this.abd = abd;
    }

    public String getThigh() {
        return thigh;
    }

    public void setThigh(String thigh) {
        this.thigh = thigh;
    }

    public String getHips() {
        return hips;
    }

    public void setHips(String hips) {
        this.hips = hips;
    }

    public String getForearm() {
        return forearm;
    }

    public void setForearm(String forearm) {
        this.forearm = forearm;
    }
}
