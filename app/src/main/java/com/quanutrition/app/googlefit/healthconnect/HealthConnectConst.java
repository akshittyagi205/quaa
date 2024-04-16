package com.quanutrition.app.googlefit.healthconnect;

import android.os.Build;

public class HealthConnectConst {
    public static String HEALTH_CONNECT_ACTIVE = "heath_connect_active" ;
    // The minimum android level that can use Health Connect
    public static int MIN_SUPPORTED_SDK = Build.VERSION_CODES.O_MR1;
    public static String HEALTH_CONNECT_SETTINGS_ACTION = "androidx.health.ACTION_HEALTH_CONNECT_SETTINGS";
}
