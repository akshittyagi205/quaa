package com.quanutrition.app.googlefit;


import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import com.quanutrition.app.firebaseUtils.FirebaseUtils;

import java.util.ArrayList;

public class FirebaseSyncService extends JobService {
    private static final String TAG = FirebaseSyncService.class.getSimpleName();
    boolean isWorking = false;
    boolean jobCancelled = false;

    // Called by the Android system when it's time to run the job
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "Job started!");
        isWorking = true;
        // We need 'jobParameters' so we can call 'jobFinished'
        startWorkOnNewThread(jobParameters); // Services do NOT run on a separate thread

        return isWorking;
    }

    private void startWorkOnNewThread(final JobParameters jobParameters) {
        new Thread(new Runnable() {
            public void run() {
                doWork(jobParameters);
            }
        }).start();
    }

    private void doWork(final JobParameters jobParameters) {
        // 10 seconds of working (1000*10ms)


//        Tools.initCustomToast(this,"Service running!");
            // If the job has been cancelled, stop working; the job will be rescheduled.
            if (jobCancelled)
                return;

        final FirebaseUtils mFirebaseUtils = new FirebaseUtils(this);
//        mFirebaseUtils.addWater();

        GoogleFitUtils googleFitUtils = new GoogleFitUtils(getApplicationContext(), new GoogleFitUtils.OnDataReady() {
            @Override
            public void onStepsReady(String steps) {
//                Tools.initCustomToast(MainActivity.this,"Steps : "+steps);
                if(!steps.equalsIgnoreCase("-1"))
                mFirebaseUtils.syncStepsData(steps);

                Log.d("Job Working","Steps Sync : "+steps);
            }

            @Override
            public void onCaloriesReady(String totalCal, String walking, String running, String other) {
//                Tools.initCustomToast(MainActivity.this,"Calories : "+totalCal);
                if(!totalCal.equalsIgnoreCase("-1"))
                mFirebaseUtils.syncCaloriesData(totalCal);

                Log.d("Job Working","Cal Sync : "+totalCal);
                isWorking = false;
                boolean needsReschedule = false;
                jobFinished(jobParameters, needsReschedule);
            }

            @Override
            public void onWeeklyDataReady(ArrayList<String> days, ArrayList<String> steps) {

            }
        });
        googleFitUtils.setFlag(false);
        googleFitUtils.init();

//            try { Thread.sleep(10); } catch (Exception e) { }

        Log.d(TAG, "Job finished!");

    }

    // Called if the job was cancelled before being finished
    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d(TAG, "Job cancelled before being completed.");
        jobCancelled = true;
        boolean needsReschedule = isWorking;
        jobFinished(jobParameters, needsReschedule);
        return needsReschedule;
    }
}