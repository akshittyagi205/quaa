package com.quanutrition.app.general;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.quanutrition.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReferHistoryActivity extends AppCompatActivity {

    RecyclerView re;
    ArrayList<ReferModel> list;
    ReferAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_history);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        re = findViewById(R.id.re);

        list = new ArrayList<>();

        String response = getIntent().getStringExtra("response");
        try{
            JSONObject ob = new JSONObject(response);
            if(ob.getInt("res")==1){
                JSONArray data = ob.getJSONArray("data");
                for(int i=0;i<data.length();i++){
                    JSONObject ob1 = data.getJSONObject(i);
                    ReferModel model = new ReferModel(ob1.getString("name"),ob1.getString("image"),ob1.getString("date_joined"),ob1.getString("status"));
                    list.add(model);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter = new ReferAdapter(list,this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        re.setLayoutManager(layoutManager);
        re.setAdapter(adapter);
    }
}
