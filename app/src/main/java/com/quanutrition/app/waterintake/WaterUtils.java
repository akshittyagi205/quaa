package com.quanutrition.app.waterintake;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

public class WaterUtils {

    Context context;
    AppCompatActivity activity;
    public interface OnDataRecieved{
        void onDataRecieved(int glass, String goal);
    }
    OnDataRecieved onDataRecieved;
    public WaterUtils(Context context, OnDataRecieved onDataRecieved){
        this.context = context;
        this.onDataRecieved = onDataRecieved;
    }

    public void addWater(int glass){
        //sendRequest
    }

    public void getWater(){
        //sendRequest
        onDataRecieved.onDataRecieved(1,"8");
    }

}
