package com.quanutrition.app.googlefit;

import android.content.Context;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.android.gms.fitness.FitnessStatusCodes;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Subscription;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DailyTotalResult;
import com.google.android.gms.fitness.result.DataReadResult;
import com.google.android.gms.fitness.result.ListSubscriptionsResult;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class GoogleFitUtils {
    GoogleApiClient mClient;
    String TAG = "Google Fit Activity";
    String STEPS="0",CALORIES="0";
    Float expendedCalories=0f;
    float walking=0f,running=0f,other=0f;
    private int REQUEST_OAUTH = 1;
    private ResultCallback<Status> mSubscribeResultCallback;
    private ResultCallback<Status> mCancelSubscriptionResultCallback;
    private ResultCallback<ListSubscriptionsResult> mListSubscriptionsResultCallback;
    private boolean authInProgress = false;
    private static final String AUTH_PENDING = "auth_state_pending";
    public interface OnDataReady{
        void onStepsReady(String steps);
        void onCaloriesReady(String totalCal, String walking, String running, String other);
        void onWeeklyDataReady(ArrayList<String> days, ArrayList<String> steps);
    }
    private OnDataReady onDataReady;
    private Context context;
    private boolean flag = true;
    AppCompatActivity activity;

    public GoogleFitUtils(Context context, OnDataReady onDataReady){
        this.context = context;
        this.onDataReady = onDataReady;
        this.activity = (AppCompatActivity)context;
    }

    public void setFlag(boolean flag){
        this.flag = flag;
    }

    public void init(){
        googleFitCode();
        initCallbacks();
    }

    void googleFitCode(){
//        ad = Tools.getDialog("Loading...",this);
        mClient = new GoogleApiClient.Builder(context)
                .addApi(Fitness.HISTORY_API)
                .addApi(Fitness.RECORDING_API)
                .addApi(Fitness.CONFIG_API)
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ))
                .useDefaultAccount()
                .addConnectionCallbacks(
                        new GoogleApiClient.ConnectionCallbacks() {

                            @Override
                            public void onConnected(Bundle bundle) {
                                Fitness.RecordingApi.subscribe(mClient, DataType.TYPE_CALORIES_EXPENDED)
                                        .setResultCallback(mSubscribeResultCallback);
                                Fitness.RecordingApi.subscribe(mClient, DataType.AGGREGATE_STEP_COUNT_DELTA)
                                        .setResultCallback(mSubscribeResultCallback);

                                //Async To fetch steps
                                new GoogleFitUtils.FetchStepsAsync().execute();
                                Calendar c = Calendar.getInstance();
                                DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
                                String selecteddate = dateformat.format(c.getTime());
                                //Log.d("Selected date",selecteddate);
                                fetchUserGoogleFitData(selecteddate);
                                fetchWeeklyProgressData();
                                new GoogleFitUtils.FetchCalorieAsync().execute();

                            }

                            @Override
                            public void onConnectionSuspended(int i) {
                                // If your connection to the sensor gets lost at some point,
                                // you'll be able to determine the reason and react to it here.
                                if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
                                    //Log.i(TAG, "Connection lost.  Cause: Network Lost.");
                                } else if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
                                    //Log.i(TAG, "Connection lost.  Reason: Service Disconnected");
                                }
                            }
                        }
                ).addOnConnectionFailedListener(
                        new GoogleApiClient.OnConnectionFailedListener() {
                            // Called whenever the API client fails to connect.
                            @Override
                            public void onConnectionFailed(ConnectionResult result) {

                                if(flag) {
                                    //Log.i(TAG, "Connection failed. Cause: " + result.toString());
                                    if (!result.hasResolution()) {
                                        // Show the localized error dialog
                                        GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(),
                                                activity, 0).show();
                                        return;
                                    }
                                    // The failure has a resolution. Resolve it.
                                    // Called typically when the app is not yet authorized, and an
                                    // authorization dialog is displayed to the user.
                                    if (!authInProgress) {
                                        try {
                                            //Log.i(TAG, "Attempting to resolve failed connection");
                                            authInProgress = true;
                                            result.startResolutionForResult(activity, REQUEST_OAUTH);
                                        } catch (IntentSender.SendIntentException e) {
                                            //Log.e(TAG,
//                                                "Exception while starting resolution activity", e);
                                        }
                                    }
                                }else{
                                    onDataReady.onStepsReady("0");
                                    onDataReady.onCaloriesReady("0","","","");
                                }
                            }
                        }
                ).build();
        mClient.connect();
    }

    private void initCallbacks() {
        mSubscribeResultCallback = new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    if (status.getStatusCode() == FitnessStatusCodes.SUCCESS_ALREADY_SUBSCRIBED) {
                        //Log.e( "RecordingAPI", "Already subscribed to the Recording API");
                    } else {
                        //Log.e("RecordingAPI", "Subscribed to the Recording API");
                    }
                }
            }
        };

        mCancelSubscriptionResultCallback = new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    //Log.e( "RecordingAPI", "Canceled subscriptions!");
                } else {
                    // Subscription not removed
                    //Log.e("RecordingAPI", "Failed to cancel subscriptions");
                }
            }
        };

        mListSubscriptionsResultCallback = new ResultCallback<ListSubscriptionsResult>() {
            @Override
            public void onResult(@NonNull ListSubscriptionsResult listSubscriptionsResult) {
                for (Subscription subscription : listSubscriptionsResult.getSubscriptions()) {
                    DataType dataType = subscription.getDataType();
                    //Log.e( "RecordingAPI", dataType.getName() );
                    for (Field field : dataType.getFields() ) {
                        //Log.e( "RecordingAPI", field.toString() );
                    }
                }
            }
        };
    }

    private class FetchStepsAsync extends AsyncTask<Object, Object, Long> {
        protected Long doInBackground(Object... params) {
            long total = 0;
            PendingResult<DailyTotalResult> result = Fitness.HistoryApi.readDailyTotal(mClient, DataType.AGGREGATE_STEP_COUNT_DELTA);
            DailyTotalResult totalResult = result.await(30, TimeUnit.SECONDS);
            if (totalResult.getStatus().isSuccess()) {
                DataSet totalSet = totalResult.getTotal();
                if (totalSet != null) {
                    total = totalSet.isEmpty()
                            ? 0
                            : totalSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();
//                    //Log.d("Step",totalSet.getDataPoints().get(0)+"");
                }
            } else {
                //Log.w(TAG, "There was a problem getting the step count.");
            }
            return total;
        }


        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);

            //Total steps covered for that day
//            Toast.makeText(getApplicationContext(), "Total steps: " + aLong, Toast.LENGTH_SHORT).show();
            STEPS = aLong+"";
//            ((TextView)findViewById(R.id.steps)).setText(STEPS + "");
            onDataReady.onStepsReady(STEPS);
            //Log.i(TAG, "Total steps: " + aLong);

        }
    }

    private class FetchCalorieAsync extends AsyncTask<Object, Object, Float> {
        protected Float doInBackground(Object... params) {
            Float total = 0.0f;
            PendingResult<DailyTotalResult> result = Fitness.HistoryApi.readDailyTotal(mClient, DataType. TYPE_CALORIES_EXPENDED);
            DailyTotalResult totalResult = result.await(30, TimeUnit.SECONDS);
            if (totalResult.getStatus().isSuccess()) {
                DataSet totalSet = totalResult.getTotal();
                if (totalSet != null) {
                    total = totalSet.isEmpty()
                            ? 0
                            : totalSet.getDataPoints().get(0).getValue(Field.FIELD_CALORIES).asFloat();
                }
            } else {
                //Log.w(TAG, "There was a problem getting the calories.");
            }
            return total;
        }


        @Override
        protected void onPostExecute(Float aLong) {
            super.onPostExecute(aLong);

            //Total calories burned for that day
            //Log.i(TAG, "Total calories: " + aLong);

        }
    }


    public void fetchUserGoogleFitData(String date) {
        if (mClient != null && mClient.isConnected()) {

            Date d1 = null;
            try {
                d1 = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            } catch (Exception e) {

            }
            Calendar calendar = Calendar.getInstance();

            try {
                calendar.setTime(d1);
            } catch (Exception e) {
                calendar.setTime(new Date());
            }
            DataReadRequest readRequest = queryDateFitnessData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            new GoogleFitUtils.GetCaloriesAsyncTask(readRequest, mClient).execute();

        }
    }

    private DataReadRequest queryDateFitnessData(int year, int month, int day_of_Month) {

        Calendar startCalendar = Calendar.getInstance(Locale.getDefault());
        startCalendar.set(Calendar.YEAR, year);
        startCalendar.set(Calendar.MONTH, month);
        startCalendar.set(Calendar.DAY_OF_MONTH, day_of_Month);
        startCalendar.set(Calendar.HOUR_OF_DAY, 23);
        startCalendar.set(Calendar.MINUTE, 59);
        startCalendar.set(Calendar.SECOND, 59);
        startCalendar.set(Calendar.MILLISECOND, 999);
        long endTime = startCalendar.getTimeInMillis();


        startCalendar.set(Calendar.HOUR_OF_DAY, 0);
        startCalendar.set(Calendar.MINUTE, 0);
        startCalendar.set(Calendar.SECOND, 0);
        startCalendar.set(Calendar.MILLISECOND, 0);
        long startTime = startCalendar.getTimeInMillis();

        return new DataReadRequest.Builder()
                // The data request can specify multiple data types to return, effectively
                // combining multiple data queries into one call.
                // In this example, it's very unlikely that the request is for several hundred
                // datapoints each consisting of a few steps and a timestamp.  The more likely
                // scenario is wanting to see how many steps were walked per day, for 7 days.
                //.aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                //.aggregate(DataType.TYPE_CALORIES_EXPENDED,DataType.AGGREGATE_CALORIES_EXPENDED)
                .aggregate(DataType.TYPE_CALORIES_EXPENDED, DataType.AGGREGATE_CALORIES_EXPENDED)
                // .read(DataType.TYPE_CALORIES_EXPENDED)
                // Analogous to a "Group By" in SQL, defines how data should be aggregated.
                // bucketByTime allows for a time span, whereas bucketBySession would allow
                // bucketing by "sessions", which would need to be defined in code.
                //.bucketByTime(1, TimeUnit.DAYS)
                .bucketByActivitySegment(1, TimeUnit.MILLISECONDS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();

    }

    public class GetCaloriesAsyncTask extends AsyncTask<Void, Void, DataReadResult> {
        DataReadRequest readRequest;
        String TAG = "GoogleFitUtils";
        GoogleApiClient mClient = null;

        public GetCaloriesAsyncTask(DataReadRequest dataReadRequest_, GoogleApiClient googleApiClient) {
            this.readRequest = dataReadRequest_;
            this.mClient = googleApiClient;
        }

        @Override
        protected DataReadResult doInBackground(Void... params) {
            return Fitness.HistoryApi.readData(mClient, readRequest).await(1, TimeUnit.MINUTES);
        }

        @Override
        protected void onPostExecute(DataReadResult dataReadResult) {
            super.onPostExecute(dataReadResult);
            printData(dataReadResult);
        }

    }

    private void printData(DataReadResult dataReadResult) {


        if (dataReadResult.getBuckets().size() > 0) {
            for (Bucket bucket : dataReadResult.getBuckets()) {
                String bucketActivity = bucket.getActivity();
                if (!(bucketActivity.contains(FitnessActivities.UNKNOWN)||bucketActivity.contains(FitnessActivities.IN_VEHICLE)||bucketActivity.contains(FitnessActivities.STILL))) {
                    List<DataSet> dataSets = bucket.getDataSets();
                    for (DataSet dataSet : dataSets) {
                        dumpDataSet(dataSet,bucket.getActivity());
                    }
                }
            }

            float calFloat = expendedCalories;
            int calInt = (int)calFloat;
            CALORIES = String.valueOf(calInt);
            Log.d("Akshit cal",CALORIES);
            onDataReady.onCaloriesReady(CALORIES,walking+"",running+"",other+"");



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
                    if(activity.equalsIgnoreCase("walking")){
                        walking+=dp.getValue(field).asFloat();
                    }else if(activity.equalsIgnoreCase("running")){
                        running+=dp.getValue(field).asFloat();
                    }else{
                        other+=dp.getValue(field).asFloat();
                    }
                    expendedCalories = expendedCalories + dp.getValue(field).asFloat();
                }
            }
        }
    }

    private void fetchWeeklyProgressData() {
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        cal.set(Calendar.HOUR_OF_DAY,23);
        cal.set(Calendar.MINUTE,59);
        cal.set(Calendar.SECOND,59);
        cal.set(Calendar.MILLISECOND,999);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        long startTime = cal.getTimeInMillis();

        java.text.DateFormat dateFormat = DateFormat.getDateInstance();


//Check how many steps were walked and recorded in the last 7 days
        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();
//                DataReadRequest readRequest = queryWeeklyStepsData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        new GoogleFitUtils.GetWeeklyStepsAsyncTask(readRequest, mClient,0).execute();

    }

    private int count;
    ArrayList<String> days;
    ArrayList<String> steps;

    public class GetWeeklyStepsAsyncTask extends AsyncTask<Void, Void, DataReadResult> {
        DataReadRequest readRequest;
        String TAG = GoogleFitUtils.GetCaloriesAsyncTask.class.getName();
        GoogleApiClient mClient = null;
        int position;

        public GetWeeklyStepsAsyncTask(DataReadRequest dataReadRequest_, GoogleApiClient googleApiClient, int position) {
            this.readRequest = dataReadRequest_;
            this.mClient = googleApiClient;
            this.position = position;
        }

        @Override
        protected DataReadResult doInBackground(Void... params) {
            return Fitness.HistoryApi.readData(mClient, readRequest).await(1, TimeUnit.MINUTES);
        }

        @Override
        protected void onPostExecute(DataReadResult dataReadResult) {
            super.onPostExecute(dataReadResult);
//            printDataWeekly(dataReadResult,position);
            count=6;
            days= new ArrayList<>();
            steps = new ArrayList<>();
            displayLastWeeksData(dataReadResult,position);
        }

    }

    void displayLastWeeksData(DataReadResult dataReadResult, int position){
        //Used for aggregated data
        if (dataReadResult.getBuckets().size() > 0) {
            for (Bucket bucket : dataReadResult.getBuckets()) {
                List<DataSet> dataSets = bucket.getDataSets();
                for (DataSet dataSet : dataSets) {
                    showDataSet(dataSet);
                }
            }
        }
//Used for non-aggregated data
        else if (dataReadResult.getDataSets().size() > 0) {
            //Log.e("History", "Number of returned DataSets: " + dataReadResult.getDataSets().size());
            for (DataSet dataSet : dataReadResult.getDataSets()) {
//                showDataSet(dataSet);
            }
        }
    }


    private void showDataSet(DataSet dataSet) {
        //Log.e("History", "Data returned for Data type: " + dataSet.getDataType().getName());
        DateFormat dateFormat = DateFormat.getDateInstance();
        DateFormat timeFormat = DateFormat.getTimeInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd-MM-yyyy");

        for (DataPoint dp : dataSet.getDataPoints()) {
            //Log.e("History", "Data point:");
            //Log.e("History", "\tType: " + dp.getDataType().getName());
            //Log.e("History", "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)) + " " + timeFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            //Log.e("History", "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)) + " " + timeFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            days.add(sdf.format(dp.getEndTime(TimeUnit.MILLISECONDS)).split(" ")[0]);
            for (Field field : dp.getDataType().getFields()) {
                //Log.e("History", "\tField: " + field.getName() +
//                        " Value: " + dp.getValue(field));
//                ((ProgressBar)findViewById(progressBars[count])).setProgress((dp.getValue(field).asInt())/100);
                steps.add(dp.getValue(field).asInt()+"");
                //Log.d("Progress Value",((dp.getValue(field).asInt())/10000)+"");
            }
            count--;
        }
        onDataReady.onWeeklyDataReady(days,steps);
    }
}
