package com.quanutrition.app.waterintake;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.quanutrition.app.firebaseUtils.FirebaseUtils;

import static android.content.Context.NOTIFICATION_SERVICE;

public class WaterActionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // an Intent broadcast.

        if(intent.hasExtra("add")){
            NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            manager.cancel(110011);
            Log.d("Adding a glass","In Water Reciever");
            final FirebaseUtils mFirebaseUtils = new FirebaseUtils(context);
            mFirebaseUtils.addWater();
        }

    }
}
