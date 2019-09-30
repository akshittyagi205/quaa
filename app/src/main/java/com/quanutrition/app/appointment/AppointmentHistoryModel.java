package com.quanutrition.app.appointment;

public class AppointmentHistoryModel {

    private String id,date,time,dietitianName,type,status,notes;

    public AppointmentHistoryModel(String id, String date, String time, String dietitianName, String type, String status, String notes) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.dietitianName = dietitianName;
        this.type = type;
        this.status = status;
        this.notes = notes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDietitianName() {
        return dietitianName;
    }

    public void setDietitianName(String dietitianName) {
        this.dietitianName = dietitianName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
