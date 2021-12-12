package com.quanutrition.app.firebaseUtils;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.quanutrition.app.Utils.Constants;
import com.quanutrition.app.Utils.Tools;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseUtils {
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mMessagesDatabaseReference;
    ChildEventListener mChildEventLintener;
    Context context;

    public interface OnGraphReady{
        void onDataReady(ArrayList<GraphModel> data);
    }

    public interface OnDataRecieved{
        void onDataReady(String data);
    }

    public interface OnDailyStatsReady{
        void onDataReady(String steps, String cal, String water);
    }

    public FirebaseUtils(Context context){
        this.context = context;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("GoogleFit").child(Tools.getGeneralSharedPref(context).getString(Constants.USER_ID,"-1"));
    }

    public void addWater(){
        mMessagesDatabaseReference.child(Tools.getFormattedDateToday()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.hasChild("water")) {
                    // run some code
                    mMessagesDatabaseReference.child(Tools.getFormattedDateToday()).child("water").setValue("1");
                }else{
                    Log.d("Reference",snapshot.toString());
                    int glasses = Integer.parseInt(snapshot.child("water").getValue().toString());
                    glasses++;
                    mMessagesDatabaseReference.child(Tools.getFormattedDateToday()).child("water").setValue(glasses+"");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void reduceWater(){
        mMessagesDatabaseReference.child(Tools.getFormattedDateToday()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.hasChild("water")) {
                    // run some code
                    mMessagesDatabaseReference.child(Tools.getFormattedDateToday()).child("water").setValue("0");
                }else{
                    Log.d("Reference",snapshot.toString());
                    int glasses = Integer.parseInt(snapshot.child("water").getValue().toString());
                    if(glasses==0){
                        mMessagesDatabaseReference.child(Tools.getFormattedDateToday()).child("water").setValue("0");
                    }else {
                        glasses--;
                        mMessagesDatabaseReference.child(Tools.getFormattedDateToday()).child("water").setValue(glasses + "");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setWater(String value){
        mMessagesDatabaseReference.child(Tools.getFormattedDateToday()).child("water").setValue(value);
    }

    public void syncStepsData(final String value){
        mMessagesDatabaseReference.child(Tools.getFormattedDateToday()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mMessagesDatabaseReference.child(Tools.getFormattedDateToday()).child("steps").setValue(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void syncStepsData(final String date,final String value){
        mMessagesDatabaseReference.child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mMessagesDatabaseReference.child(date).child("steps").setValue(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void syncCaloriesData(final String date,final String value){
        mMessagesDatabaseReference.child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mMessagesDatabaseReference.child(date).child("calories").setValue(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void syncCaloriesData(final String value){
        mMessagesDatabaseReference.child(Tools.getFormattedDateToday()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mMessagesDatabaseReference.child(Tools.getFormattedDateToday()).child("calories").setValue(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getWaterGraph(final OnGraphReady onGraphReady){
        final ArrayList<String> dates = Tools.getLastSevenDates();
        final ArrayList<GraphModel> values = new ArrayList<>();
        for(int i=0;i<7;i++){
            mMessagesDatabaseReference.child(dates.get(i).split(" ")[1]).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (!snapshot.hasChild("water")) {
                        values.add(new GraphModel(snapshot.getKey(),"0"));
                    }else{
                        values.add(new GraphModel(snapshot.getKey(),snapshot.child("water").getValue().toString()));
                    }
                    Log.d("values size",values.size()+"");
                    if(values.size()==7){
                        onGraphReady.onDataReady(values);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    public void getStepsGraph(final OnGraphReady onGraphReady){
        final ArrayList<String> dates = Tools.getLastSevenDates();
        final ArrayList<GraphModel> values = new ArrayList<>();
        for(int i=0;i<7;i++){
            mMessagesDatabaseReference.child(dates.get(i).split(" ")[1]).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (!snapshot.hasChild("steps")) {
                        values.add(new GraphModel(snapshot.getKey(),"0"));
                    }else{
                        values.add(new GraphModel(snapshot.getKey(),snapshot.child("steps").getValue().toString()));
                    }
                    Log.d("values size",values.size()+"");
                    if(values.size()==7){
                        onGraphReady.onDataReady(values);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    public void getCaloriesGraph(final OnGraphReady onGraphReady){
        final ArrayList<String> dates = Tools.getLastSevenDates();
        final ArrayList<GraphModel> values = new ArrayList<>();
        for(int i=0;i<7;i++){
            mMessagesDatabaseReference.child(dates.get(i).split(" ")[1]).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (!snapshot.hasChild("calories")) {
                        values.add(new GraphModel(snapshot.getKey(),"0"));
                    }else{
                        values.add(new GraphModel(snapshot.getKey(),snapshot.child("calories").getValue().toString()));
                    }
                    Log.d("values size",values.size()+"");
                    if(values.size()==7){
                        onGraphReady.onDataReady(values);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    public void getWaterToday(final OnDataRecieved onDataRecieved){
        mMessagesDatabaseReference.child(Tools.getFormattedDateToday()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.hasChild("water")) {
                    // run some code
                    onDataRecieved.onDataReady("0");
                }else{
                    onDataRecieved.onDataReady(snapshot.child("water").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Call for Diary
    public void getDataAnyDay(final OnDailyStatsReady onDataRecieved, String date){
        mMessagesDatabaseReference.child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String water="0",steps="0",cal="0";
                if (snapshot.hasChild("water")) {
                    water = snapshot.child("water").getValue().toString();
                }
                if (snapshot.hasChild("steps")) {
                    steps = snapshot.child("steps").getValue().toString();
                }
                if (snapshot.hasChild("calories")) {
                    cal = snapshot.child("calories").getValue().toString();
                }
                onDataRecieved.onDataReady(steps,cal,water);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                onDataRecieved.onDataReady("-","-","-");
            }
        });
    }
}
