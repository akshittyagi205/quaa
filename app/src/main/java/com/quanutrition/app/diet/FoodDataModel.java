package com.quanutrition.app.diet;

import java.util.ArrayList;

public class FoodDataModel {
    private String foodId,foodName,foodCalories,foodQuant,foodNotes;
    private boolean foodCalorieFlag;
    private ArrayList<FoodOptionsModel> optionsList;
    private String url = "";

    public FoodDataModel(String foodId, String foodName, String foodCalories, String foodQuant, String foodNotes, boolean foodCalorieFlag, ArrayList<FoodOptionsModel> optionsList) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodCalories = foodCalories;
        this.foodQuant = foodQuant;
        this.foodNotes = foodNotes;
        this.foodCalorieFlag = foodCalorieFlag;
        this.optionsList = optionsList;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodCalories() {
        return foodCalories;
    }

    public void setFoodCalories(String foodCalories) {
        this.foodCalories = foodCalories;
    }

    public String getFoodQuant() {
        return foodQuant;
    }

    public void setFoodQuant(String foodQuant) {
        this.foodQuant = foodQuant;
    }

    public String getFoodNotes() {
        return foodNotes;
    }

    public void setFoodNotes(String foodNotes) {
        this.foodNotes = foodNotes;
    }

    public boolean isFoodCalorieFlag() {
        return foodCalorieFlag;
    }

    public void setFoodCalorieFlag(boolean foodCalorieFlag) {
        this.foodCalorieFlag = foodCalorieFlag;
    }

    public ArrayList<FoodOptionsModel> getOptionsList() {
        return optionsList;
    }

    public void setOptionsList(ArrayList<FoodOptionsModel> optionsList) {
        this.optionsList = optionsList;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
