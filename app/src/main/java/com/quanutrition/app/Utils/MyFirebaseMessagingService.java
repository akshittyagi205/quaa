package com.quanutrition.app.Utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    Intent intent;
    String TAG="MY TAG";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        Log.d("Message",remoteMessage.getMessageId());
        Log.d("Reminder","Notification recieved");
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        if (android.os.Build.VERSION.SDK_INT >= 26) {
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(Constants.CHAT_CHANNEL_ID, Constants.CHAT_CHANNEL_NAME, importance);
            mNotificationManager.createNotificationChannel(mChannel);
            NotificationManager mNotificationManager1 =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance1 = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel1 = new NotificationChannel(Constants.APPOINTMENT_CHANNEL_ID, Constants.APPOINTMENT_CHANNEL_NAME, importance1);
            mNotificationManager1.createNotificationChannel(mChannel1);
            NotificationManager mNotificationManager2 =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance2 = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel2 = new NotificationChannel(Constants.GENERAL_CHANNEL_ID, Constants.GENERAL_CHANNEL_NAME, importance2);
            mNotificationManager2.createNotificationChannel(mChannel2);
        }
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            try {
                JSONObject data = new JSONObject(remoteMessage.getData());
                String tag = data.getString("tag");
                if(tag.equals("1")){
                    MyNotificationManager.getInstance(getApplicationContext()).displayNotification(data.getString("title"),data.getString("data"),tag,Constants.CHAT_CHANNEL_ID);
                }else if(tag.equals("2")){
                    MyNotificationManager.getInstance(getApplicationContext()).displayNotification(data.getString("title"),data.getString("data"),tag,Constants.APPOINTMENT_CHANNEL_ID);
                } else{
                    MyNotificationManager.getInstance(getApplicationContext()).displayNotification(data.getString("title"),data.getString("data"),tag,Constants.GENERAL_CHANNEL_ID);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getTitle());
            Log.d("Noti",remoteMessage.getNotification().getBody() + "  title  :      "+remoteMessage.getNotification().getTitle());
        }
        if (android.os.Build.VERSION.SDK_INT >= 26) {
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(Constants.ChannelID, Constants.CHANNEL_NAME, importance);
            mNotificationManager.createNotificationChannel(mChannel);
        }
        String body;
        if (android.os.Build.VERSION.SDK_INT >= 26) {
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(Constants.ChannelID, Constants.CHANNEL_NAME, importance);
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }
}
