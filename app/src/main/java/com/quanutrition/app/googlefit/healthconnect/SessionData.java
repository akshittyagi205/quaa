package com.quanutrition.app.googlefit.healthconnect;


public class SessionData {
    String date,totalEnergyBurned;

    public SessionData(String date, Long totalSteps, String totalEnergyBurned) {
        this.date = date;
        this.totalSteps = totalSteps;
        this.totalEnergyBurned = totalEnergyBurned;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getTotalSteps() {
        return totalSteps;
    }

    public void setTotalSteps(Long totalSteps) {
        this.totalSteps = totalSteps;
    }

    public String getTotalEnergyBurned() {
        return totalEnergyBurned;
    }

    public void setTotalEnergyBurned(String totalEnergyBurned) {
        this.totalEnergyBurned = totalEnergyBurned;
    }

    Long totalSteps;
}
