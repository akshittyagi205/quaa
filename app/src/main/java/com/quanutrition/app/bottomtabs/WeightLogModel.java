package com.quanutrition.app.bottomtabs;

public class WeightLogModel {

    private String weight,date;

    public WeightLogModel(String weight, String date) {
        this.weight = weight;
        this.date = date;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
