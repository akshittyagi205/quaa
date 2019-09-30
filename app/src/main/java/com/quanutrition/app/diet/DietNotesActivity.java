package com.quanutrition.app.diet;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.quanutrition.app.R;

public class DietNotesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_notes);

        WebView htmlNotes = findViewById(R.id.htmlNotes);
        String html = getIntent().getStringExtra("note");

        final String mimeType = "text/html";
        final String encoding = "UTF-8";

        htmlNotes.loadDataWithBaseURL("", html, mimeType, encoding, "");
    }
}
