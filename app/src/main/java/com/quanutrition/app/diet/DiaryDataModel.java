package com.quanutrition.app.diet;

public class DiaryDataModel {
    private String id,name,otherfood;
    private boolean taken,option;
//    private MacroModel foodMacros;

    public DiaryDataModel(String id, String name, String otherfood, boolean taken, boolean option) {
        this.id = id;
        this.name = name;
        this.otherfood = otherfood;
        this.taken = taken;
        this.option = option;
//        this.foodMacros = foodMacros;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOtherfood() {
        return otherfood;
    }

    public void setOtherfood(String otherfood) {
        this.otherfood = otherfood;
    }

    public boolean isTaken() {
        return taken;
    }

    public void setTaken(boolean taken) {
        this.taken = taken;
    }

    public boolean isOption() {
        return option;
    }

    public void setOption(boolean option) {
        this.option = option;
    }

    /*public MacroModel getFoodMacros() {
        return foodMacros;
    }

    public void setFoodMacros(MacroModel foodMacros) {
        this.foodMacros = foodMacros;
    }*/
}
