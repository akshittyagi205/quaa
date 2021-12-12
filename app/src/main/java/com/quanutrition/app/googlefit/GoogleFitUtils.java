package com.quanutrition.app.googlefit;

import android.Manifest;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessActivities;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.FitnessStatusCodes;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Subscription;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DailyTotalResult;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.fitness.result.DataReadResult;
import com.google.android.gms.fitness.result.ListSubscriptionsResult;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.quanutrition.app.Utils.Tools;
import com.quanutrition.app.firebaseUtils.FirebaseUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class GoogleFitUtils {

    String TAG = "Google Fit Activity";
    String STEPS="0",CALORIES="0";
    Float expendedCalories=0f;
    Float expendedCaloriesAr[] = {0f,0f,0f,0f,0f,0f,0f};
    float walking=0f,running=0f,other=0f;

    public interface OnDataReady{
        void onStepsReady(String steps);
        void onCaloriesReady(String totalCal, String walking, String running, String other);
        void onWeeklyDataReady(ArrayList<String> days, ArrayList<String> steps);
    }
    private OnDataReady onDataReady;
    private Context context;
    private boolean flag = true;
    AppCompatActivity activity;
    FitnessOptions fitnessOptions;
    private boolean weekly = false;

    public GoogleFitUtils(Context context, OnDataReady onDataReady){
        this.context = context;
        this.onDataReady = onDataReady;
        this.activity = (AppCompatActivity)context;
    }

    public void setFlag(boolean flag){
        this.flag = flag;
    }

    public void setWeekly(boolean weekly) {
        this.weekly = weekly;
    }

    public void init(){


        if(Build.VERSION.SDK_INT> Build.VERSION_CODES.P) {

            PackageManager pm = context.getPackageManager();
            int hasPerm = pm.checkPermission(
                    Manifest.permission.ACTIVITY_RECOGNITION,
                    context.getPackageName());
            if (hasPerm != PackageManager.PERMISSION_GRANTED) {
                if(flag) {
                    Dexter.withActivity((AppCompatActivity) context)
                            .withPermission(Manifest.permission.ACTIVITY_RECOGNITION)
                            .withListener(new PermissionListener() {
                                @Override
                                public void onPermissionGranted(PermissionGrantedResponse response) {
                                    startCode();
                                }

                                @Override
                                public void onPermissionDenied(PermissionDeniedResponse response) {
                                    Tools.initCustomToast(context, "Need permission to access activity");
                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                                }
                            }).check();
                }else{
                    onDataReady.onStepsReady("-1");
                    onDataReady.onCaloriesReady("-1","","","");
                }
            }else{
                startCode();
            }
        }else{
            startCode();
        }
    }

    void startCode() {
        fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
                .build();

        GoogleSignInAccount account = GoogleSignIn.getAccountForExtension(activity, fitnessOptions);

        if (!GoogleSignIn.hasPermissions(account, fitnessOptions)) {
            if(flag)
                GoogleSignIn.requestPermissions(activity, 0, account, fitnessOptions);
            else {
                onDataReady.onStepsReady("-1");
                onDataReady.onCaloriesReady("-1","","","");
            }
        } else {
            getDailySteps();
            getDailyCalories();
            if(weekly) {
                getWeeklySteps(); //sync data on firebase
                getWeeklyCalories();
            }
        }

    }

    private void getWeeklyCalories() {

        for(int i=0;i<7;i++) {

            final long endTime,startTime;
            final int pos = i;
            if(i==0) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                endTime = cal.getTimeInMillis();
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                startTime = cal.getTimeInMillis();
            }else{
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE,-1*i);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                startTime = cal.getTimeInMillis();


                cal.set(Calendar.HOUR_OF_DAY,23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);
                cal.set(Calendar.MILLISECOND, 0);
                endTime = cal.getTimeInMillis();
            }

            DataReadRequest readRequest = new DataReadRequest.Builder()
                    .aggregate(DataType.TYPE_CALORIES_EXPENDED, DataType.AGGREGATE_CALORIES_EXPENDED)
                    .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                    .bucketByActivityType(1, TimeUnit.SECONDS)
                    .build();

            GoogleSignInAccount account = GoogleSignIn
                    .getAccountForExtension(activity, fitnessOptions);

            Fitness.getHistoryClient(activity, account)
                    .readData(readRequest)
                    .addOnSuccessListener(new OnSuccessListener<DataReadResponse>() {
                        @Override
                        public void onSuccess(DataReadResponse dataReadResult) {

                            if (dataReadResult.getBuckets().size() > 0) {
                                for (Bucket bucket : dataReadResult.getBuckets()) {
                                    String bucketActivity = bucket.getActivity();
                                    Log.d("Activity",bucketActivity);
                                    if (!(bucketActivity.contains(FitnessActivities.IN_VEHICLE)||bucketActivity.contains(FitnessActivities.STILL))) {
                                        List<DataSet> dataSets = bucket.getDataSets();
                                        for (DataSet dataSet : dataSets) {
                                            for (DataPoint dp : dataSet.getDataPoints()) {
                                                if (dp.getEndTime(TimeUnit.MILLISECONDS) > dp.getStartTime(TimeUnit.MILLISECONDS)) {
                                                    for (Field field : dp.getDataType().getFields()) {
                                                        expendedCaloriesAr[pos] = expendedCaloriesAr[pos] + dp.getValue(field).asFloat();
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                float calFloat = expendedCaloriesAr[pos];
                                int calInt = (int)calFloat;
                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                new FirebaseUtils(context).syncCaloriesData(sdf.format(startTime),String.valueOf(calInt));



                            } else if (dataReadResult.getDataSets().size() > 0) {

                                for (DataSet dataSet : dataReadResult.getDataSets()) {
                                    for (DataPoint dp : dataSet.getDataPoints()) {
                                        if (dp.getEndTime(TimeUnit.MILLISECONDS) > dp.getStartTime(TimeUnit.MILLISECONDS)) {
                                            for (Field field : dp.getDataType().getFields()) {
                                                expendedCaloriesAr[pos] = expendedCaloriesAr[pos] + dp.getValue(field).asFloat();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });
        }
    }


    private void getDailyCalories() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        long endTime = cal.getTimeInMillis();
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        long startTime = cal.getTimeInMillis();

        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_CALORIES_EXPENDED, DataType.AGGREGATE_CALORIES_EXPENDED)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .bucketByActivityType(1, TimeUnit.SECONDS)
                .build();

        GoogleSignInAccount account = GoogleSignIn
                .getAccountForExtension(activity, fitnessOptions);

        Fitness.getHistoryClient(activity, account)
                .readData(readRequest)
                .addOnSuccessListener(new OnSuccessListener<DataReadResponse>() {
                    @Override
                    public void onSuccess(DataReadResponse dataReadResponse) {
                        printDataCal(dataReadResponse);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    private void printDataCal(DataReadResponse dataReadResult) {


        if (dataReadResult.getBuckets().size() > 0) {
            for (Bucket bucket : dataReadResult.getBuckets()) {
                String bucketActivity = bucket.getActivity();
                Log.d("Activity",bucketActivity);
                if (!(bucketActivity.contains(FitnessActivities.IN_VEHICLE)||bucketActivity.contains(FitnessActivities.STILL))) {
                    List<DataSet> dataSets = bucket.getDataSets();
                    for (DataSet dataSet : dataSets) {
                        dumpDataSet(dataSet,bucket.getActivity());
                    }
                }
            }

            float calFloat = expendedCalories;
            int calInt = (int)calFloat;
            CALORIES = String.valueOf(calInt);
            Log.e("Akshit cal Fitness",CALORIES);
            onDataReady.onCaloriesReady(CALORIES,walking+"",running+"",other+"");
//            onDataReady.onCaloriesReady(CALORIES,walking+"",running+"",other+"");



        } else if (dataReadResult.getDataSets().size() > 0) {

            for (DataSet dataSet : dataReadResult.getDataSets()) {
                dumpDataSet(dataSet,"");
            }
        }

        // [END parse_read_data_result]
    }

    // [START parse_dataset]
    private void dumpDataSet(DataSet dataSet, String activity) {

        for (DataPoint dp : dataSet.getDataPoints()) {
            if (dp.getEndTime(TimeUnit.MILLISECONDS) > dp.getStartTime(TimeUnit.MILLISECONDS)) {
                for (Field field : dp.getDataType().getFields()) {
                    /*if(activity.equalsIgnoreCase("walking")){
                        walking+=dp.getValue(field).asFloat();
                    }else if(activity.equalsIgnoreCase("running")){
                        running+=dp.getValue(field).asFloat();
                    }else{
                        other+=dp.getValue(field).asFloat();
                    }*/
                    expendedCalories = expendedCalories + dp.getValue(field).asFloat();
                }
            }
        }
    }

    private void getDailySteps() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        long endTime = cal.getTimeInMillis();
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        long startTime = cal.getTimeInMillis();

        DataReadRequest readRequest = new DataReadRequest.Builder()
                .read(DataType.AGGREGATE_STEP_COUNT_DELTA)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .bucketByTime(1, TimeUnit.DAYS)
                .build();

        GoogleSignInAccount account = GoogleSignIn
                .getAccountForExtension(activity, fitnessOptions);

        Fitness.getHistoryClient(activity, account)
                .readDailyTotal(DataType.AGGREGATE_STEP_COUNT_DELTA)
                .addOnSuccessListener(new OnSuccessListener<DataSet>() {
                    @Override
                    public void onSuccess(DataSet dataSet) {
                        long total = 0;
                        if (dataSet != null) {
                            total = dataSet.isEmpty()
                                    ? 0
                                    : dataSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();
//                    //Log.d("Step",totalSet.getDataPoints().get(0)+"");
                        }

                        Log.d("Total Steps",total+"");
                        onDataReady.onStepsReady(total+"");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    private int count;
    ArrayList<String> days;
    ArrayList<String> steps;

    private void getWeeklySteps() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY,23);
        cal.set(Calendar.MINUTE,59);
        cal.set(Calendar.SECOND,59);
        cal.set(Calendar.MILLISECOND,0);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.WEEK_OF_MONTH,-1);
        /*cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,1);
        cal.set(Calendar.SECOND,1);
        cal.set(Calendar.MILLISECOND,0);*/
        long startTime = cal.getTimeInMillis();

        days = new ArrayList<>();
        steps = new ArrayList<>();

        DataReadRequest readRequest = new DataReadRequest.Builder()
                .read(DataType.AGGREGATE_STEP_COUNT_DELTA)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .bucketByTime(1, TimeUnit.DAYS)
                .build();

        GoogleSignInAccount account = GoogleSignIn
                .getAccountForExtension(activity, fitnessOptions);

        Fitness.getHistoryClient(activity, account)
                .readData(readRequest)
                .addOnSuccessListener(new OnSuccessListener<DataReadResponse>() {
                    @Override
                    public void onSuccess(DataReadResponse dataReadResponse) {
                        printData(dataReadResponse);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    private void printData(DataReadResponse dataReadResult) {


        if (dataReadResult.getBuckets().size() > 0) {
            for (Bucket bucket : dataReadResult.getBuckets()) {
                Log.d("Activity",bucket.getActivity());
//                if (!(bucketActivity.contains(FitnessActivities.UNKNOWN)||bucketActivity.contains(FitnessActivities.IN_VEHICLE)||bucketActivity.contains(FitnessActivities.STILL))) {
                List<DataSet> dataSets = bucket.getDataSets();
                for (DataSet dataSet : dataSets) {
                    Log.d("Data",dataSet.getDataSource().getStreamName());
//                        Log.d("Data",dataSet.getDataPoints().toString());
                    dumpDataSet(dataSet);
                }
//                }
            }

         /*   float calFloat = expendedCalories;
            int calInt = (int)calFloat;
            CALORIES = String.valueOf(calInt);
            Log.d("Akshit cal",CALORIES);
            onDataReady.onCaloriesReady(CALORIES,walking+"",running+"",other+"");*/



        } else if (dataReadResult.getDataSets().size() > 0) {

            for (DataSet dataSet : dataReadResult.getDataSets()) {
                dumpDataSet(dataSet);
            }
        }

        // [END parse_read_data_result]
    }

    private void dumpDataSet(DataSet dataSet) {

        int step = 0;
        String day = "";
        for (DataPoint dp : dataSet.getDataPoints()) {
            step+=dp.getValue(Field.FIELD_STEPS).asInt();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE dd-MM-yyyy");
            day= simpleDateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)).split(" ")[1];
//            if (dp.getEndTime(TimeUnit.MILLISECONDS) > dp.getStartTime(TimeUnit.MILLISECONDS)) {
                /*for (Field field : dp.getDataType().getFields()) {
                    Log.e("Steps",dp.getValue(field)+"");
                }*/
//            }
        }

        new FirebaseUtils(context).syncStepsData(day,String.valueOf(step));
        steps.add(step+"");
        days.add(day);

//        if(steps.size()==7)
//            onDataReady.onWeeklyDataReady(days,steps);

        Log.e("Steps "+day,step+"");
    }
}
