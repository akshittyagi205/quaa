package com.quanutrition.app.chat;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.quanutrition.app.R;
import com.quanutrition.app.Utils.Tools;
import com.quanutrition.app.Utils.TouchImageView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class ImageViewActivity extends AppCompatActivity {

    String url = "";
    TouchImageView image;
    ProgressBar pb;
    FloatingActionButton downloadImage;
    Bitmap resource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_image_view);
        url = getIntent().getExtras().getString("url");
        image = findViewById(R.id.image);
        pb = findViewById(R.id.progress_bar);
        downloadImage = findViewById(R.id.downloadImage);
        downloadImage.setVisibility(View.GONE);
        downloadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveImage(resource);
            }
        });
        image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_no_image));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Glide.
                            with(ImageViewActivity.this).
                            load(url).
                            asBitmap().
                            listener(new RequestListener<String, Bitmap>() {
                                @Override
                                public boolean onException(Exception e, String model, com.bumptech.glide.request.target.Target<Bitmap> target, boolean isFirstResource) {
                                    pb.setVisibility(View.GONE);
                                    Tools.initCustomToast(ImageViewActivity.this,"Cannot load image! Try again later");
                                    finish();
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Bitmap resource, String model, com.bumptech.glide.request.target.Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    ImageViewActivity.this.resource = resource;
                                    pb.setVisibility(View.GONE);
                                    image.setImageBitmap(resource);
                                    downloadImage.setVisibility(View.VISIBLE);
                                    return false;
                                }
                            }).
                            into(640, 640). // Width and height
                            get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    void saveImage(final Bitmap bm){
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                                              try {
                                                  String root = Environment.getExternalStorageDirectory().toString();
                                                  File myDir = new File(root + "/Ediet");

                                                  if (!myDir.exists()) {
                                                      myDir.mkdirs();
                                                  }

                                                  Calendar calendar = Calendar.getInstance();
                                                  Date c = calendar.getTime();
                                                  SimpleDateFormat df = new SimpleDateFormat("ddMMyyyyHHmmss");
                                                  String time = df.format(c);
                                                  String name = time + ".png";
                                                  myDir = new File(myDir, name);
                                                  FileOutputStream out = new FileOutputStream(myDir);
//                                                  Bitmap bm=((BitmapDrawable)image.getDrawable()).getBitmap();

                                                  bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
                                                  Tools.initCustomToast(ImageViewActivity.this,"Image saved to this device.");
                                                  out.flush();
                                                  out.close();
                                              } catch(Exception e){
                                                  // some action
                                              }
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied()) {
                            // navigate user to app settings
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }
}
