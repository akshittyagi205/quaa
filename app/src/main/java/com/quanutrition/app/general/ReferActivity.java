package com.quanutrition.app.general;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.quanutrition.app.R;
import com.quanutrition.app.Utils.Constants;
import com.quanutrition.app.Utils.NetworkManager;
import com.quanutrition.app.Utils.Tools;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import org.json.JSONArray;
import org.json.JSONObject;

public class ReferActivity extends AppCompatActivity {

    String res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        (findViewById(R.id.share)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildDeepLink();
            }
        });

        findViewById(R.id.history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReferActivity.this,ReferHistoryActivity.class);
                intent.putExtra("response",res);
                startActivity(intent);
            }
        });
        fetchData();
    }



    public void buildDeepLink() {
        /*DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://dietitio.page.link/?ref="+refCode))
                .setDomainUriPrefix("https://dietitio.page.link/")

                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                // Open links with com.example.ios on iOS
                .setIosParameters(new DynamicLink.IosParameters.Builder("com.App.DietitioClinic").build())

                .buildDynamicLink();*/

        final String refCode = Tools.getGeneralSharedPref(this).getString(Constants.REFER_CODE,"0");
        final AlertDialog ad = Tools.getDialog("Processing...",this);
        ad.show();
        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://dietitio.page.link/?ref="+refCode))
                .setDomainUriPrefix("https://dietitio.page.link/")
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                // Open links with com.example.ios on iOS
                .setIosParameters(new DynamicLink.IosParameters.Builder("com.App.DietitioClinic").build())
                .setSocialMetaTagParameters(
                        new DynamicLink.SocialMetaTagParameters.Builder()
                                .setTitle("Join "+Tools.getGeneralSharedPref(this).getString(Constants.CLINIC,"Dietitio"))
                                .setDescription("Start your health journey with "+Tools.getGeneralSharedPref(this).getString(Constants.DIETITIAN_NAME,""))
                                .build())
                .buildShortDynamicLink(ShortDynamicLink.Suffix.SHORT).addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        ad.dismiss();
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();
                            Log.d("Short linkl", shortLink.toString());
                            Log.d("Preview Link",flowchartLink.toString());
                            String dietitian = Tools.getGeneralSharedPref(ReferActivity.this).getString(Constants.DIETITIAN_NAME,"");
                            String message = "Hey, I'm enjoying getting fit with "+dietitian+"\nJoin me using my code "+refCode+"\nOr Click here : "+shortLink.toString();

                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT,message);
                            sendIntent.setType("text/plain");
                            startActivity(sendIntent);
                        } else {
                            // Error
                            // ...
                            Tools.initCustomToast(ReferActivity.this,"Some error occured! Try again.");
                        }
                    }
                });


//        Uri dynamicLinkUri = dynamicLink.getUri();




    }



    void fetchData(){
        final AlertDialog ad = Tools.getDialog("Fetching data...",this);
        ad.show();
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ad.dismiss();
                try {
                    Log.d("Response", response);
                    JSONObject result = new JSONObject(response);
                    if (result.getInt("res") == 1) {
                        JSONArray data = result.getJSONArray("data");
                        if(data.length()>0){
                            findViewById(R.id.history).setVisibility(View.VISIBLE);
                        }else{
                            findViewById(R.id.history).setVisibility(View.GONE);
                        }
                        res = response;
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
                ad.dismiss();
                Tools.initNetworkErrorToast(ReferActivity.this);
                Log.d("Error",error.toString());
                Log.d("myTag","I am here");
            }
        };

        NetworkManager.getInstance(this).sendGetRequest(Urls.referred_details,listener,errorListener,this);

    }
}
