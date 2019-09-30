package com.quanutrition.app.diet;

import java.util.ArrayList;

public class MealModel {

    private String mealId,mealName,mealTime,mealCalories,macros;
    private boolean timeFlag,calorieFlag;
    private ArrayList<FoodDataModel> mealData;
    private boolean macro_flag;
    private String mealNotes;
    private boolean recipe_flag;

    public MealModel(String mealId, String mealName, String mealTime, String mealCalories, boolean timeFlag, boolean calorieFlag, ArrayList<FoodDataModel> mealData) {
        this.mealId = mealId;
        this.mealName = mealName;
        this.mealTime = mealTime;
        this.mealCalories = mealCalories;
        this.timeFlag = timeFlag;
        this.calorieFlag = calorieFlag;
        this.mealData = mealData;
    }

    public String getMealId() {
        return mealId;
    }

    public void setMealId(String mealId) {
        this.mealId = mealId;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public String getMealTime() {
        return mealTime;
    }

    public void setMealTime(String mealTime) {
        this.mealTime = mealTime;
    }

    public String getMealCalories() {
        return mealCalories;
    }

    public void setMealCalories(String mealCalories) {
        this.mealCalories = mealCalories;
    }

    public boolean isTimeFlag() {
        return timeFlag;
    }

    public void setTimeFlag(boolean timeFlag) {
        this.timeFlag = timeFlag;
    }

    public boolean isCalorieFlag() {
        return calorieFlag;
    }

    public void setCalorieFlag(boolean calorieFlag) {
        this.calorieFlag = calorieFlag;
    }

    public ArrayList<FoodDataModel> getMealData() {
        return mealData;
    }

    public void setMealData(ArrayList<FoodDataModel> mealData) {
        this.mealData = mealData;
    }

    public String getMacros() {
        return macros;
    }

    public void setMacros(String macros) {
        this.macros = macros;
    }

    public String getMealNotes() {
        return mealNotes;
    }

    public void setMealNotes(String mealNotes) {
        this.mealNotes = mealNotes;
    }

    public boolean isMacro_flag() {
        return macro_flag;
    }

    public void setMacro_flag(boolean macro_flag) {
        this.macro_flag = macro_flag;
    }

    public boolean isRecipe_flag() {
        return recipe_flag;
    }

    public void setRecipe_flag(boolean recipe_flag) {
        this.recipe_flag = recipe_flag;
    }
}
