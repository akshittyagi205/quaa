package com.quanutrition.app.Utils;

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
import com.quanutrition.app.appointment.AppointmentsActivity;
import com.quanutrition.app.chat.ChatActivity;
import com.quanutrition.app.diet.DietPlanViewActivity;
import com.quanutrition.app.general.DailyDiaryActivity;
import com.quanutrition.app.payments.PaymentHistory;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.content.Context.NOTIFICATION_SERVICE;


public class MyNotificationManager {

    private Context mCtx;
    private static MyNotificationManager mInstance;

    private MyNotificationManager(Context context) {
        mCtx = context;
    }

    public static synchronized MyNotificationManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MyNotificationManager(context);
        }
        return mInstance;
    }

    public void displayNotification(String title, String body, String tag, String ChannelID) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mCtx,ChannelID)
                        .setSmallIcon(R.drawable.qua_nutrition_notification)
                        .setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.mipmap.ic_launcher))
                        .setContentTitle(title)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                        .setColor(ContextCompat.getColor(mCtx,R.color.colorAccent))
                        .setContentText(body);


        if(Build.VERSION.SDK_INT< Build.VERSION_CODES.O){
            mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }
        mBuilder.setAutoCancel(true);
        Intent resultIntent;
        if (tag.equals("-2")) {
            resultIntent = new Intent(mCtx, PaymentHistory.class);
            mBuilder.setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.drawable.bill));
        } else if(tag.equals("1")){
            resultIntent = new Intent(mCtx, ChatActivity.class);
            mBuilder.setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.drawable.chat));
            resultIntent.putExtra("tag", "1");
        }else if(tag.equals("3")){
            resultIntent = new Intent(mCtx, DietPlanViewActivity.class);
            mBuilder.setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.drawable.dinner));
            resultIntent.putExtra("tag", "3");
        }else if(tag.equals("2")){
            resultIntent = new Intent(mCtx, AppointmentsActivity.class);
            mBuilder.setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.drawable.calendar));
            resultIntent.putExtra("tag", "2");
        }else if (tag.equals("-3")) {
            resultIntent = new Intent(mCtx, DailyDiaryActivity.class);
            mBuilder.setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.drawable.diary));
        }else{
            resultIntent = new Intent(mCtx, MainActivity.class);
            mBuilder.setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.drawable.notification));
        }

        resultIntent.putExtra("KEY_NOTIFICATION_ID", 1);
        PendingIntent pendingIntent = PendingIntent.getActivity(mCtx, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        NotificationManager mNotifyMgr =
                (NotificationManager) mCtx.getSystemService(NOTIFICATION_SERVICE);
        SharedPreferences mPrefs = mCtx.getSharedPreferences(Constants.MyPreferences, Context.MODE_PRIVATE);
        String userId = mPrefs.getString("userId", "0");
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        int id=1;
        if(!tag.equalsIgnoreCase("1"))
            id = Integer.parseInt(sdf.format(c.getTime()));
        if (mNotifyMgr != null && !userId.equals("0")) {
            mNotifyMgr.notify(id, mBuilder.build());
        }
    }

}