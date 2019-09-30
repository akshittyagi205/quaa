package com.quanutrition.app.blogs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.quanutrition.app.R;
import com.quanutrition.app.Utils.NetworkManager;
import com.quanutrition.app.Utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SocialMediaGridActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<ListModel> list;
    GalleryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media_grid);

        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Social Media");
        list=new ArrayList<>();


        ListModel mediaModel = new ListModel("https://res.cloudinary.com/dietitioweb/image/upload/v1567162378/46231111_327952294466473_4343606653915100417_n.jpg","1");
        ListModel mediaModel1 = new ListModel("https://res.cloudinary.com/dietitioweb/image/upload/v1567162378/42817614_185216395759153_7235482963145162043_n.jpg","1");
        ListModel mediaModel2 = new ListModel("https://res.cloudinary.com/dietitioweb/image/upload/v1567162378/45392915_323938901762199_7613445403551522007_n.jpg","1");

        /*list.add(mediaModel);
        list.add(mediaModel1);
        list.add(mediaModel2);
        list.add(mediaModel);
        list.add(mediaModel1);
        list.add(mediaModel2);
        list.add(mediaModel);
        list.add(mediaModel1);
        list.add(mediaModel2);
        list.add(mediaModel);
        list.add(mediaModel1);
        list.add(mediaModel2);*/

        recyclerView = findViewById(R.id.re);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        recyclerView.setHasFixedSize(true);

        adapter = new GalleryAdapter(this, list, new GalleryAdapter.OnShare() {
            @Override
            public void onShare(String message, Uri image) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, image);
                shareIntent.putExtra(Intent.EXTRA_TEXT,message);
                shareIntent.setType("image/*");
                startActivity(Intent.createChooser(shareIntent, "Share Image"));
            }
        });
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
                        JSONArray ar = ob.getJSONArray("response");
                        for(int i=0;i<ar.length();i++){
                            JSONObject res = ar.getJSONObject(i);
                            ListModel model = new ListModel(res.optString("image"),res.optString("id")+"");
//                            model.setTag(res.optString("category","Myth and Facts"));
                            list.add(model);
                        }
                        adapter.notifyDataSetChanged();
                    }else{
                        Tools.initCustomToast(SocialMediaGridActivity.this,ob.getString("msg"));
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
                Tools.initNetworkErrorToast(SocialMediaGridActivity.this);
                Log.d("Error",error.toString());
                Log.d("myTag","I am here");
            }
        };
        NetworkManager.getInstance(this).sendGetRequest(Urls.GET_GALLERY,listener,errorListener,this);

    }
}

