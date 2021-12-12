package com.quanutrition.app.general;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.quanutrition.app.R;
import com.quanutrition.app.Utils.NetworkManager;
import com.quanutrition.app.Utils.Tools;
import com.quanutrition.app.profile.TimeInputChildModel;
import com.quanutrition.app.profile.TimeInputModel;
import com.quanutrition.app.profile.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PhysicalActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    ArrayList<PhysicalActivityModel> data;
    PhysicalActivityAdapter adapter;
    LinearLayout mon,tue,wed,thu,fri,sat,sun;
    TextView monTxt,tueTxt,wedTxt,thuTxt,friTxt,satTxt,sunTxt;
    ArrayList<PhysicalActivityModel> monData,tueData,wedData,thuData,friData,satData,sunData;
    int[] days = {R.id.mon,R.id.tue,R.id.wed,R.id.thu,R.id.fri,R.id.sat,R.id.sun};
    int[] daysText = {R.id.monTxt,R.id.tueTxt,R.id.wedTxt,R.id.thuTxt,R.id.friTxt,R.id.satTxt,R.id.sunTxt};
    RelativeLayout noData;
    int lastId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physical);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.re);
        mon = findViewById(R.id.mon);
        tue = findViewById(R.id.tue);
        wed = findViewById(R.id.wed);
        thu = findViewById(R.id.thu);
        fri = findViewById(R.id.fri);
        sat = findViewById(R.id.sat);
        sun = findViewById(R.id.sun);
        monTxt = findViewById(R.id.monTxt);
        tueTxt = findViewById(R.id.tueTxt);
        wedTxt = findViewById(R.id.wedTxt);
        thuTxt = findViewById(R.id.thuTxt);
        friTxt = findViewById(R.id.friTxt);
        satTxt = findViewById(R.id.satTxt);
        sunTxt = findViewById(R.id.sunTxt);
        noData = findViewById(R.id.noData);


        mon.setOnClickListener(this);
        tue.setOnClickListener(this);
        wed.setOnClickListener(this);
        thu.setOnClickListener(this);
        fri.setOnClickListener(this);
        sat.setOnClickListener(this);
        sun.setOnClickListener(this);

        data = new ArrayList<>();
        monData = new ArrayList<>();
        tueData = new ArrayList<>();
        wedData = new ArrayList<>();
        thuData = new ArrayList<>();
        friData = new ArrayList<>();
        satData = new ArrayList<>();
        sunData = new ArrayList<>();

        /*data.add(new PhysicalActivityModel("Running(Slow like 2Km/h)","30 Min."));
        data.add(new PhysicalActivityModel("Jogging","30 Min."));
        data.add(new PhysicalActivityModel("Cycling","30 Min."));
        data.add(new PhysicalActivityModel("Walking","30 Min."));
        data.add(new PhysicalActivityModel("Cricket","30 Min."));


        monData = tueData = wedData = thuData = friData = satData = sunData = data;*/


        adapter = new PhysicalActivityAdapter(data,this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        fetchData();

        lastId = daysText[Tools.getDayNumberToday()];
        onClick(findViewById(days[Tools.getDayNumberToday()]));
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        ((TextView)findViewById(lastId)).setTextColor(getResources().getColor(R.color.colorPrimary));
        ((TextView)findViewById(lastId)).setBackground(null);
        if(id == R.id.mon){
            lastId = R.id.monTxt;
            monTxt.setTextColor(Color.parseColor("#FFFFFF"));
            monTxt.setBackgroundResource(R.drawable.circle_yellow);
            PhysicalActivityAdapter adapter = new PhysicalActivityAdapter(monData,PhysicalActivity.this);
            recyclerView.setAdapter(adapter);
            if(monData.size()==0){
                noData.setVisibility(View.VISIBLE);
            }else{
                noData.setVisibility(View.GONE);
            }

        }else if(id == R.id.tue){
            lastId = R.id.tueTxt;
            tueTxt.setTextColor(Color.parseColor("#FFFFFF"));
            tueTxt.setBackgroundResource(R.drawable.circle_yellow);
            PhysicalActivityAdapter adapter = new PhysicalActivityAdapter(tueData,PhysicalActivity.this);
            recyclerView.setAdapter(adapter);
            if(tueData.size()==0){
                noData.setVisibility(View.VISIBLE);
            }else{
                noData.setVisibility(View.GONE);
            }
        }else if(id == R.id.wed){
            lastId = R.id.wedTxt;
            wedTxt.setTextColor(Color.parseColor("#FFFFFF"));
            wedTxt.setBackgroundResource(R.drawable.circle_yellow);
            PhysicalActivityAdapter adapter = new PhysicalActivityAdapter(wedData,PhysicalActivity.this);
            recyclerView.setAdapter(adapter);
            if(wedData.size()==0){
                noData.setVisibility(View.VISIBLE);
            }else{
                noData.setVisibility(View.GONE);
            }
        }
        else if(id == R.id.thu){
            lastId = R.id.thuTxt;
            thuTxt.setTextColor(Color.parseColor("#FFFFFF"));
            thuTxt.setBackgroundResource(R.drawable.circle_yellow);
            PhysicalActivityAdapter adapter = new PhysicalActivityAdapter(thuData,PhysicalActivity.this);
            recyclerView.setAdapter(adapter);
            if(thuData.size()==0){
                noData.setVisibility(View.VISIBLE);
            }else{
                noData.setVisibility(View.GONE);
            }
        }
        else if(id == R.id.fri){
            lastId = R.id.friTxt;
            friTxt.setTextColor(Color.parseColor("#FFFFFF"));
            friTxt.setBackgroundResource(R.drawable.circle_yellow);
            PhysicalActivityAdapter adapter = new PhysicalActivityAdapter(friData,PhysicalActivity.this);
            recyclerView.setAdapter(adapter);
            if(friData.size()==0){
                noData.setVisibility(View.VISIBLE);
            }else{
                noData.setVisibility(View.GONE);
            }
        }
        else if(id == R.id.sat){
            lastId = R.id.satTxt;
            satTxt.setTextColor(Color.parseColor("#FFFFFF"));
            satTxt.setBackgroundResource(R.drawable.circle_yellow);
            PhysicalActivityAdapter adapter = new PhysicalActivityAdapter(satData,PhysicalActivity.this);
            recyclerView.setAdapter(adapter);
            if(satData.size()==0){
                noData.setVisibility(View.VISIBLE);
            }else{
                noData.setVisibility(View.GONE);
            }
        }else if(id == R.id.sun){
            lastId = R.id.sunTxt;
            sunTxt.setTextColor(Color.parseColor("#FFFFFF"));
            sunTxt.setBackgroundResource(R.drawable.circle_yellow);
            PhysicalActivityAdapter adapter = new PhysicalActivityAdapter(sunData,PhysicalActivity.this);
            recyclerView.setAdapter(adapter);
            if(sunData.size()==0){
                noData.setVisibility(View.VISIBLE);
            }else{
                noData.setVisibility(View.GONE);
            }
        }

        recyclerView.startAnimation(AnimationUtils.loadAnimation(this,android.R.anim.fade_in));
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
                        /*JSONObject data = result.getJSONObject("data");
                        monData = loadData(data.getJSONArray("MON"));
                        tueData = loadData(data.getJSONArray("TUE"));
                        wedData = loadData(data.getJSONArray("WED"));
                        thuData = loadData(data.getJSONArray("THU"));
                        friData = loadData(data.getJSONArray("FRI"));
                        satData = loadData(data.getJSONArray("SAT"));
                        sunData = loadData(data.getJSONArray("SUN"));*/


                        JSONObject data = result.getJSONObject("data");
                        JSONObject trainingArray = data.getJSONObject("trainingActivity");
                        if(trainingArray.has("monday"))
                            monData = loadDataFromList(trainingArray.getJSONArray("monday"),"Monday","0");
                        if(trainingArray.has("tuesday"))
                            tueData = loadDataFromList(trainingArray.getJSONArray("tuesday"),"Tuesday","1");
                        if(trainingArray.has("wednesday"))
                            wedData = loadDataFromList(trainingArray.getJSONArray("wednesday"),"Wednesday","2");
                        if(trainingArray.has("thursday"))
                            thuData = loadDataFromList(trainingArray.getJSONArray("thursday"),"Thursday","3");
                        if(trainingArray.has("friday"))
                            friData = loadDataFromList(trainingArray.getJSONArray("friday"),"Friday","4");
                        if(trainingArray.has("saturday"))
                            satData = loadDataFromList(trainingArray.getJSONArray("saturday"),"Saturday","5");
                        if(trainingArray.has("sunday"))
                            sunData = loadDataFromList(trainingArray.getJSONArray("sunday"),"Sunday","6");

                        /*lastId = R.id.monTxt;
                        monTxt.setTextColor(Color.parseColor("#FFFFFF"));
                        monTxt.setBackgroundResource(R.drawable.circle_yellow);
                        PhysicalActivityAdapter adapter = new PhysicalActivityAdapter(monData,PhysicalActivity.this);
                        if(monData.size()==0){
                            noData.setVisibility(View.VISIBLE);
                        }else{
                            noData.setVisibility(View.GONE);
                        }
                        recyclerView.setAdapter(adapter);*/

                        lastId = daysText[Tools.getDayNumberToday()];
                        onClick(findViewById(days[Tools.getDayNumberToday()]));
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
                Tools.initNetworkErrorToast(PhysicalActivity.this);
                Log.d("Error",error.toString());
                Log.d("myTag","I am here");
            }
        };

        NetworkManager.getInstance(this).sendPostRequestWithoutParams(Urls.get_activity_info,listener,errorListener,this);

    }

    ArrayList<PhysicalActivityModel> loadData(JSONArray data){
        ArrayList<PhysicalActivityModel> list = new ArrayList<>();
        try {
        for(int i=0;i<data.length();i++){
            JSONObject ob = null;

                ob = data.getJSONObject(i);

            float min = Float.parseFloat(ob.getString("minute"));
            String duration = "";
            Log.d("Division",min/60 +"");
            if(min/60<1f){
                duration = (int)min+" min.";
            }else{
                if(min/60<2f)
                duration = (int)(min/60)+" hour "+(int)(min%60)+" min.";
                else
                    duration = (int)(min/60)+" hours "+(int)(min%60)+" min.";
            }
            PhysicalActivityModel model = new PhysicalActivityModel(ob.getString("activity"),duration);
            model.setNotes(ob.getString("note"));
            model.setId(ob.getString("pk"));
            list.add(model);
        }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("Size of List",list.size()+"");
        return list;
    }

    ArrayList<PhysicalActivityModel> loadDataFromList(JSONArray array, String day, String id) throws JSONException {

        ArrayList<PhysicalActivityModel> list = new ArrayList<>();
        for(int i=0;i<array.length();i++){
            JSONObject ob = array.getJSONObject(i);
            /*TimeInputChildModel childModel = new TimeInputChildModel(ob.getString("From"),ob.getString("To"));
            childModel.setActivity(ob.getString("Activity"));

            childList.add(childModel);*/

            PhysicalActivityModel model = new PhysicalActivityModel(ob.getString("Activity"),Tools.getDuration(ob.getString("From"),ob.getString("To")));
            model.setNotes("");
            model.setId(id);
            model.setFrom(ob.getString("From"));
            model.setTo(ob.getString("To"));
            list.add(model);
        }

        return list;
    }
}
