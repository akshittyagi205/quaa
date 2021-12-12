package com.quanutrition.app.waterintake;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.quanutrition.app.Utils.Constants;
import com.quanutrition.app.Utils.Tools;

import java.util.Calendar;

public class WaterReminderReciever extends BroadcastReceiver {

    /*String body[] = {"Water is the only beverage which has no side effects, drink enough to stay clean",
            "Drink water to maintain a healthy body fluid balance by regulating body temperature, digest food and transport nutrients in the body.",
            "Keep your body weight under control by drinking water.",
            "Give that extra boost of energy to your muscles by drinking water.",
            "Maintain a glowing, clear and healthy skin by drinking water, it flushes out the toxins causing acnes and pimples.",
            "Our kidneys need extra fluid to clean away what we don’t need in our body. Let’s drink water.",
            "Stay refreshed and alert by drinking a glass of water, which helps in improving concentration.",
            "Get rid of tiredness and fatigue by gulping a big tumbler of water.",
            "Dehydration causes muscle cramps and aching joints. Drink enough water to stay out of dehydration.",
            "Suffering from indigestion, get to drink water to keep things moving smoothly within your internal system.",
            "Studies have revealed that students getting drinking water to exam rooms have scored better. Give that extra boost to your brain with water."};
    */
    String body[] = {
            "The centre of Hunger and Thirst are next to each other in the brain...May be you arent Hungry..Likely you are thirsty",
            "Water regulates our body temperature...Drink a glass now",
            "Feeling bloated? Your body could be retaining water...Drink a glass Now",
            "Give that extra boost of energy to your muscles by drinking water - Drink a Glass now",
            "Water Helps you flush out toxins...Drink a glass now..",
            "Water Water Water...May be its your Body Screaming...Drink a Glass Now",
            "Stay refreshed and alert by drinking a glass of water..Drink a Glass Now",
            "You could feel fatigued if you are dehydrated...Drink a Glass Now",
            "Hoping for a good nights sleep? Stay Hydrated...Drink a Glass Now..",
            "Suffering from indigestion ? Let Water Help you digest better...Drink a Glass Now",
            "Working Late? Stay Hydrated..Stay Focused...Drink a Glass Now"
    };


    @Override
    public void onReceive(Context context, Intent intent) {
        if (android.os.Build.VERSION.SDK_INT >= 26) {
            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(Constants.WATER_CHANNEL, Constants.WATER_CHANNEL_NAME, importance);
            mNotificationManager.createNotificationChannel(mChannel);
        }

        Calendar c = Calendar.getInstance();
        Log.d("Time", "time" + c.get(Calendar.HOUR_OF_DAY) + " : " + c.get(Calendar.MINUTE));
        String Title = "Water Reminder!";
        String Body = "Hey, It's time to hydrate yourself";
        SharedPreferences sharedPreferences = Tools.getGeneralSharedPref(context);
        String endTime = sharedPreferences.getString("to", "22:00");
        int count = sharedPreferences.getInt("count", 0);
        Calendar c1 = Calendar.getInstance();
        c1.set(Calendar.HOUR_OF_DAY, Integer.parseInt(endTime.split(":")[0]));
        c1.set(Calendar.MINUTE, Integer.parseInt(endTime.split(":")[1]));
        count++;
        if (count < 11) {
            Body = body[count];
        } else {
            count = 0;
            Body = body[count];
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("count", count);
        editor.commit();

        Calendar now = Calendar.getInstance();

        Calendar start = Calendar.getInstance();
        start.set(Calendar.HOUR_OF_DAY, Integer.parseInt(Tools.getGeneralSharedPref(context).getString("from", "10:00").split(":")[0]));
        start.set(Calendar.MINUTE, Integer.parseInt(Tools.getGeneralSharedPref(context).getString("from", "10:00").split(":")[1]));
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);

        Calendar end = Calendar.getInstance();
        end.set(Calendar.HOUR_OF_DAY, Integer.parseInt(Tools.getGeneralSharedPref(context).getString("to", "22:00").split(":")[0]));
        end.set(Calendar.MINUTE, Integer.parseInt(Tools.getGeneralSharedPref(context).getString("to", "22:00").split(":")[1]));
        end.set(Calendar.SECOND, 0);
        end.set(Calendar.MILLISECOND, 0);

        boolean waterFlag = Tools.getGeneralSharedPref(context).getBoolean(Constants.NOTIFICATION_WATER,true);

        if(now.getTimeInMillis()>start.getTimeInMillis()&&now.getTimeInMillis()<end.getTimeInMillis())
            if(waterFlag)
                WaterNotificationManager.getInstance(context).displayNotification(Title, Body, "11", Constants.WATER_CHANNEL);


    }
}
