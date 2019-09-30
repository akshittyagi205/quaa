package com.quanutrition.app.Utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class NotifyUtils {


    public void uploadPhoto(final Uri currImageURI, final Context context) throws IOException {

        Map config = new HashMap();
        config.put("cloud_name", Constants.dietitioWeb);
        config.put("api_key", "787886675331985");
        config.put("api_secret", "J6OPmk7BU9Mqcv2OYZ1oARnMeps");
        final Cloudinary cloudinary = new Cloudinary(config);
        try {
            new Thread(new Runnable() {

                @Override
                public void run() {

                    InputStream in = null;

                    try {
                        in = context.getContentResolver().openInputStream(currImageURI);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    Map uploadResult = null;
                    try {
                        uploadResult = cloudinary.uploader().upload(in, ObjectUtils.emptyMap());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (uploadResult.containsKey("url")) {
                        Log.d("hey", uploadResult.get("url") + "");
                    } else {
                        Log.d("hey", "error");

                    }

                }
            }).start();

//            new Thread(runnable).start();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
