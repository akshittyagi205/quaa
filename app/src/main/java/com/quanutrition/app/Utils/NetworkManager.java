package com.quanutrition.app.Utils;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.quanutrition.app.BuildConfig;
import com.quanutrition.app.general.SignInActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NetworkManager {
    private Context mCtx;
    private static NetworkManager mInstance;

    public static String BASE_URL = BuildConfig.BASE_URL;

    public static int MY_SOCKET_TIMEOUT_MS_SHORT = 30000;
    public static int MY_SOCKET_TIMEOUT_MS = 60000;

    private NetworkManager(Context context) {
        mCtx = context;
    }

    public static synchronized NetworkManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new NetworkManager(context);
        }
        return mInstance;
    }

    public interface OnAPIResponse{
        void onResponse(String response);
        void onError();
    }


    public void sendPostRequest(final String url, final Map<String, String> params, final Response.Listener<String> listener, final Response.ErrorListener errorListener, final Context context){

        String apiURL = BASE_URL+url;
        Log.d("Call to URL ",apiURL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiURL,listener,errorListener)
        {
            @Override
            protected Map<String, String> getParams() {
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

    public void sendRequestOTPRequest(final String url, final Map<String, String> params, final Response.Listener<String> listener, final Response.ErrorListener errorListener, final Context context){

        String apiURL = BASE_URL+url;
//        Log.d("Call to URL ",apiURL);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, apiURL,new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(context!=null)
                        listener.onResponse(response.toString());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(context!=null) {
                    errorListener.onErrorResponse(error);
                    if (error.toString().equalsIgnoreCase("com.android.volley.AuthFailureError")) {
                        finishActivity(context);
                    }else if(error.networkResponse.statusCode==429){
                        Tools.initCustomToast(context,"Maximum requests passed for today, try again tomorrow or contact your practitioner");
                    }else if(error.networkResponse.statusCode==408){
                        Tools.initCustomToast(context,"Your internet is unstable, please check your network connection");
                    }else{
                        Tools.initNetworkErrorToast(context);
                    }
                }
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("User-Agent","Zoconut/"+Tools.getGeneralSharedPref(context).getString(Constants.AUTH_TOKEN,""));
                if(BuildConfig.DEBUG){
                    params.put("Connection", "Keep-Alive");
                }
//                params.put("Authorization","token 2c56c0336d6f050d941d276046e99ec0fd62b4fb");
                Log.d("Params",params.toString());
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public void sendPostRequestWithHeader(final String url, final Map<String, String> params, final Response.Listener<String> listener, final Response.ErrorListener errorListener, final Context context){

        String apiURL = BASE_URL+url;
        Log.d("Call to URL ",apiURL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiURL,listener,errorListener)
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                if(BuildConfig.TEST_TOKEN.equalsIgnoreCase("NO"))
                    params.put("Authorization","token "+Tools.getGeneralSharedPref(context).getString(Constants.AUTH_TOKEN,""));
                else
                    params.put("Authorization","token "+BuildConfig.TOKEN);
                if(BuildConfig.DEBUG){
                    params.put("Connection", "Keep-Alive");
                }
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
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

    public void sendPostRequestWithHeader(final String url, final Map<String, String> params,final Context context,final OnAPIResponse onAPIResponse){

        String apiURL = BASE_URL+url;
        Log.d("Call to URL ",apiURL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                onAPIResponse.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onAPIResponse.onError();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                if(BuildConfig.TEST_TOKEN.equalsIgnoreCase("NO"))
                    params.put("Authorization","token "+Tools.getGeneralSharedPref(context).getString(Constants.AUTH_TOKEN,""));
                else
                    params.put("Authorization","token "+BuildConfig.TOKEN);
                if(BuildConfig.DEBUG){
                    params.put("Connection", "Keep-Alive");
                }
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
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

    public void sendRawPOstRequest(final String url, final String params, final Response.Listener<String> listener, final Response.ErrorListener errorListener, final Context context){

        String apiURL = BASE_URL+url;
        Log.d("Call to URL ",apiURL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiURL,listener,errorListener)
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                if(BuildConfig.TEST_TOKEN.equalsIgnoreCase("NO"))
                    params.put("Authorization","token "+Tools.getGeneralSharedPref(context).getString(Constants.AUTH_TOKEN,""));
                else
                    params.put("Authorization","token "+BuildConfig.TOKEN);
                if(BuildConfig.DEBUG){
                    params.put("Connection", "Keep-Alive");
                }
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return params.getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
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

    public void sendGetRequest(final String url, final Response.Listener<String> listener, final Response.ErrorListener errorListener, final Context context){

        String apiURL = BASE_URL+url;
        Log.d("Call to URL ",apiURL);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiURL,listener,errorListener)
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                if(BuildConfig.TEST_TOKEN.equalsIgnoreCase("NO"))
                    params.put("Authorization","token "+Tools.getGeneralSharedPref(context).getString(Constants.AUTH_TOKEN,""));
                else
                    params.put("Authorization","token "+BuildConfig.TOKEN);
                if(BuildConfig.DEBUG){
                    params.put("Connection", "Keep-Alive");
                }
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

    public void sendGetRequest(final String url,final Context context,final OnAPIResponse onAPIResponse){

        String apiURL = BASE_URL+url;
        Log.d("Call to URL ",apiURL);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                onAPIResponse.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onAPIResponse.onError();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                if(BuildConfig.TEST_TOKEN.equalsIgnoreCase("NO"))
                    params.put("Authorization","token "+Tools.getGeneralSharedPref(context).getString(Constants.AUTH_TOKEN,""));
                else
                    params.put("Authorization","token "+BuildConfig.TOKEN);
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


    public void sendGetWithoutHeaderRequest(final String url, final Response.Listener<String> listener, final Response.ErrorListener errorListener, final Context context){

        String apiURL = BASE_URL+url;
        Log.d("Call to URL ",apiURL);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiURL,listener,errorListener);
        int MY_SOCKET_TIMEOUT_MS = 50000;
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }


    public void sendPutRequest(final String url, final Map<String, String> params, final Response.Listener<String> listener, final Response.ErrorListener errorListener, Context context){

        String apiURL = BASE_URL+url;
        Log.d("Call to URL ",apiURL);
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, apiURL,listener,errorListener) {
            @Override
            protected Map<String, String> getParams() {
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

    public void sendPostRequestWithoutParams(final String url, final Response.Listener<String> listener, final Response.ErrorListener errorListener, final Context context){

        String apiURL = BASE_URL+url;
        Log.d("Call to URL ",apiURL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiURL,listener,errorListener)
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                if(BuildConfig.TEST_TOKEN.equalsIgnoreCase("NO"))
                    params.put("Authorization","token "+Tools.getGeneralSharedPref(context).getString(Constants.AUTH_TOKEN,""));
                else
                    params.put("Authorization","token "+BuildConfig.TOKEN);
                if(BuildConfig.DEBUG){
                    params.put("Connection", "Keep-Alive");
                }
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

    public void sendNotification(final Context context, final String title, final String body, final String tag){

        String apiURL = BASE_URL+"/dieter/v1/send_notification/";
        Log.d("Call to URL ",apiURL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response",response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               error.printStackTrace();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                if(BuildConfig.TEST_TOKEN.equalsIgnoreCase("NO"))
                    params.put("Authorization","token "+Tools.getGeneralSharedPref(context).getString(Constants.AUTH_TOKEN,""));
                else
                    params.put("Authorization","token "+BuildConfig.TOKEN);
                if(BuildConfig.DEBUG){
                    params.put("Connection", "Keep-Alive");
                }
                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("title",title);
                params.put("tag",tag);
                params.put("body",body);
                params.put("dietitianId",Tools.getGeneralSharedPref(context).getString(Constants.DIETITIAN_ID,"0"));
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

    void finishActivity(Context context){
        SharedPreferences.Editor editor = Tools.getGeneralEditor(context);
        editor.clear();
        editor.commit();
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
//        Checkout.clearUserData(context);
        try {
            ((FragmentActivity)context).finish();
        }catch (Exception e){
            e.printStackTrace();
        }
        Intent intent = new Intent(context, SignInActivity.class);
        intent.putExtra("auth_fail","1");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
