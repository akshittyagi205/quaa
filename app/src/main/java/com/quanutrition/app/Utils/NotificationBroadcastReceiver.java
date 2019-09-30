package com.quanutrition.app.Utils;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class NotificationBroadcastReceiver extends BroadcastReceiver {

//    private static String KEY_NOTIFICATION_ID = "key_noticiation_id";
//    private static String KEY_MESSAGE_ID = "key_message_id";
//    public static Intent getReplyMessageIntent(Context context, int notificationId, int messageId) {
//        Intent intent = new Intent(context, NotificationBroadcastReceiver.class);
//        intent.setAction(Constants.YES_ACTION);
//        intent.putExtra(KEY_NOTIFICATION_ID, notificationId);
//        intent.putExtra(KEY_MESSAGE_ID, messageId);
//        return intent;
//    }
    public NotificationBroadcastReceiver() {
    }

    String mealNo="";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Reciever","I am here");
        int notificationId = intent.getIntExtra("KEY_NOTIFICATION_ID",0);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notificationId);
         if(Constants.YES_ACTION.equals(intent.getAction())){
             Log.d("Reciever","Yes clicked!");
//             PersistableBundle bundle = new PersistableBundle();
//             bundle.putString("foodtag",intent.getExtras().getString("foodtag"));
//             bundle.putString("food",intent.getExtras().getString("food"));
//             bundle.putString("tag","Yes");
//             ComponentName componentName = new ComponentName(context,ExampleJobService2.class);
//             JobInfo info = new JobInfo.Builder(1234,componentName)
//                     .setExtras(bundle)
//                     .setPersisted(false)
//                     .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
//                     .build();
//             JobScheduler scheduler = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);
//             int resultCode = scheduler.schedule(info);
//             if(resultCode==JobScheduler.RESULT_SUCCESS){
//                 Log.d("My Tag","Job Scheduled");
//             }else{
//                 Log.d("My Tag","Job not Scheduled");
//             }

             final SharedPreferences sharedpreferences = context.getSharedPreferences(Constants.SHERED_PREF, Context.MODE_PRIVATE);
             Calendar calendar = Calendar.getInstance();
             Date c = calendar.getTime();
             SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
             Bundle ob = intent.getExtras();



             if(ob.getString("foodtag").equals("1")){
                 mealNo="foodone";
             }else if(ob.getString("foodtag").equals("2")){
                 mealNo="foodtwo";
             }else if(ob.getString("foodtag").equals("3")){
                 mealNo="foodthree";
             }else if(ob.getString("foodtag").equals("4")){
                 mealNo="foodfour";
             }else if(ob.getString("foodtag").equals("5")){
                 mealNo="foodfive";
             }else if(ob.getString("foodtag").equals("6")){
                 mealNo="foodsix";
             }else if(ob.getString("foodtag").equals("7")){
                 mealNo="foodseven";
             }else if(ob.getString("foodtag").equals("8")){
                 mealNo="foodeight";
             }

             final String userId = String.valueOf(sharedpreferences.getInt("clientId", 0));
             final String date = df.format(c).split(" ")[0].trim();

             sendR(userId,date,ob.getString("food"),context);
//            Toast.makeText(context,"Yes clicked",Toast.LENGTH_SHORT).show();
        }
        else if(Constants.NO_ACTION.equals(intent.getAction())){
             Log.d("Reciever","No clicked!");
//             PersistableBundle bundle = new PersistableBundle();
//             bundle.putString("data",intent.getExtras().getString("data"));
//             bundle.putString("tag","No");
//             ComponentName componentName = new ComponentName(context,ExampleJobService2.class);
//             JobInfo info = new JobInfo.Builder(1234,componentName)
//                     .setExtras(bundle)
//                     .setPersisted(false)
//                     .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
//                     .build();
//             JobScheduler scheduler = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);
//             int resultCode = scheduler.schedule(info);
//             if(resultCode==JobScheduler.RESULT_SUCCESS){
//                 Log.d("My Tag","Job Scheduled");
//             }else{
//                 Log.d("My Tag","Job not Scheduled");
//             }
             Intent i = new Intent();
             i.putExtra("foodtag",intent.getExtras().getString("foodtag"));
             i.putExtra("from","notification");
             i.setClassName("com.dietitianshreya.eatrightdiet", "com.dietitianshreya.eatrightdiet.DietDiary");
             i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             context.startActivity(i);
//            Toast.makeText(context,"No clicked",Toast.LENGTH_SHORT).show();
        }
    }

    void sendR(final String userId, final String date, final String text, final Context context){

        final JSONObject ob1 = new JSONObject();
        try {
            ob1.put(mealNo,text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("Job Scheduler","Sending request "+userId+" date: "+date+" Data : "+ob1);
        String url = "https://shreyaapi.herokuapp.com/enterdiary/";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject result = new JSONObject();

                        try {
                            result = new JSONObject(response);
                            if (result.getInt("res") == 1) {
                                Toast.makeText(context,"Response Recorded!", Toast.LENGTH_SHORT).show();
                                Log.d("Tag","Job finished");

                            } else {

                                Log.d("Task", "Not done");
                                Toast.makeText(context,"Some Error occured!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context,"Some Error occured!", Toast.LENGTH_SHORT).show();

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,"Some Error occured!", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(getApplicationContext(),"Something went wrong!\nCheck your Internet connection and try again..", Toast.LENGTH_LONG).show();
                        //Toast.makeText(MedicineData.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userId", userId);
                params.put("dietdata", ob1+"");
                params.put("dateofdiet",date);
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
