package com.quanutrition.app.payments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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
import com.quanutrition.app.programs.DurationModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ExtendDurationActivity extends AppCompatActivity {

    RecyclerView extension_re;
    ArrayList<DurationModel> list;
    DurationAdapter adapter;
    String programId="-1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extend_duration);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        getSupportActionBar().setTitle("Extend Plan");
//        Tools.setSystemBarColor(this,R.color.colorPrimary);

        extension_re = findViewById(R.id.extension_re);
        list = new ArrayList<>();

        /*DurationModel model = new DurationModel("1 Month","30","10000","0",false);
        model.setName("Weight Gain Program");
        list.add(model);
        list.add(model);
        list.add(model);*/

        adapter = new DurationAdapter(list,this);

        adapter.setOnClickListener(new DurationAdapter.OnItemClick() {
            @Override
            public void onClick(int pos) {
                DurationModel model = list.get(pos);
                Intent intent = new Intent(ExtendDurationActivity.this, CheckoutActivity.class);
                intent.putExtra("paymentId",getIntent().getStringExtra("id"));
                intent.putExtra("name",model.getName());
                intent.putExtra("duration",model.getDuration());
                intent.putExtra("days",model.getDays());
                intent.putExtra("is_extended","1");
                intent.putExtra("amount",model.getAmount().split(" ")[0]);
                intent.putExtra("currency",model.getAmount().split(" ")[1]);
                intent.putExtra("discount",model.getDiscount());
                intent.putExtra("has_discount",model.isHas_discount());
                intent.putExtra("programId",programId);
                startActivity(intent);
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        extension_re.setLayoutManager(layoutManager);
        extension_re.setAdapter(adapter);

        fetchData();
    }

    void fetchData(){
        final AlertDialog ad = Tools.getDialog("Fetching data...", ExtendDurationActivity.this);
        ad.show();
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ad.dismiss();
                Log.d("Response",response);
                try {
                    JSONObject ob = new JSONObject(response);
                    if(ob.getInt("res")==1){
                        JSONArray data = ob.getJSONArray("data");
                        for(int i=0;i<data.length();i++){
                            JSONObject duration = data.getJSONObject(i);
                            DurationModel model = new DurationModel(duration.getString("plan"),duration.getString("duration"),duration.getString("price")+" "+duration.getString("currency"),"0",false);
                            model.setName(duration.getString("name"));
                            list.add(model);
                            programId = duration.getInt("id")+"";
                        }
                        adapter.notifyDataSetChanged();
                    }else{
                        Tools.initCustomToast(ExtendDurationActivity.this,ob.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("myTag","I am here");
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ad.dismiss();
                Tools.initNetworkErrorToast(ExtendDurationActivity.this);
                Log.d("Error",error.toString());
                Log.d("myTag","I am here");
            }
        };

        String url = Urls.get_extensible_plans +"?pid="+getIntent().getStringExtra("id");
        NetworkManager.getInstance(this).sendGetRequest(url,listener,errorListener,this);

    }
}
