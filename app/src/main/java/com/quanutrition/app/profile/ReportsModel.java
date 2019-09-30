package com.quanutrition.app.profile;

import android.net.Uri;

public class ReportsModel {
    String url;
    Uri imageuri;

    public ReportsModel(String url, Uri imageuri) {
        this.url = url;
        this.imageuri = imageuri;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Uri getImageuri() {
        return imageuri;
    }

    public void setImageuri(Uri imageuri) {
        this.imageuri = imageuri;
    }
}
