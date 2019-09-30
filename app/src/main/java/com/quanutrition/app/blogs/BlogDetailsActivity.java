package com.quanutrition.app.blogs;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.quanutrition.app.R;
import com.quanutrition.app.Utils.NetworkManager;
import com.quanutrition.app.Utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

public class BlogDetailsActivity extends AppCompatActivity {

    TextView title,body;
    ImageView image;
    WebView htmlWeb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_details);
        title = findViewById(R.id.title);
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
                       Tools.setHTMLData(body,data.getString("content"));

                        final String mimeType = "text/html";
                        final String encoding = "UTF-8";
                        String html = data.getString("content").replaceAll("<img","<img style=\"max-width:100%;\"");


//                        htmlWeb.loadDataWithBaseURL("", html, mimeType, encoding, "");


                        Tools.loadBlogImage(data.getString("image"),image);
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
        NetworkManager.getInstance(BlogDetailsActivity.this).sendGetRequest(url,listener,errorListener,BlogDetailsActivity.this);

    }

}
