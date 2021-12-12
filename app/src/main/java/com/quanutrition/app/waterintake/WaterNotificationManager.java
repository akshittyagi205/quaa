package com.quanutrition.app.waterintake;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.quanutrition.app.MainActivity;
import com.quanutrition.app.R;
import com.quanutrition.app.Utils.Constants;

import static android.content.Context.NOTIFICATION_SERVICE;


public class WaterNotificationManager {

    private Context mCtx;
    private static WaterNotificationManager mInstance;

    private WaterNotificationManager(Context context) {
        mCtx = context;
    }

    public static synchronized WaterNotificationManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new WaterNotificationManager(context);
        }
        return mInstance;
    }

    public void displayNotification(String title, String body, String tag, String ChannelID) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mCtx,ChannelID)
                        .setSmallIcon(R.drawable.qua_nutrition_notification)
                        .setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.drawable.ic_water_notification))
                        .setContentTitle(title)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setColor(ContextCompat.getColor(mCtx,R.color.colorAccent))
                        .setContentText(body);


        if(Build.VERSION.SDK_INT< Build.VERSION_CODES.O){
            mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        int id=110011;

        Intent i = new Intent(mCtx,WaterActionReceiver.class);
        i.putExtra("add",1);
        i.setAction("Add_Water");

        /*if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.P) {
            IntentFilter filter = new IntentFilter("Add_Water");
            WaterActionReceiver receiver = new WaterActionReceiver();
            mCtx.registerReceiver(receiver, filter);
        }*/
        PendingIntent addIntent = PendingIntent.getBroadcast(mCtx, 0, i, 0);

//        mBuilder.addAction(R.drawable.ic_notification_glass,"+1 Glass",addIntent);

        mBuilder.setAutoCancel(true);
        Intent resultIntent;
        resultIntent = new Intent(mCtx, MainActivity.class);
        resultIntent.putExtra("KEY_NOTIFICATION_ID", id);
        PendingIntent pendingIntent = PendingIntent.getActivity(mCtx, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        NotificationManager mNotifyMgr =
                (NotificationManager) mCtx.getSystemService(NOTIFICATION_SERVICE);
        SharedPreferences mPrefs = mCtx.getSharedPreferences(Constants.MyPreferences, Context.MODE_PRIVATE);
        String userId = mPrefs.getString("userId", "0");
        if (mNotifyMgr != null && !userId.equals("0")) {
            mNotifyMgr.notify(id, mBuilder.build());
        }
    }

}