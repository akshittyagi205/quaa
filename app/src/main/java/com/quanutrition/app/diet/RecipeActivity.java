package com.quanutrition.app.diet;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.quanutrition.app.R;
import com.quanutrition.app.Utils.Tools;

public class RecipeActivity extends AppCompatActivity {

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

        title.setText(getIntent().getStringExtra("title"));
        Tools.loadBlogImage(getIntent().getStringExtra("image"),image);

        final String mimeType = "text/html";
        final String encoding = "UTF-8";
        String html = getIntent().getStringExtra("body").replaceAll("<img","<img style=\"max-width:100%;\"");

        htmlWeb.setVisibility(View.GONE);
        htmlWeb.loadDataWithBaseURL("", html, mimeType, encoding, "");
        htmlWeb.reload();
        htmlWeb.setVisibility(View.VISIBLE);

//        setupWebView();
    }


    /*private void setupWebView() {
        htmlWeb.getSettings().setJavaScriptEnabled(true);
        htmlWeb.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                htmlWeb.loadUrl("javascript:MyApp.resize(document.body.getBoundingClientRect().height)");
                super.onPageFinished(view, url);
            }
        });
        htmlWeb.addJavascriptInterface(this, "MyApp");
    }
    @JavascriptInterface
    public void resize(final float height) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                htmlWeb.setLayoutParams(new LinearLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels, (int) (height * getResources().getDisplayMetrics().density)));
            }
        });
    }*/

}
