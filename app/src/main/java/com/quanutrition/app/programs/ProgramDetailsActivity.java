package com.quanutrition.app.programs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
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
import com.quanutrition.app.payments.CheckoutActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProgramDetailsActivity extends AppCompatActivity {

    TextView title,body;
    RecyclerView duration_re;
    ArrayList<DurationModel> durationList;
    DurationAdapter durationAdapter;
    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_details);
//        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//        getSupportActionBar().setHomeButtonEnabled(false);
//        Tools.setSystemBarColor(this,R.color.colorPrimary);
//        getSupportActionBar().setTitle("Program Details");
        title = findViewById(R.id.title);
        body = findViewById(R.id.body);
        image = findViewById(R.id.image);
        duration_re = findViewById(R.id.duration_re);

        durationList = new ArrayList<>();

        durationAdapter = new DurationAdapter(durationList,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL,false);
        duration_re.setLayoutManager(layoutManager);
        duration_re.setAdapter(durationAdapter);


        durationAdapter.setOnClickListener(new DurationAdapter.OnItemClick() {
            @Override
            public void onClick(int pos) {
                DurationModel model = durationList.get(pos);
                Intent intent = new Intent(ProgramDetailsActivity.this, CheckoutActivity.class);
                intent.putExtra("programId",getIntent().getStringExtra("id"));
                intent.putExtra("name",model.getName());
                intent.putExtra("duration",model.getDuration());
                intent.putExtra("days",model.getDays());
                intent.putExtra("amount",model.getAmount().split(" ")[0]);
                intent.putExtra("currency",model.getAmount().split(" ")[1]);
                intent.putExtra("discount",model.getDiscount());
                intent.putExtra("has_discount",model.isHas_discount());
                intent.putExtra("paymentId","-1");
                startActivity(intent);
            }
        });

        fetchData();
    }

    void fetchData(){
        final AlertDialog ad = Tools.getDialog("Fetching data...", ProgramDetailsActivity.this);
        ad.show();
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ad.dismiss();
                Log.d("ResponseSlots",response);
                try {
                    JSONObject ob = new JSONObject(response);
                    if(ob.getInt("res")==1){
                        JSONObject data = ob.getJSONObject("data");
                        title.setText(data.getString("name"));
                        Tools.loadImageIntoImageView(data.getString("image"),image);
                        Tools.setHTMLData(body,data.getString("description"));
                        JSONArray amount = data.getJSONArray("amount");
                        for(int i=0;i<amount.length();i++){
                            JSONObject duration = amount.getJSONObject(i);
                            DurationModel model = new DurationModel(duration.getString("plan"),duration.getString("duration"),duration.getString("price")+" "+duration.getString("cur"),duration.optString("discount","0"),duration.optBoolean("has_discount",false));
                            model.setName(data.getString("name"));
                            durationList.add(model);
                        }
                        durationAdapter.notifyDataSetChanged();
                    }else{
                        Tools.initCustomToast(ProgramDetailsActivity.this,ob.getString("msg"));
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
                Tools.initNetworkErrorToast(ProgramDetailsActivity.this);
                Log.d("Error",error.toString());
                Log.d("myTag","I am here");
            }
        };

        String url = Urls.GET_PROGRAM_DETAILS+"?programId="+getIntent().getStringExtra("id");
        NetworkManager.getInstance(this).sendGetRequest(url,listener,errorListener,this);

    }
}
