package com.quanutrition.app.programs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.quanutrition.app.R;
import com.quanutrition.app.Utils.NetworkManager;
import com.quanutrition.app.Utils.SpacingItemDecoration;
import com.quanutrition.app.Utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProgramsActivity extends AppCompatActivity {

    RecyclerView plans_view;
    ArrayList<ProgramModel> plans;
    ProgramsGridAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programs);
//        Tools.setSystemBarColorDark(this, R.color.colorPrimary);

        plans_view = findViewById(R.id.plans_view);

        plans = new ArrayList<>();

        ProgramModel model = new ProgramModel("1","Weight Loss","",false);
        ProgramModel model1 = new ProgramModel("1","Weight Gain","",false);


        adapter = new ProgramsGridAdapter(this,plans);
        adapter.setOnItemClickListener(new ProgramsGridAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ProgramModel obj, int position) {
                Intent intent = new Intent(ProgramsActivity.this,ProgramDetailsActivity.class);
                intent.putExtra("id",plans.get(position).getId());
                startActivity(intent);
            }
        });
        plans_view.setLayoutManager(new GridLayoutManager(this, 2));
        plans_view.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(this, 15), true));

        plans_view.setAdapter(adapter);

        fetchData();
    }

    void fetchData(){
        final AlertDialog ad = Tools.getDialog("Fetching data...", ProgramsActivity.this);
        ad.show();
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ad.dismiss();
                Log.d("ResponseSlots",response);
                try {
                    JSONObject ob = new JSONObject(response);
                    if(ob.getInt("res")==1){
                        JSONArray data = ob.getJSONArray("data");
                        for(int i=0;i<data.length();i++) {
                            JSONObject ob1 = data.getJSONObject(i);
                            ProgramModel model = new ProgramModel(ob1.getInt("id")+"",ob1.getString("name"),ob1.optString("image","https://quanutrition.com/images/package/1.jpg"),false);
                            plans.add(model);
                        }
                        adapter.notifyDataSetChanged();
                    }else{
                        Tools.initCustomToast(ProgramsActivity.this,ob.getString("msg"));
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
                Tools.initNetworkErrorToast(ProgramsActivity.this);
                Log.d("Error",error.toString());
                Log.d("myTag","I am here");
            }
        };

        NetworkManager.getInstance(this).sendGetRequest(Urls.GET_ALL_PROGRAMS,listener,errorListener,this);

    }
}
