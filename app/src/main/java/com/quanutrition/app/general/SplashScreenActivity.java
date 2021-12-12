package com.quanutrition.app.general;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.quanutrition.app.HandleTheFuckingLink;
import com.quanutrition.app.MainActivity;
import com.quanutrition.app.R;
import com.quanutrition.app.Utils.Constants;
import com.quanutrition.app.Utils.NetworkManager;
import com.quanutrition.app.Utils.Tools;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
//import com.razorpay.Checkout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.setSystemBarColorCustom(this,R.color.textColorDark);
        setContentView(R.layout.activity_splash_screen);

        try{

                Log.d("Null Intent",getIntent().getExtras().toString());
            Log.d("In Catch bro","in if");
                FirebaseDynamicLinks.getInstance()
                        .getDynamicLink(getIntent())
                        .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                            @Override
                            public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                                // Get deep link from result (may be null if no link is found)
                                Uri deepLink = null;
                                if (pendingDynamicLinkData != null) {
                                    deepLink = pendingDynamicLinkData.getLink();

                                    Log.d("Recieved Link", String.valueOf(deepLink));

                                    if(deepLink.getQueryParameter("ref").equalsIgnoreCase("0")){
                                        if (Tools.getGeneralSharedPref(SplashScreenActivity.this).getString(Constants.AUTH_TOKEN, "").isEmpty()) {
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        Thread.sleep(3000);
                                                    } catch (InterruptedException e1) {
                                                        e1.printStackTrace();
                                                    }
                                                    finish();
                                                    startActivity(new Intent(SplashScreenActivity.this, SignInActivity.class));
                                                }
                                            }).start();
                                        } else {
                                            Log.d("In Catch bro", "in else");
                                            check();
                                        }
                                    }else
                                        checkRefer(deepLink.getQueryParameter("ref"));
//                                textview.setText(String.valueOf(deepLink) + "        " +  deepLink.getQueryParameter("user"));
                                }else{
                                    Log.d("mytag","Deeplink not found");
                                    if (Tools.getGeneralSharedPref(SplashScreenActivity.this).getString(Constants.AUTH_TOKEN, "").isEmpty()) {
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    Thread.sleep(3000);
                                                } catch (InterruptedException e1) {
                                                    e1.printStackTrace();
                                                }
                                                finish();
                                                startActivity(new Intent(SplashScreenActivity.this, SignInActivity.class));
                                            }
                                        }).start();
                                    } else {
                                        Log.d("In Catch bro", "in else");
                                        check();
                                    }
                                }


                                // Handle the deep link. For example, open the linked
                                // content, or apply promotional credit to the user's
                                // account.
                                // ...

                                // ...
                            }
                        })
                        .addOnFailureListener(this, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("mytag", "getDynamicLink:onFailure", e);
                            }
                        });

        }catch (Exception e) {
            Log.d("In Catch bro","here");
            if (Tools.getGeneralSharedPref(this).getString(Constants.AUTH_TOKEN, "").isEmpty()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        finish();
                        startActivity(new Intent(SplashScreenActivity.this, SignInActivity.class));
                    }
                }).start();

            } else {
                Log.d("In Catch bro", "in else");
                check();
            }
        }
    }

    void check(){
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Response", response);
                    JSONObject result = new JSONObject(response);
                    if (result.getInt("res") == 1) {
                       SharedPreferences.Editor editor = Tools.getGeneralEditor(SplashScreenActivity.this);
                       JSONObject data = result.getJSONObject("data");
                       editor.putString(Constants.DIETITIAN_ID,data.optInt("id")+"");
                       editor.putString(Constants.DIETITIAN_NAME,data.getString("name"));
                       editor.putString(Constants.DIETITIAN_PHONE,data.getString("phone"));
                       editor.putString(Constants.DIETITIAN_PIC,data.getString("imageUrl"));
                       editor.putString(Constants.PROFILE_NAME,data.optString("user_name"));
                       editor.commit();
                       finish();
                       startActivity(new Intent(SplashScreenActivity.this,MainActivity.class));
                    }else{
                        if(result.optBoolean("basicinfo")){
                            finish();
                            startActivity(new Intent(SplashScreenActivity.this,MainActivity.class));
                        } else if(result.getBoolean("is_verified")) {
                            finish();
                            startActivity(new Intent(SplashScreenActivity.this, SignUpInfo.class));
                        }else{
                            finish();
                            startActivity(new Intent(SplashScreenActivity.this, SignInActivity.class));
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
//                Log.d("Response",response);
                Log.d("myTag","I am here");
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Tools.initNetworkErrorToast(SplashScreenActivity.this);
                showLoginDialog();
                Log.d("Error",error.toString());
                Log.d("myTag","I am here");
            }
        };
        Map<String, String> params = new HashMap<>();

        NetworkManager.getInstance(this).sendPostRequestWithHeader(Urls.Splash_Screen,params,listener,errorListener,this);

    }

    void checkRefer(final String ref){
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Response", response);
                    JSONObject result = new JSONObject(response);
                    if (result.getInt("res") == 1) {
                        finish();
                        Intent i = new Intent(SplashScreenActivity.this,HandleTheFuckingLink.class);
                        i.putExtra("name",result.optString("name"));
                        i.putExtra("is_user",result.optBoolean("isUser"));
                        i.putExtra("d_name",result.getString("d_name"));
                        i.putExtra("id",result.getInt("d_id")+"");
                        i.putExtra("user_ref",ref);
                        Tools.getGeneralEditor(SplashScreenActivity.this).putString("user_ref",ref).commit();
                        i.putExtra("dietitian_ref",result.optString("d_code"));
                        startActivity(i);
                    }else if (result.getInt("res") == 2){
                        finish();
                        startActivity(new Intent(SplashScreenActivity.this,MainActivity.class));
                    }else if (result.getInt("res") == 3){
                        finish();
                        startActivity(new Intent(SplashScreenActivity.this,SignUpInfo.class));
                    }else{
                        finish();
                        startActivity(new Intent(SplashScreenActivity.this,SignInActivity.class));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
//                Log.d("Response",response);
                Log.d("myTag","I am here");
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Tools.initNetworkErrorToast(SplashScreenActivity.this);
                finish();
                startActivity(new Intent(SplashScreenActivity.this,SignInActivity.class));
                Log.d("Error",error.toString());
                Log.d("myTag","I am here");
            }
        };

        String url = Urls.Check_Refer+"?ref_code="+ref+"&userId="+Tools.getGeneralSharedPref(this).getString(Constants.USER_ID,"-1");
        NetworkManager.getInstance(this).sendGetWithoutHeaderRequest(url,listener,errorListener,this);

    }

    void showLoginDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        // Setting Dialog Message
        alertDialog.setTitle("Authentication Failed!");
        alertDialog.setIcon(R.mipmap.ic_launcher);
        alertDialog.setCancelable(false);
        alertDialog.setMessage("There is a problem verifying your account. Sign In again to continue.");
        alertDialog.setPositiveButton("SignIn", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferences.Editor editor = Tools.getGeneralEditor(SplashScreenActivity.this);
                editor.clear();
                editor.commit();
                NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancelAll();
//                Checkout.clearUserData(getApplicationContext());
                finish();
                startActivity(new Intent(SplashScreenActivity.this,SignInActivity.class));

            }
        });
        alertDialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.show();
    }


    void saveToken(String token){
        /*final AlertDialog ad = Tools.getDialog("Saving feedback...",this);
        ad.show();*/
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                ad.dismiss();
                Log.d("ResponseSlots",response);
                try {
                    JSONObject ob = new JSONObject(response);

//                    Tools.initCustomToast(MainActivity.this,ob.getString("msg"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("myTag","I am here");
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                ad.dismiss();
                Tools.initNetworkErrorToast(SplashScreenActivity.this);
                Log.d("Error",error.toString());
                Log.d("myTag","I am here");
            }
        };
        Map<String, String> params = new HashMap<>();
        params.put("token",token);

        NetworkManager.getInstance(this).sendPostRequestWithHeader(Urls.save_token,params,listener,errorListener,this);

    }
}
