package com.quanutrition.app.Utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MyAlarmManager extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;
    static int lastTime=0;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("Alarm Manager Called", "Hey");
        final SharedPreferences sharedpreferences = Tools.getGeneralSharedPref(context);
//        final String userId = sharedpreferences.getString("userId", "0");
        String mealNo = "0";
        int mealTime = Integer.parseInt(intent.getExtras().getString("mealTime"));
        Calendar calendar = Calendar.getInstance();
        Date c = calendar.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String time = df.format(c).split(" ")[1].trim();
        int hour = Integer.parseInt(time.split(":")[0].trim());
        int min = Integer.parseInt(time.split(":")[1].trim());
        Log.d("Current time",time);
        String time1 = sharedpreferences.getString(intent.getExtras().getString("mealTime"),"-1:-1");
        Log.d("Time Should be",time1);
        int hour1 = Integer.parseInt(time1.split(":")[0].trim());
        int min1 = Integer.parseInt(time1.split(":")[1].trim());
        String Body="",Title="";
        if(intent.getExtras().getBoolean("status")) {
            if (hour == hour1) {
                if (Math.abs(min - min1) < 5) {
                    switch (mealTime) {
                        case 1:
                            Title = "Early Morning Meal Time";
                            Body = "Hey it's time to start your day with early morning meal";
                            break;
                        case 2:
                            Title = "Breakfast Meal Time";
                            Body = "Fuel up your day with a healthy breakfast";
                            break;
                        case 3:
                            Title = "Mid Morning Meal Time";
                            Body = "Feeling hungry? Time for Mid morning munching";
                            break;
                        case 4:
                            Title = "Lunch Meal Time";
                            Body = "Hey it's time to take a break from work and have lunch";
                            break;
                        case 5:
                            Title = "Evening Meal Time";
                            Body = "Evening meal can help you regain the lost energy";
                            break;
                        case 6:
                            Title = "Late Evening Meal Time";
                            Body = "Hey, it's late evening meal time";
                            break;
                        case 7:
                            Title = "Dinner Time";
                            Body = "End your day with a healthy dinner";
                            break;
                        case 8:
                            Title = "Post Dinner Meal Time";
                            Body = "Time to have post dinner meal before going to bed";
                            break;
                        case 9:
                            Title = "Meditation Reminder";
                            Body = "Hey, It's time to take a break and meditate for next 2 minutes.";
                            break;
                        case 10:
                            Title = "Update Diary";
                            Body = "Day is about to end, make sure you've updated your diary for today.";
                            break;
                        default:
                            Title = "";
                            Body = "";
                            break;
                    }

                    if (android.os.Build.VERSION.SDK_INT >= 26) {
                        NotificationManager mNotificationManager =
                                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        int importance = NotificationManager.IMPORTANCE_HIGH;
                        NotificationChannel mChannel = new NotificationChannel(Constants.REMINDER_CHANNEL_ID, Constants.REMINDER_CHANNEL_NAME, importance);
                        mNotificationManager.createNotificationChannel(mChannel);

                    }
                    Log.d("Sending Notification", Title + "   Akshit AKshit  " + Body);
                    if (mealTime < 10)
                        if(mealTime!=9)
                            MyNotificationManager.getInstance(context).displayNotification(Title, Body, "3", Constants.REMINDER_CHANNEL_ID);
                        else
                            MyNotificationManager.getInstance(context).displayNotification(Title, Body, "-6", Constants.REMINDER_CHANNEL_ID);

                    else
                        MyNotificationManager.getInstance(context).displayNotification(Title, Body, "-3", Constants.REMINDER_CHANNEL_ID);
                }
            }
        }

    }
}