package com.quanutrition.app.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class NotificationNetworkManager {
    private Context mCtx;
    private static NotificationNetworkManager mInstance;

    private NotificationNetworkManager(Context context) {
        mCtx = context;
    }

    public static synchronized NotificationNetworkManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new NotificationNetworkManager(context);
        }
        return mInstance;
    }

    public void sendNotification(Context context, final String title, final String body, final String tag){

        String apiURL = "http://nmamilifeapi.herokuapp.com/sendnotification/";
        Log.d("Calling URL",apiURL);
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHERED_PREF, Context.MODE_PRIVATE);
        final String userId = sharedPreferences.getString(Constants.USER_ID,"0");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response",response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("response",error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("userId",userId);
                params.put("title",title);
                params.put("tag",tag);
                params.put("body",body);
                return params;
            }

        };

        int MY_SOCKET_TIMEOUT_MS = 50000;
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);



    }

}
