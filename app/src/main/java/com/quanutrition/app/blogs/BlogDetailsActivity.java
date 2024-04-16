package com.quanutrition.app.blogs;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.quanutrition.app.R;
import com.quanutrition.app.Utils.NetworkManager;
import com.quanutrition.app.Utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class BlogDetailsActivity extends AppCompatActivity {

    TextView title,body,added_on;
    ImageView image;
    WebView htmlWeb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_details);

        Log.d("response", getIntent().getStringExtra("type"));
        Log.d("response type", getIntent().getStringExtra("type").getClass().getSimpleName());
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        if (getIntent().getStringExtra("type").equalsIgnoreCase("4")){
            getSupportActionBar().setTitle("Testimonials");
        }else if (getIntent().getStringExtra("type").equalsIgnoreCase("3")){
            getSupportActionBar().setTitle("Recipe Details");
        }
        title = findViewById(R.id.title);
        added_on = findViewById(R.id.added_on);
        body = findViewById(R.id.body);
        image = findViewById(R.id.image);

        htmlWeb = findViewById(R.id.htmlWeb);

        fetchData();
    }

    void fetchData(){
        final AlertDialog ad = Tools.getDialog("Fetching data...",BlogDetailsActivity.this);
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
                        title.setText(data.getString("title"));
                        added_on.setText("Published On : " + data.getString("added_on"));
                        if (!data.getString("image").isEmpty()){
                            image.setVisibility(View.VISIBLE);
                            Tools.loadImageIntoImageView(data.getString("image"),image);
                        }else {
                            image.setVisibility(View.GONE);
                        }
                       Tools.setHTMLData(body,data.getString("content"));

                        final String mimeType = "text/html";
                        final String encoding = "UTF-8";
                        String html = data.getString("content").replaceAll("<img","<img style=\"max-width:100%;\"");


//                        htmlWeb.loadDataWithBaseURL("", html, mimeType, encoding, "");

                    }else{
                        Tools.initCustomToast(BlogDetailsActivity.this,ob.getString("msg"));
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
                Tools.initNetworkErrorToast(BlogDetailsActivity.this);
                Log.d("Error",error.toString());
                Log.d("myTag","I am here");
            }
        };
        String url = Urls.GET_BLOG_DETAILS+"?id="+getIntent().getStringExtra("id");
        Log.d("response",url.toString());
        NetworkManager.getInstance(BlogDetailsActivity.this).sendGetRequest(url,listener,errorListener,BlogDetailsActivity.this);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
