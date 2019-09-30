package com.quanutrition.app.general;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.quanutrition.app.R;
import com.quanutrition.app.Utils.Constants;
import com.quanutrition.app.Utils.NetworkManager;
import com.quanutrition.app.Utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AboutDietitianActivity extends AppCompatActivity {

    ImageView image;
    TextView languages,workHours,bio,qualifications,clinic_name,phone,address;
    LinearLayout clinic_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_dietitian);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Dietitian not found");
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//        Tools.setSystemBarColorCustom(this,R.color.textColorLight);



        image = findViewById(R.id.image);
        languages = findViewById(R.id.languages);
        workHours = findViewById(R.id.workHours);
        bio = findViewById(R.id.bio);
        qualifications = findViewById(R.id.qualifications);
        clinic_name = findViewById(R.id.clinic_name);
        phone = findViewById(R.id.phone);
        address = findViewById(R.id.address);
        clinic_layout = findViewById(R.id.clinic_layout);

        fetchData();

//        Tools.loadImageIntoImageView("https://images1-fabric.practo.com/doctor/577103/ms-nmami-agarwal-59cb8bd1cb60d.JPG",image);
    }

    void fetchData(){
        final AlertDialog ad = Tools.getDialog("Fetching data...", this);
        ad.show();
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ad.dismiss();
                try {
                    JSONObject result = new JSONObject(response);
                    if(result.getInt("res")==1){
                        JSONObject dietitian = result.getJSONObject("d_data");
                        getSupportActionBar().setTitle(dietitian.getString("name"));
                        Tools.loadImageIntoImageView(dietitian.getString("image"),image);
//                        Tools.loadProfileImage("https://www.ryanfernando.in/wp-content/uploads/2018/07/about-thumb.jpg",image);
                        JSONArray lang = dietitian.getJSONObject("language").getJSONArray("spoken");
                        String spokenLang = "";
                        for(int i=0;i<lang.length();i++){
                            if(i==0)
                                spokenLang += lang.getString(i);
                            else
                                spokenLang += ","+lang.getString(i);
                        }
                        languages.setText("Speaks : "+spokenLang);
                        bio.setText(dietitian.getString("bio"));

                        String qual = "";
                        JSONObject qualOb = dietitian.getJSONObject("qualification");
                        if(!(qualOb.getJSONObject("pg").getString("stream").isEmpty()))
                        qual += qualOb.getJSONObject("pg").getString("stream")+" , "+qualOb.getJSONObject("pg").getString("year");
                        if(!(qualOb.getJSONObject("ug").getString("stream").isEmpty()))
                        qual += "\n"+qualOb.getJSONObject("ug").getString("stream")+" , "+qualOb.getJSONObject("ug").getString("year");
                        if(!(qualOb.getJSONObject("Other").getString("stream").isEmpty()))
                            qual += "\n"+qualOb.getJSONObject("Other").getString("stream")+" , "+qualOb.getJSONObject("Other").getString("year");
                        qualifications.setText(qual);

                        if(!dietitian.getBoolean("has_clinic")){
                            clinic_layout.setVisibility(View.GONE);
                        }else{
                            clinic_layout.setVisibility(View.VISIBLE);
                            JSONObject clinic = result.getJSONObject("c_data");
                            clinic_name.setText(clinic.getString("clinic"));
                            phone.setText(clinic.getString("phone1"));
                            JSONObject addressData = clinic.getJSONObject("address");
                            address.setText(addressData.getString("City")+" , "+addressData.getString("Country"));
                        }

                        JSONArray ar = dietitian.optJSONArray("work_hour");
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                        String workHour="";
                        for(int i=0;i<ar.length();i++){
                            Date d = timeFormat.parse(ar.getString(i));
                            String formatted = sdf.format(d);
                            if(i==0){
                                workHour = formatted+" - ";
                            }else{
                                workHour+= formatted;
                            }
                        }
                        workHours.setText("Works : "+workHour);
                    }else{
                        Tools.initCustomToast(AboutDietitianActivity.this, result.getString("msg"));
                    }
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }

                Log.d("Response", response);
                Log.d("myTag", "I am here");

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ad.dismiss();
                Tools.initNetworkErrorToast(AboutDietitianActivity.this);
                Log.d("Error", error.toString());
                Log.d("myTag", "I am here");
            }
        };


        String url = Urls.Get_About+"?dietitianId="+Tools.getGeneralSharedPref(this).getString(Constants.DIETITIAN_ID,"0");

        NetworkManager.getInstance(this).sendGetRequest(url, listener, errorListener, this);

    }

}
