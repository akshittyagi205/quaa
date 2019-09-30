package com.quanutrition.app.Utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.quanutrition.app.waterintake.WaterReminderReciever;

import java.util.Calendar;

public class SampleBootReciever extends BroadcastReceiver {
    Context mCtx;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // Set the alarm here.
            mCtx = context;
            setUpAlarm();
            setUpWaterReminder();
        }
    }
    void setUpAlarm(){
        SharedPreferences sharedPreferences = Tools.getGeneralSharedPref(mCtx);
        for(int i=1;i<=10;i++){
            String time = sharedPreferences.getString(i+"","0:0");
            boolean status = sharedPreferences.getBoolean(i+"Status",false);
            int hour = Integer.parseInt(time.split(":")[0]);
            int min = Integer.parseInt(time.split(":")[1]);
            setAlarm(i+"",hour,min,status);
        }
    }
    public void setAlarm(String mealTime, int time, int min, boolean status){
        AlarmManager alarmMgr;
        PendingIntent alarmIntent;
        alarmMgr = (AlarmManager)mCtx.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(mCtx, MyAlarmManager.class);
        intent.putExtra("mealTime",mealTime);
        intent.putExtra("status",status);
        alarmIntent = PendingIntent.getBroadcast(mCtx, Integer.parseInt(mealTime), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, time);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND,0);

        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 60 * 60*24, alarmIntent);
    }

    void setUpWaterReminder(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(Tools.getGeneralSharedPref(mCtx).getString("from","10:00").split(":")[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(Tools.getGeneralSharedPref(mCtx).getString("from","10:00").split(":")[1]));
        AlarmManager alarmMgr;
        PendingIntent alarmIntent;
        alarmMgr = (AlarmManager)mCtx.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(mCtx, WaterReminderReciever.class);
        alarmIntent = PendingIntent.getBroadcast(mCtx, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                Tools.getGeneralSharedPref(mCtx).getLong("interval",60*60*1000), alarmIntent);
    }


}
