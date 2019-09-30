package com.quanutrition.app.waterintake;


import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import com.quanutrition.app.Utils.Tools;

import java.util.Calendar;

public class ReminderService extends JobService {
    private static final String TAG = ReminderService.class.getSimpleName();
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

    private void doWork(JobParameters jobParameters) {
        // 10 seconds of working (1000*10ms)

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(Tools.getGeneralSharedPref(getApplicationContext()).getString("to","22:00").split(":")[0]));
        c.set(Calendar.MINUTE, Integer.parseInt(Tools.getGeneralSharedPref(getApplicationContext()).getString("to","22:00").split(":")[1]));
        c.set(Calendar.SECOND,0);
        c.set(Calendar.MILLISECOND,0);

        Calendar c1 = Calendar.getInstance();
        c1.set(Calendar.HOUR_OF_DAY, Integer.parseInt(Tools.getGeneralSharedPref(getApplicationContext()).getString("from","10:00").split(":")[0]));
        c1.set(Calendar.MINUTE, Integer.parseInt(Tools.getGeneralSharedPref(getApplicationContext()).getString("from","10:00").split(":")[0]));
        c1.set(Calendar.SECOND,0);
        c1.set(Calendar.MILLISECOND,0);

//        Tools.initCustomToast(this,"Service running!");
            // If the job has been cancelled, stop working; the job will be rescheduled.
            if (jobCancelled)
                return;
            /*if(Calendar.getInstance().getTimeInMillis()<c.getTimeInMillis()&&Calendar.getInstance().getTimeInMillis()>c1.getTimeInMillis()) {
                Log.d("Job working", "Im here babe");
                if (android.os.Build.VERSION.SDK_INT >= 26) {
                    NotificationManager mNotificationManager2 =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    int importance2 = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel mChannel2 = new NotificationChannel(Constants.GENERAL_CHANNEL_ID, Constants.GENERAL_CHANNEL_NAME, importance2);
                    mNotificationManager2.createNotificationChannel(mChannel2);
                }
                if(getSharedPreferences(Constants.MyPreferences,Context.MODE_PRIVATE).getBoolean("setNow",false)){
                    SharedPreferences.Editor editor = Tools.getGeneralEditor(getApplicationContext());
                    editor.putBoolean("setNow",false);
                    editor.commit();
                }else
                WaterNotificationManager.getInstance(this).displayNotification("Water Reminder from service", "Hey It's Time to drink water babe!", "2", Constants.GENERAL_CHANNEL_ID);
            }*/
//            try { Thread.sleep(10); } catch (Exception e) { }

        Log.d(TAG, "Job finished!");
        isWorking = false;
        boolean needsReschedule = false;
        jobFinished(jobParameters, needsReschedule);
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