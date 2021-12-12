package com.quanutrition.app.general;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.quanutrition.app.R;
import com.quanutrition.app.Utils.NetworkManager;
import com.quanutrition.app.Utils.Tools;
import com.quanutrition.app.blogs.GalleryAdapter;
import com.quanutrition.app.blogs.ListModel;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ContactUsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<ContactUsModel> list;
    ContactUsAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Contact Us");
        recyclerView = findViewById(R.id.re);

        list = new ArrayList<>();

        adapter = new ContactUsAdapter(list,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        fetchData();
    }

    void fetchData(){
        final AlertDialog ad = Tools.getDialog("Fetching data...",this);
        ad.show();
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ad.dismiss();
                Log.d("ResponseSlots",response);
                try {
                    JSONObject ob = new JSONObject(response);
                    if(ob.getInt("res")==1){
                        list.clear();
                        JSONArray ar = ob.getJSONArray("data");
                        for(int i=0;i<ar.length();i++){
                            JSONObject res = ar.getJSONObject(i);
//                            ListModel model = new ListModel(res.optString("image"),res.optString("id")+"");

                            String address = res.getString("address1");
                            if(!res.getString("address2").isEmpty())
                            address += ", "+res.getString("address2");
                            if(!res.getString("city").isEmpty())
                                address += ", "+res.getString("city");
                            if(!res.getString("state").isEmpty())
                                address += ", "+res.getString("state");
                            if(!res.getString("country").isEmpty())
                                address += ", "+res.getString("country");
                            ContactUsModel model = new ContactUsModel(res.getString("city"),address,res.getString("phone"),res.getString("pemail"),"");

//                            model.setTag(res.optString("category","Myth and Facts"));
                            list.add(model);
                        }
                        adapter.notifyDataSetChanged();
                    }else{
                        Tools.initCustomToast(ContactUsActivity.this,ob.getString("msg"));
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
                Tools.initNetworkErrorToast(ContactUsActivity.this);
                Log.d("Error",error.toString());
                Log.d("myTag","I am here");
            }
        };
        NetworkManager.getInstance(this).sendGetRequest(Urls.Contact_us,listener,errorListener,this);

    }
}
