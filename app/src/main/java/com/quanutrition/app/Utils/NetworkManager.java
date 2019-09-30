package com.quanutrition.app.Utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class NetworkManager {
    private Context mCtx;
    private static NetworkManager mInstance;

    private NetworkManager(Context context) {
        mCtx = context;
    }

    public static synchronized NetworkManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new NetworkManager(context);
        }
        return mInstance;
    }

    public void sendPostRequest(final String url, final Map<String, String> params, final Response.Listener<String> listener, final Response.ErrorListener errorListener, final Context context){

        String apiURL = "http://qua-api.herokuapp.com/"+url;
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


    public void sendPostRequestWithHeader(final String url, final Map<String, String> params, final Response.Listener<String> listener, final Response.ErrorListener errorListener, final Context context){

        String apiURL = "http://qua-api.herokuapp.com/"+url;
        Log.d("Call to URL ",apiURL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiURL,listener,errorListener)
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization","token "+Tools.getGeneralSharedPref(context).getString(Constants.AUTH_TOKEN,""));
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

    public void sendGetRequest(final String url, final Response.Listener<String> listener, final Response.ErrorListener errorListener, final Context context){

        String apiURL = "http://qua-api.herokuapp.com/"+url;
        Log.d("Call to URL ",apiURL);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiURL,listener,errorListener)
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization","token "+Tools.getGeneralSharedPref(context).getString(Constants.AUTH_TOKEN,""));
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

        String apiURL = "http://qua-api.herokuapp.com/"+url;
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

        String apiURL = "http://qua-api.herokuapp.com/"+url;
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

        String apiURL = "http://qua-api.herokuapp.com/"+url;
        Log.d("Call to URL ",apiURL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiURL,listener,errorListener)
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization","token "+Tools.getGeneralSharedPref(context).getString(Constants.AUTH_TOKEN,""));
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

        String apiURL = "http://qua-api.herokuapp.com/dieter/v1/send_notification/";
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
                params.put("Authorization","token "+Tools.getGeneralSharedPref(context).getString(Constants.AUTH_TOKEN,""));
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

}
