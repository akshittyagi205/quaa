package com.quanutrition.app.diet;

public interface OnDiaryDataChanged {
    void onDataChanged(DiaryDataModel data, int pos);
    void onMealDataChange(DiaryDataModel data,String mealName,String mealTime,int pos,boolean isMealOption);
}
