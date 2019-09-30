package com.quanutrition.app.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MeasurementsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<MeasurementsModel> list;
    MeasurementsViewAdapter adapter;
    RelativeLayout noData;
    TextView basicBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurements);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        recyclerView = findViewById(R.id.measurements_re);
        noData = findViewById(R.id.noData);
        basicBtn = findViewById(R.id.basicBtn);

        list = new ArrayList<>();
        /*MeasurementsModel measurementsModel = new MeasurementsModel("24-05-2019","1","12","12","12","12","12","12");
        list.add(measurementsModel);
        list.add(measurementsModel);
        list.add(measurementsModel);*/

        basicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMeasurement();
            }
        });

        adapter = new MeasurementsViewAdapter(list,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        fetchData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.measurement_add,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==R.id.add){
            addMeasurement();
        }

        return super.onOptionsItemSelected(item);
    }


    AlertDialog alertDialog1;
    void addMeasurement(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater linf = LayoutInflater.from(this);
        final View inflator = linf.inflate(R.layout.dialog_add_measurement, null);
        alertDialog.setView(inflator);
        alertDialog.setCancelable(true);
        final EditText chest = inflator.findViewById(R.id.chest);
        final EditText arms = inflator.findViewById(R.id.arms);
        final EditText waist = inflator.findViewById(R.id.waist);
        final EditText abd = inflator.findViewById(R.id.abd);
        final EditText hips = inflator.findViewById(R.id.hips);
        final EditText thighs = inflator.findViewById(R.id.thigh);

        chest.requestFocus();

        Button save = inflator.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flag=0;
                if(Tools.validateNormalText(chest)){
                    flag++;
                }
                if(Tools.validateNormalText(arms)){
                    flag++;
                }
                if(Tools.validateNormalText(waist)){
                    flag++;
                }
                if(Tools.validateNormalText(abd)){
                    flag++;
                }
                if(Tools.validateNormalText(hips)){
                    flag++;
                }
                if(Tools.validateNormalText(thighs)){
                    flag++;
                }

                if(flag==0){
                    Tools.initCustomToast(MeasurementsActivity.this,"Please fill some data");
                }else{
                    alertDialog1.dismiss();
                    requestSave(Tools.getText(chest),Tools.getText(waist),Tools.getText(arms),Tools.getText(abd),Tools.getText(hips),Tools.getText(thighs));
                }
            }
        });



        alertDialog1 = null;
        alertDialog1 = alertDialog.show();
        alertDialog1.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.dialog_background_drawable));
    }


    void requestSave(String chest, String waist, String arms, String abd, String hips, String thighs){
        final AlertDialog ad = Tools.getDialog("Saving data...", this);
        ad.show();
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ad.dismiss();
                try {
                    JSONObject result = new JSONObject(response);
                    if (result.getInt("res") == 1) {
                        fetchData();
                        Tools.initCustomToast(MeasurementsActivity.this, result.getString("msg"));
                    }
                } catch (JSONException e) {
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
                Tools.initNetworkErrorToast(MeasurementsActivity.this);
                Log.d("Error", error.toString());
                Log.d("myTag", "I am here");
            }
        };
        HashMap<String, String> params = new HashMap<>();
        params.put("arms",arms);
        params.put("chest",chest);
        params.put("waist",waist);
        params.put("hip",hips);
        params.put("thigh",thighs);
        params.put("abd",abd);

        NetworkManager.getInstance(this).sendPostRequestWithHeader(Urls.save_measurement_info,params, listener, errorListener, this);

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
                    if (result.getInt("res") == 1) {
                        list.clear();
                        JSONArray data = result.getJSONArray("data");
                        for(int i=0;i<data.length();i++){
                            JSONObject ob = data.getJSONObject(i);
                            MeasurementsModel model = new MeasurementsModel(ob.getString("date"),ob.getInt("id")+"",
                                    ob.getString("chest"),ob.getString("waist"),ob.getString("abd"),ob.getString("thigh"),ob.getString("hip"),ob.getString("arms"));

                            list.add(model);
                        }
                        adapter.notifyDataSetChanged();
                        if(data.length()>0){
                            noData.setVisibility(View.GONE);
                        }else{
                            noData.setVisibility(View.VISIBLE);
                        }

                    } else {
                        Tools.initCustomToast(MeasurementsActivity.this, result.getString("msg"));
                    }
                } catch (JSONException e) {
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
                Tools.initNetworkErrorToast(MeasurementsActivity.this);
                Log.d("Error", error.toString());
                Log.d("myTag", "I am here");
            }
        };

        NetworkManager.getInstance(this).sendGetRequest(Urls.get_measurement_info, listener, errorListener, this);

    }
}
